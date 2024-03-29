package de.fh_luebeck.jsn.doit.acitivites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.fh_luebeck.jsn.doit.R;
import de.fh_luebeck.jsn.doit.tasks.CheckWebAppReachableTask;
import de.fh_luebeck.jsn.doit.tasks.LoginTask;

/**
 * Activity
 * - Login
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.login_error)
    TextView _loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        // Check if Server is reachable
        new CheckWebAppReachableTask(this).execute();

        _loginButton.setEnabled(false);
    }

    private void enableLoginButtonIfFormSet() {
        if (_emailText.getText().toString().isEmpty() || _passwordText.getText().toString().isEmpty()) {
            _loginButton.setEnabled(false);
        } else {
            _loginButton.setEnabled(true);
        }
    }

    private void hideErrorMessage() {
        if (_loginError.getVisibility() == View.VISIBLE) {
            _loginError.setVisibility(View.GONE);
        }
    }

    @OnTextChanged(value = R.id.input_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void passwordUpdated() {
        enableLoginButtonIfFormSet();
        hideErrorMessage();
        _passwordText.setError(null);
    }

    @OnTextChanged(value = R.id.input_email, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void eMailChanged() {
        enableLoginButtonIfFormSet();
        hideErrorMessage();
        _emailText.setError(null);
    }

    @OnClick(R.id.btn_login)
    public void login() {

        _loginButton.setEnabled(false);

        if (isFormValid()) {
            // Validierung über HTTP durchführen
            new LoginTask(this, _emailText.getText().toString(), _passwordText.getText().toString(), _loginError).execute();
        }

        _loginButton.setEnabled(true);
    }

    private boolean isFormValid() {
        boolean valid = true;

        String mail = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            _emailText.setError("Keine gültige E-Mail-Adresse.");
            valid = false;
        }

        boolean passwordNumeric = true;
        try {
            Integer.parseInt(password);
        } catch (NumberFormatException e) {
            passwordNumeric = false;
        }

        if (passwordNumeric == false) {
            _passwordText.setError("Passwort muss numerisch sein.");
            valid = false;
        }

        if (password.isEmpty() || password.length() != 6) {
            _passwordText.setError("Passwort muss 6 Stellen haben.");
            valid = false;
        }

        return valid;
    }
}

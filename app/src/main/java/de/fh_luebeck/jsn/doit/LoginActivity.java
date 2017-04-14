package de.fh_luebeck.jsn.doit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static de.fh_luebeck.jsn.doit.AppConstants.INTENT_EXTRA_WEB_APP_AVAILABLE;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        // Check wether Server is reachable
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

    @OnTextChanged(value = R.id.input_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void passwordUpdated() {
        enableLoginButtonIfFormSet();
        _passwordText.setError(null);
    }

    @OnTextChanged(value = R.id.input_email, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void eMailChanged() {
        enableLoginButtonIfFormSet();
        _emailText.setError(null);
    }

    @OnClick(R.id.btn_login)
    public void login() {

        _loginButton.setEnabled(false);

        if (isFormValid()) {
            // Validierung über HTTP durchführen
            new LoginTask(this, _emailText.getText().toString(), _passwordText.getText().toString()).execute();
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

    class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private Activity activity;
        private ProgressDialog progressDialog;

        private String eMail;
        private String password;

        LoginTask(Activity caller, String eMail, String password) {
            this.activity = caller;
            this.progressDialog = new ProgressDialog(activity);

            this.eMail = eMail;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // TODO jsn: Server aufruf

            // TODO Produktion: entfernen
            if (eMail.equals("test@admin.de") && password.equals("123456")) {
                return true;
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog.setMessage("Login ...");
            this.progressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (success) {
                Toast.makeText(activity, "Login erfolgreich", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(activity, OverviewActivity.class);
                intent.putExtra(INTENT_EXTRA_WEB_APP_AVAILABLE, true);
                startActivity(intent);
            } else {
                Toast.makeText(activity, "Login nicht vorhanden", Toast.LENGTH_LONG).show();
                // TODO jsn: Permanenter Fehler
            }
        }
    }

    class CheckWebAppReachableTask extends AsyncTask<Void, Void, Boolean> {

        private Activity activity;
        private ProgressDialog progressDialog;

        CheckWebAppReachableTask(Activity caller) {
            this.activity = caller;
            this.progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                InetAddress.getByName(AppConstants.WEB_APP_URL).isReachable(3000);
                return true; // No Exception -> Reachable
            } catch (Exception e) {
                Log.i(TAG, "Connection to webapp no existing", e);
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog.setMessage("Prüfe Verbindung...");
            this.progressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (success) {
                Toast.makeText(activity, "Verbindung vorhanden, bitte anmelden", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Verbindung nicht vorhanden", Toast.LENGTH_LONG).show();

                // Direkter Wechsel in die Übersicht
                Intent intent = new Intent(activity, OverviewActivity.class);
                intent.putExtra(INTENT_EXTRA_WEB_APP_AVAILABLE, false);
                startActivity(intent);
            }
        }
    }
}

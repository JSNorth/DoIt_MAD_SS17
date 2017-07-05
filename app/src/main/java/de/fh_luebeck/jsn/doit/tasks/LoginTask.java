package de.fh_luebeck.jsn.doit.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

import de.fh_luebeck.jsn.doit.acitivites.OverviewActivity;
import de.fh_luebeck.jsn.doit.data.User;
import de.fh_luebeck.jsn.doit.webservice.UserWebserviceFactory;
import retrofit2.Response;

import static de.fh_luebeck.jsn.doit.util.AppConstants.INTENT_EXTRA_WEB_APP_AVAILABLE;

/**
 * Created by USER on 14.04.2017.
 */

public class LoginTask extends AsyncTask<Void, Void, Boolean> {

    private Activity activity;
    private ProgressDialog progressDialog;
    private TextView errorText;

    private String eMail;
    private String password;

    public LoginTask(Activity caller, String eMail, String password, TextView errorText) {
        this.activity = caller;
        this.progressDialog = new ProgressDialog(activity);

        this.eMail = eMail;
        this.password = password;

        this.errorText = errorText;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        // TODO Produktion: entfernen
        if (eMail.equals("test@admin.de") && password.equals("123456")) {
            return true;
        }

        try {
            Response<Boolean> response = UserWebserviceFactory.getUserWebService().authenticateUser(new User(eMail, password)).execute();
            if (response.isSuccessful() == false) {
                Log.e(LoginTask.class.getSimpleName(), "Fehler beim Webservice");
                return false;
            }

            return response.body();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Login nicht vorhanden", Toast.LENGTH_LONG).show();
            errorText.setVisibility(View.VISIBLE);
        }
    }
}

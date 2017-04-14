package de.fh_luebeck.jsn.doit.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;

import de.fh_luebeck.jsn.doit.acitivites.OverviewActivity;
import de.fh_luebeck.jsn.doit.util.AppConstants;

import static de.fh_luebeck.jsn.doit.util.AppConstants.INTENT_EXTRA_WEB_APP_AVAILABLE;

/**
 * Created by USER on 14.04.2017.
 */

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

package de.fh_luebeck.jsn.doit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class OverviewActivity extends AppCompatActivity {

    private boolean isWebAppReachable;

    @InjectView(R.id.toolbar)
    private Toolbar _toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        setSupportActionBar(_toolbar);

        Intent intent = getIntent();
        isWebAppReachable = intent.getBooleanExtra(AppConstants.INTENT_EXTRA_WEB_APP_AVAILABLE, false); // False als Default, dann kommt es nicht zu Problemen

    }

    @OnClick(R.id.fab)
    public void createNewTodo(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}

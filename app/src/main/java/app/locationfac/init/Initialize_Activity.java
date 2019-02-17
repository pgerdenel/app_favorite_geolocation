package app.locationfac.init;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import app.locationfac.R;
import app.locationfac.articulate.TemplateGPSStateActivity;

/*
 * Class permettant d'initialiser l'application
 * - l'activation du gps et du réseau en informant l'utilisateur s'ils sont désactivés
 */

public class Initialize_Activity extends TemplateGPSStateActivity implements View.OnClickListener {

    private Button button_logo;

    // Shared Preferences
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        // Pour cacher la barre de statut ( heure, etat du reseau etc ... )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Pour masquer la barre de navigation
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_initialize);

        // On initialiser les shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        button_logo = (Button) findViewById(R.id.button_logo);
        button_logo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       Initialize_CircleRotateActivity.startActivity(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /** Méthodes de l'activity ***/
    @Override
    public void onRestart() {
        super.onRestart();
        //Log.d("init OnRestart Called", "ononRestart a été appelée");
    }
    @Override
    public void onPause() {
        super.onPause();
        //Log.d("init OnPause Called", "onPause a été appelée");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        //Log.d("init OnResume Called", "onResume a été appelée");
    }
    @Override
    public void onStop() {
        super.onStop();
        //Log.d("init OnStop Called", "onStop a été appelée");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //Log.d("init OnDestroy Called", "onDestroy a été appelée");
    }

    /******************* Interface Location Listenner ****************/
    @Override
    public void onLocationChanged(Location location) {
        Log.d("OnLocationChanged calld", "OnLocationChanged called");
    }
    @Override
    public void onStatusChanged(String s, int status, Bundle bundle) {
        Log.d("onStatusChanged called", "onStatusChanged called");
    }
    @Override
    public void onProviderEnabled(String s) {
        Log.d("onProviderEnabled calld", "onProviderEnabled called");
    }
    @Override
    public void onProviderDisabled(String s) {
        Log.d("onProviderDisabled call", "onProviderDisabled called");
    }
}
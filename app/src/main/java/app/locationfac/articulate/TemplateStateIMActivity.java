package app.locationfac.articulate;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/*
    Template Activity qui gère
    -la désactivation du GPS
    -la désactivation du Réseau
    -l'immersive mode
    -les états de l'activity(onPause, Onstart, OnResume, etc...)
*/

public class TemplateStateIMActivity extends AppCompatActivity implements LocationListener {

    // Service GPS
    LocationManager locManager;
    // Service Wifi
    WifiManager wifiManager;
    // AlertDialog
    private AlertDialog alertDialog;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toggleHideyBar();
    }

    public void onStart() {
        super.onStart();
        // On remet l'application en immersive mode
        toggleHideyBar();
        // On récupère le service de GPS
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // On récupère le service WiFi d'Android
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // si le gps est désactivé et internet aussi
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isOnline())
        {
            Log.d("Tmp check gps", "GPS et internet sont désactivés");
            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Pour continuer, veuillez vous connecter à internet et activer le GPS", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle user action
                }
            });
            snackbar.show();
        }
        // si le gps est désactivé
        else if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("Tmp check gps", "GPS est désactivé");
            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Pour continuer, veuillez activer le GPS", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle user action
                }
            });
            snackbar.show();
        }
        // si internet est désactivé
        else if (!isOnline()) {
            Log.d("Tmp check gps", "GPS est désactivé");
            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Pour continuer, veuillez vous connecté à internet svp", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle user action
                }
            });
            snackbar.show();
        }
        else {
            Log.d("Tmp check gps", "Internet et le gps sont activés");
        }
    }

    // Fonctions du cycle de vie de l'Activity
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("tmp OnRestart Called", "ononRestart a été appelée");
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        Log.d("tmp OnPause Called", "onPause a été appelée");
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // On cache la navigation et la statut bar
        toggleHideyBar();

        Log.d("tmp OnResume Called", "onResume a été appelée");
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isOnline())
        {
            Log.d("Tmp check gps", "GPS et internet sont désactivés");
            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Pour continuer, veuillez vous connecter à internet et activer le GPS", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle user action
                }
            });
            snackbar.show();
        }
        // si le gps est désactivé
        else if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("Tmp check gps", "GPS est désactivé");
            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Pour continuer, veuillez activer le GPS", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle user action
                }
            });
            snackbar.show();
        }
        // si internet est désactivé
        else if (!isOnline()) {
            Log.d("Tmp check gps", "GPS est désactivé");
            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Pour continuer, veuillez vous connecté à internet svp", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle user action
                }
            });
            snackbar.show();
        }
        else {
            Log.d("Tmp check gps", "Internet et le gps sont activés");
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("tmp OnStop Called", "onStop a été appelée");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
        Log.d("tmp OnDestroy Called", "onDestroy a été appelée");
    }

    /* Interface LocationListenner */
    public void onLocationChanged(Location location) {
        Log.d("OnLocationChanged calld", "OnLocationChanged called");
    }
    public void onStatusChanged(String s, int status, Bundle bundle) {
        Log.d("onStatusChanged called", "onStatusChanged called");
    }
    public void onProviderEnabled(String s) {
        Log.d("onProviderEnabled calld", "onProviderEnabled called");

    }
    public void onProviderDisabled(String s) {
        Log.d("onProviderDisabled call", "onProviderDisabled called");

        Log.d("Tmp check gps", "GPS et internet sont désactivés");
        snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Pour continuer, veuillez activer le GPS", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle user action
            }
        });
        snackbar.show();
    }
    public boolean isOnline() {
        // Fonction qui détermine si 'utilisateur est connecté à internet
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public void toggleHideyBar() {

        int uiOptions = TemplateStateIMActivity.this.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Immersive", "Turning immersive mode mode off. ");
        } else {
            Log.i("Immersive", "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        TemplateStateIMActivity.this.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}

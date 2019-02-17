package app.locationfac.init;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.util.Random;

import app.locationfac.R;
import app.locationfac.endroit.Endroit;
import app.locationfac.maintabbed.MainTabbedActivity;
import app.locationfac.ressources.localisation.GPSTracker;
import app.locationfac.ressources.stockage.local.UseBDD_Endroit;

/*
 * Class permettant d'initialiser l'application
    - Détermine les coordonnées GPS
    - Creer ou récupèrer les données de l'utilisateur depuis la BDD local/distante/sharedprefererences
    - faire patienter l'utilisateur avec une animation de chargement
*/

public class Initialize_CircleRotateActivity extends AppCompatActivity {

    // Variables de localisation
    private double longitude = 0;
    private double latitude = 0;
    private double altitude =0;
    private double accuracy = 0;

    // Token d'existance des BDD, profil, variables utilisateurs
    private boolean isExistant_BDD_ENDROIT = false;    // si la BDD PROFIL_ENDROIT existe

    // Token de contenu des BDD Endroit
    private boolean isEmpty_BDD_ENDROIT = true;      // si la BDD ENDROIT contient des entrées

    // Helper d'utilisation pour manier les BDD SQLite
    UseBDD_Endroit useBDD_endroit;

    // Shared Preferences
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    // Variables de récupération des informations utilisateurs
    Endroit endroit;
    int param_current[]=new int[4];
    GPSTracker gps;

    // Ressources & objets utilitaires
    Gson gson;

    // AsyncTask de traitement
    private InitAsyncTask initAsyncTask;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, Initialize_CircleRotateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_rotate);
        // Pour masquer la barre de navigation
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        // Pour cacher la barre de statut ( heure, etat du reseau etc ... )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* On anime la progress bar horizontal selon l'avancement de l'activité en elle même
            On anime la progress bar circulaire tous le temps où l'activité travaille */
        // On initialise les composants des preférences partagées
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        /* On instancie l'objet gson */
        gson = new Gson();

        /* On instancie la class de géolocalisation */
        gps = new GPSTracker(Initialize_CircleRotateActivity.this);

        /* On instancie les BDD */
        useBDD_endroit = new UseBDD_Endroit(this);

        /* on initialize certaines variables */
        gson = new Gson();

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            altitude = gps.getAltitude();
            accuracy = gps.getAccuracy();

            Log.i("latitude", "latitude"+latitude);
            Log.i("longitude", "longitude"+longitude);
            Log.i("altitude", "altitude"+altitude);
            Log.i("accuracy", "accuracy"+accuracy);
        }

        initAsyncTask = new InitAsyncTask();
        initAsyncTask.execute();
     }

    /*
    Regroupe l'ensemble des traitements d'initialisation de l'application
        1. Récupérer les Objet Endroit enregistrés en BDD
        2. Mettre à jour la liste des Endroit affiché et l'enregistrer dans la BDD profil local
    */
    public void init() {

        // On fait des vérifications sur la BDD ENDROIT
        isExistant_BDD_ENDROIT = useBDD_endroit.isExistingDB(); // si la BDD existe
        isEmpty_BDD_ENDROIT = useBDD_endroit.isEmptyDB();   // si la BDD est vide ?


        Log.d("isExistant_BDD_ENDROIT", "isExistant_BDD_ENDROIT="+String.valueOf(isExistant_BDD_ENDROIT));
        Log.d("isExistant_BDD_LOGIN_A", "isEmpty_BDD_ENDROIT="+String.valueOf(isEmpty_BDD_ENDROIT));

/* 1 - VERIFICATION DES BDD ET RECUPERATION DES INFORMATIONS */
        if (!isExistant_BDD_ENDROIT ) {
            Log.i("Traitement -1-", "Les BDD ont besoin d'être rafraichit");
            /* On verifie si la table de la BDD profil_local contient des données (table remplie) */
            if (!isEmpty_BDD_ENDROIT) {
                Log.i("Traitement 1.1", "La tables de la BDD ENDROIT n'est pas vide et contient des entrées");
                // on récupère les endroits en BDD distante

            } else {
                Log.i("Traitement 1.1", "La tables de la BDD ENDROIT ont besoin d'être crée");
                // la table de la BDD endroit ne contient pas de données (table vide)
            }
        }


        else
        {
            Log.i("Traitement 1", "La BDD ENDROIT est opérationnelle");
            // On affiche toutes les bdd
            useBDD_endroit.open();
            Log.v("Traitement 1", "***************  BDD ENDROIT  ***************\n"+ useBDD_endroit.queryAllEndroitAsString());
            useBDD_endroit.close();

            // Toutes les BDD sont opérationnelles
            isExistant_BDD_ENDROIT = true;
        }

/*2 VERICATION DU STATUT DU COMPTE ET REDIRECTION VERS L'ACTIVITY EN QUESTION*/
        Log.d("isExistant_endroit", String.valueOf(isExistant_BDD_ENDROIT));;

        // On affiche toutes les bdd
        useBDD_endroit.open();
        Log.v("Traitement 1", "***************  BDD ENDROIT ***************\n"+ useBDD_endroit.queryAllEndroitAsString());
        useBDD_endroit.close();

        if (isExistant_BDD_ENDROIT)
        {
                    Intent intenta = new Intent(Initialize_CircleRotateActivity.this, MainTabbedActivity.class);
                    // On réunie les données de l'utilisateur pour les passer en extra
                    intenta.putExtra("activitycalling", "init");
                    startActivity(intenta);
        }
        else {
            Log.e("erreurs token", "erreur au niveau du token d'existance de la création BDD Endroit et d'accès");
        }
    }

    /** Méthodes de l'activity ***/
    @Override
    public void onRestart() {
        super.onRestart();
        //Log.d("init OnRestart Called", "ononRestart a été appelée");
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        //Log.d("init OnPause Called", "onPause a été appelée");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // @TODO

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        toggleHideBar();
        initAsyncTask = new Initialize_CircleRotateActivity.InitAsyncTask();
        initAsyncTask.execute();
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
    }

    /******************* Interface IDUser *************/
    public String getAleaOf(int min, int max, int taille) {
        max = 39;
        String chaine_alea="";
        String chaine = "abcdefghijklmnopqrstuvwxyz#-_@123456789";
        char[] tabChain = new char[chaine.length()];
        char[] tabChainTmp = new char[taille];
        tabChain = chaine.toCharArray();
        int indice=0;

        // Fonction random
        Random rand= new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        // Fin Fonction random

        for (int i=0;i<tabChainTmp.length;i++) {
            indice = rand.nextInt(((max-1) - min) + 1) + min;
            tabChainTmp[i] = tabChain[indice];
        }
        chaine_alea = String.copyValueOf(tabChainTmp);

        return chaine_alea;
    }
    /************** ALL ASYNCTASK ***********************/
    private class InitAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
           // On lance notre traitement qui doit se faire de façon asynchrone
            init();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    /**
     * Détecte et adapte l'immersive mode (mode bar caché)
     */
    public void toggleHideBar() {

        int uiOptions = Initialize_CircleRotateActivity.this.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Immersive", "Turning immersive mode mode off. ");
        } else {
            Log.i("Immersive", "Turning immersive mode mode on.");
        }

        // On cache la navigation bar:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // On cache la statut bar: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        Initialize_CircleRotateActivity.this.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}

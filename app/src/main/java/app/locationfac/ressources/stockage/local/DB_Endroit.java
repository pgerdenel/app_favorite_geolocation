package app.locationfac.ressources.stockage.local;


/*
Activity Qui Fait les Requetes SQLite pour les profil user temporaire
la classe d'instanciation de la base
La classe de création de la base de données SQLite
Stocke les informations d'un objet profil_local
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.DATABASE_NAME;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.DATABASE_VERSION;

public class DB_Endroit extends SQLiteOpenHelper {

    public static class Constants implements BaseColumns {
        // Le nom de la BDD
        public static final String DATABASE_NAME = "EndroitBDD.db";
        // La version de la BDD
        public static final int DATABASE_VERSION = 1;
        // Le nom de la table
        public static final String TABLE_ENDROIT = "table_endroit";

        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_ID_ENDROIT = "idendroit";
        public static final String KEY_COL_NOM = "nom";
        public static final String KEY_COL_ADRESSE = "adresse";
        public static final String KEY_COL_CODEPOSTAL = "codepostal";
        public static final String KEY_COL_VILLE = "ville";
        public static final String KEY_COL_LATITUDE = "latitude";
        public static final String KEY_COL_LONGITUDE = "longitude";
        public static final String KEY_COL_ALTITUDE = "altitude";
        public static final String KEY_COL_ACCURACY = "accuracy";

        // Index des colonnes
        // The index of the column ID
        public static final int ID_COLUMN = 1;
        public static final int IDENDROIT_COLUMN = 2;
        public static final int NOM_COLUMN = 3;
        public static final int ADRESSE_COLUMN = 4;
        public static final int CODEPOSTAL_COLUMN = 5;
        public static final int VILLE_COLUMN = 6;
        public static final int LATITUDE_COLUMN = 7;
        public static final int LONGITUDE_COLUMN = 8;
        public static final int ALTITUDE_COLUMN = 9;
        public static final int ACCURACY_COLUMN = 10;
    }

    // The static string to create the database.
    private static final String DATABASE_CREATE = "create table "
            + Constants.TABLE_ENDROIT
            + "("
            + Constants.KEY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Constants.KEY_COL_ID_ENDROIT + " TEXT UNIQUE, "
            + Constants.KEY_COL_NOM + " TEXT, "
            + Constants.KEY_COL_ADRESSE + " TEXT, "
            + Constants.KEY_COL_CODEPOSTAL + " TEXT, "
            + Constants.KEY_COL_VILLE + " TEXT, "
            + Constants.KEY_COL_LATITUDE + " REAL, "
            + Constants.KEY_COL_LONGITUDE + " REAL, "
            + Constants.KEY_COL_ALTITUDE + " REAL, "
            + Constants.KEY_COL_ACCURACY + " REAL) ";

    /**
     * @param context
     */
    public DB_Endroit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the new database using the SQL string Database_create
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion
                + " vers la version " + newVersion
                + ", les anciennes données seront détruites ");
        // Drop the old database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_ENDROIT);
        onCreate(db);
    }

    // Méthode qui permet de renvoyer une table en String (pôur l'afficher par example)
    public String getTableAsString(SQLiteDatabase db) {
        String tableString = String.format("Table %s:\n", Constants.TABLE_ENDROIT);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + Constants.TABLE_ENDROIT, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }
}

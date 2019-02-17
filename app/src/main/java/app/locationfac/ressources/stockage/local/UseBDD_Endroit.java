package app.locationfac.ressources.stockage.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.locationfac.endroit.Endroit;

import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_ACCURACY;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_ADRESSE;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_ALTITUDE;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_CODEPOSTAL;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_ID_ENDROIT;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_LATITUDE;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_LONGITUDE;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_NOM;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.KEY_COL_VILLE;
import static app.locationfac.ressources.stockage.local.DB_Endroit.Constants.TABLE_ENDROIT;

/**
 * Created by HKS on 15/11/2016.
 * Class qui permet de créer et caractériser des objets UseBDD_Endroit pour utiliser la BDD SQLite
 *
 */

public class UseBDD_Endroit {

/* Attributs */

    // The database
    private SQLiteDatabase db;
    // The database creator and updater helper
    DB_Endroit db_endroit;
    // Variables contenant toutes les colonnes
    private String[] allColumns = {
    KEY_COL_ID_ENDROIT, KEY_COL_NOM, KEY_COL_ADRESSE, KEY_COL_CODEPOSTAL, KEY_COL_VILLE,
    KEY_COL_LATITUDE, KEY_COL_LONGITUDE, KEY_COL_ALTITUDE, KEY_COL_ACCURACY
    };
    
    // Constructeur ( permet de d'ouvrir une base ou de la créer si elle n'existe pas )
    public UseBDD_Endroit(Context context) {
        db_endroit = new DB_Endroit(context);
    }

    // Méthodes et procédures
    
    /* ALL INIT */
    public void open() throws SQLException {
       db = db_endroit.getWritableDatabase();
    }
    public void close() {
        db_endroit.close();
    }
    
    /* ALL FOR BDD or TABLE */
    // Méthode permettant de déterminer si une base de donnée existe ou pas
    public boolean isExistingDB() {
        try {
            db = db_endroit.getReadableDatabase();
        }
        catch (SQLiteException se) {
            return false;
        }
        return true;
    }
    // Méthode permettant de déterminer si une base de donnée contient des tables ou pas
    public boolean isEmptyDB() {
        db = db_endroit.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_ENDROIT+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    // Méthode permettant de déterminer si une table contient des entrées ou est vide
    public boolean isEmptyTable() { // Le nom de la table pourrait être passé en paramètre
        db_endroit.onOpen(db);
        String count = "SELECT * FROM "+TABLE_ENDROIT;
        int icount=0;
        try {
            Cursor mcursor = db.rawQuery(count, null);
            mcursor.moveToFirst();
            icount = mcursor.getInt(0);
        }
        catch (SQLiteException s) {
            return false;
        }
        catch (CursorIndexOutOfBoundsException ciobe) {
            if(icount>0) {
                return false;
            }
            else {
                return true;
            }
        }
        if(icount>0) {
            return false;
        }
        else {
            return true;
        }
    }
    
    /* ALL INSERT */
    // Méthode permettant d'insérer un Endroit en BDD
    public long insertRecord_Endroit(Endroit endroit) throws SQLiteDatatypeMismatchException, SQLiteConstraintException {
        ContentValues contentValues = new ContentValues();
        try {
            // Assigne les valeurs que prend notre enregistrement
            contentValues.put(KEY_COL_ID_ENDROIT, (endroit.getId_Endroit()));
            contentValues.put(KEY_COL_NOM, endroit.getNom());
            contentValues.put(KEY_COL_ADRESSE, endroit.getAdresse());
            contentValues.put(KEY_COL_CODEPOSTAL, endroit.getCodepostal());
            contentValues.put(KEY_COL_VILLE, endroit.getVille());
            contentValues.put(KEY_COL_LATITUDE, endroit.getLatitude());
            contentValues.put(KEY_COL_LONGITUDE, endroit.getLongitude());
            contentValues.put(KEY_COL_ALTITUDE, endroit.getAltitude());
            contentValues.put(KEY_COL_ACCURACY, endroit.getAccuracy());

            // Insert the line in the database
            long rowId = db.insertOrThrow(TABLE_ENDROIT, null, contentValues);
            // Test to see if the insertion was ok
            if (rowId == -1) {
                Log.d("BDDSQLITE insert erreur", "Erreur de creation de Endroit");
            } else {
                Log.d("BDDSQLITE insert ok", "Endroit crée et stocké en base de donnée avec l'id "+rowId);
            }
            return rowId;
        }
        catch (SQLiteDatatypeMismatchException sme) {
            // On gère l'exception dans le cas où les données insérées sont déjà insérées
            // vider la table
            Log.d("SQLiteDatatypeMismatch", "La Table et les valeurs existent déjà en BDD SQLite!");
        } catch (SQLiteConstraintException sce) {
            // On gère l'exception levée pour la violation de l'unicité de la clé primaire(valeur d'ID déjà entré)
            Log.d("SQLiteConstraint", "Violation de l'unicité d'un champ !");

        }
        return -1;
    }
    
    /* ALL UPDATES */
    // Méthode permettant d'updater un enregistrement par son idEndroit
    public Endroit updateRecord_EndroitByIDEndroit(Endroit endroit, String idEndroit) {
        ContentValues contentValues = new ContentValues();
        try {
            // Assigne les valeurs que prend notre enregistrement
            contentValues.put(KEY_COL_ID_ENDROIT, (endroit.getId_Endroit()));
            contentValues.put(KEY_COL_NOM, endroit.getNom());
            contentValues.put(KEY_COL_ADRESSE, endroit.getAdresse());
            contentValues.put(KEY_COL_CODEPOSTAL, endroit.getCodepostal());
            contentValues.put(KEY_COL_VILLE, endroit.getVille());
            contentValues.put(KEY_COL_LATITUDE, endroit.getLatitude());
            contentValues.put(KEY_COL_LONGITUDE, endroit.getLongitude());
            contentValues.put(KEY_COL_ALTITUDE, endroit.getAltitude());
            contentValues.put(KEY_COL_ACCURACY, endroit.getAccuracy());

            // Insert the line in the database
            long rowId = db.update(TABLE_ENDROIT, contentValues, KEY_COL_ID_ENDROIT + "= ?", new String[]{idEndroit});
            // Test to see if the insertion was ok
            if (rowId == -1) {
                Log.d("BDDSQLITE update erreur", "Erreur de MAJ de l'endroit par idendroit");
            } else {
                Log.d("BDDSQLITE update ok", "Endroit updaté par idendroit portant l'idendroit "+idEndroit+" et l'id "+rowId);
            }
            //return queryEndroit_IDEndroit(idEndroit);
        }
        catch (SQLiteDatatypeMismatchException sme)
        {
            // On gère l'exception dans le cas où les données insérées sont déjà insérées
            // vider la table
            Log.d("SQLiteDatatypeMismatch", "La Table et les valeurs existent déjà en BDD SQLite!");
        }
        catch (SQLiteConstraintException sce)
        {
            // On gère l'exception levée pour la violation de l'unicité de la clé primaire(valeur d'ID déjà entré)
            Log.d("SQLiteConstraint", "Violation de l'unicité d'un champ !");

        }
        return endroit;
    }
    // Méthode permettant d'updater le nom de la position d'un endroit par son idEndroit
    /*public Endroit updateRecord_PostionNomByIdEndroit(Endroit endroit, String nom) {
        ContentValues contentValues = new ContentValues();
        try {
            // Assigne les valeurs que prend notre enregistrement
            contentValues.put(KEY_COL_ID_ENDROIT, (endroit.getId_Endroit()));
            contentValues.put(KEY_COL_NOM, endroit.getNom());
            contentValues.put(KEY_COL_ADRESSE, endroit.getAdresse());
            contentValues.put(KEY_COL_CODEPOSTAL, endroit.getCodepostal());
            contentValues.put(KEY_COL_VILLE, endroit.getVille());
            contentValues.put(KEY_COL_LATITUDE, endroit.getLatitude());
            contentValues.put(KEY_COL_LONGITUDE, endroit.getLongitude());
            contentValues.put(KEY_COL_ALTITUDE, endroit.getAltitude());
            contentValues.put(KEY_COL_ACCURACY, endroit.getAccuracy());

            // Insert the line in the database
            long rowId = db.update(TABLE_ENDROIT, contentValues, KEY_COL_NOM + "= ?", new String[]{nom});
            // Test to see if the insertion was ok
            if (rowId == -1) {
                Log.d("BDDSQLITE update erreur", "Erreur de MAJ de l'endroit");
            } else {
                Log.d("BDDSQLITE update ok", "endroit updaté par nom portant l'idendroit "+nom+" et l'id "+rowId);
            }
        }
        catch (SQLiteDatatypeMismatchException sme)
        {
            // On gère l'exception dans le cas où les données insérées sont déjà insérées
            // vider la table
            Log.d("SQLiteDatatypeMismatch", "La Table et les valeurs existent déjà en BDD SQLite!");
        }
        catch (SQLiteConstraintException sce)
        {
            // On gère l'exception levée pour la violation de l'unicité de la clé primaire(valeur d'ID déjà entré)
            Log.d("SQLiteConstraint", "Violation de l'unicité d'un champ !");

        }
        return endroit;
    }*/
    // Méthode permettant d'updater un enregistrement par son nom
    public Endroit updateRecord_EndroitByNom(Endroit endroit, String nom) {
        ContentValues contentValues = new ContentValues();
        try {
            // Assigne les valeurs que prend notre enregistrement
            contentValues.put(KEY_COL_ID_ENDROIT, (endroit.getId_Endroit()));
            contentValues.put(KEY_COL_NOM, endroit.getNom());
            contentValues.put(KEY_COL_ADRESSE, endroit.getAdresse());
            contentValues.put(KEY_COL_CODEPOSTAL, endroit.getCodepostal());
            contentValues.put(KEY_COL_VILLE, endroit.getVille());
            contentValues.put(KEY_COL_LATITUDE, endroit.getLatitude());
            contentValues.put(KEY_COL_LONGITUDE, endroit.getLongitude());
            contentValues.put(KEY_COL_ALTITUDE, endroit.getAltitude());
            contentValues.put(KEY_COL_ACCURACY, endroit.getAccuracy());

            // Insert the line in the database
            long rowId = db.update(TABLE_ENDROIT, contentValues, KEY_COL_NOM + "= ?", new String[]{nom});
            // Test to see if the insertion was ok
            if (rowId == -1) {
                Log.d("BDDSQLITE update erreur", "Erreur de MAJ de l'endroit");
            } else {
                Log.d("BDDSQLITE update ok", "endroit updaté par nom portant l'idendroit "+nom+" et l'id "+rowId);
            }
        }
        catch (SQLiteDatatypeMismatchException sme)
        {
            // On gère l'exception dans le cas où les données insérées sont déjà insérées
            // vider la table
            Log.d("SQLiteDatatypeMismatch", "La Table et les valeurs existent déjà en BDD SQLite!");
        }
        catch (SQLiteConstraintException sce)
        {
            // On gère l'exception levée pour la violation de l'unicité de la clé primaire(valeur d'ID déjà entré)
            Log.d("SQLiteConstraint", "Violation de l'unicité d'un champ !");

        }
        return endroit;
    }

    /* ALL DELETE */
    // Méthode permettant de supprimer un Endroit de la bdd selon l'idEndroit spécifié en paramètre
    public String deleteRecord_Endroit_WithIDEndroit(String id_endroit) {
        db.delete(TABLE_ENDROIT, DB_Endroit.Constants.KEY_COL_ID_ENDROIT +
                "= ?", new String[]{id_endroit});
        Log.d("LL deleted with idLL ", String.valueOf(id_endroit));
        return id_endroit;
    }
    // Méthode permettant de supprimer un Endroit de la bdd selon le nom spécifié en paramètre
    public String deleteRecord_Endroit_WithNom(String nom) {
        db.delete(TABLE_ENDROIT, DB_Endroit.Constants.KEY_COL_NOM +
                "= ?", new String[]{nom});
        Log.d("Endroit del idEndroit ", String.valueOf(nom));
        return nom;
    }
    
    /** ALL QUERIES
     * @param
     * @throws NullPointerException if returned queried object is null
     *
     */
    // Méthode permettant de renvoyer un Endroit correspondant à l'id_endroit de l'objet Endroit passé en paramètre
    public Endroit queryEndroit_IDEndroit(String idendroit) {
            return cursorToEndroit(queryEndroitCursor_ByIDEndroit(idendroit));
    }
    // Méthode pour renvoyer un Endroit correspondant au nom passé en paramèrte
    public Endroit queryEndroit_Nom(String nom) {
        return cursorToEndroit(queryEndroitCursor_ByNom(nom));
    }

    // Méthode permettant de renvoyer le cursor correspondant àl'idendroit passé en paramètre
    public Cursor queryEndroitCursor_ByIDEndroit(String idendroit) {
        // On définit la projection qui représente quelles sont les colonnes que nous voulons récupérer
        // ==> SELECT KEY_COL_NOM
        String[] projections = new String[] {
                KEY_COL_ID_ENDROIT,
                KEY_COL_NOM,
                KEY_COL_ADRESSE,
                KEY_COL_CODEPOSTAL,
                KEY_COL_VILLE,
                KEY_COL_LATITUDE,
                KEY_COL_LONGITUDE,
                KEY_COL_ALTITUDE,
                KEY_COL_ACCURACY
        };

        final int cursorIdLoginColNumber = 1, cursorLoginColNumber = 2, cursorEmailColNumber = 3, cursorPasswordColNumber = 4,
                cursorDATELCColNumber = 5, cursorHEURELCColNumber = 6, cursorStatutColNumber = 7, cursorMacWColNumber = 8,
                cursorMacGColNumber = 9;

        String selection= DB_Endroit.Constants.KEY_COL_ID_ENDROIT+"=?";
        String[] selectionArg=new String[] {idendroit};

        // The groupBy clause:
        String groupBy = null;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = null;
        String orderBy = DB_Endroit.Constants.KEY_COL_ID_ENDROIT+"  ASC";
        // Maximal size of the results list
        String maxResultsListSize = "50";

        Cursor cursor = db.query(TABLE_ENDROIT, projections, selection, selectionArg, null, null, null);

        return cursor;
    }
    // Méthode permettant de renvoyer le cursor correspondant  au nom passé en paramètre
    public Cursor queryEndroitCursor_ByNom(String nom) {
        // On définit la projection qui représente quelles sont les colonnes que nous voulons récupérer
        // ==> SELECT KEY_COL_NOM
        String[] projections = new String[] {
                KEY_COL_ID_ENDROIT,
                KEY_COL_NOM,
                KEY_COL_ADRESSE,
                KEY_COL_CODEPOSTAL,
                KEY_COL_VILLE,
                KEY_COL_LATITUDE,
                KEY_COL_LONGITUDE,
                KEY_COL_ALTITUDE,
                KEY_COL_ACCURACY
        };

        final int cursorIdColNumber = 1, cursorLoginColNumber = 2, cursorEmailColNumber = 3, cursorPasswordColNumber = 4,
                cursorDATELCColNumber = 5, cursorHEURELCColNumber = 6, cursorStatutColNumber = 7, cursorMacWColNumber = 8,
                cursorMacGColNumber = 9;

        String selection= DB_Endroit.Constants.KEY_COL_NOM+"=?";
        String[] selectionArg=new String[] {nom};

        // The groupBy clause:
        String groupBy = null;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = null;
        String orderBy = DB_Endroit.Constants.KEY_COL_NOM+"  ASC";
        // Maximal size of the results list
        String maxResultsListSize = "50";

        Cursor cursor = db.query(TABLE_ENDROIT, projections, selection, selectionArg, null, null, null);

        return cursor;
    }

    // Méthode permettant de renvoyer tous les Endroit sous forme de List<Endroit>
    public List<Endroit> queryAllEndroit() {
        List<Endroit> endroitList = new ArrayList<Endroit>();
        Cursor cursor = db.query(TABLE_ENDROIT, allColumns, null, null, null, null, null);
        Log.d("CursorCount", String.valueOf(cursor.getCount()));

        if (cursor != null && cursor.getCount() >0 ) {
            cursor.moveToFirst();

            do {
                // Iterate over cursor
                endroitList.add(new Endroit(cursorToEndroit1(cursor)));
                Log.d("cursor LL move", (new Endroit(cursorToEndroit1(cursor))).toString());
            } while (cursor.moveToNext());

            // assurez-vous de la fermeture du curseur
            cursor.close();
        }
        return endroitList;
    }
    // Méthode permettant de renvoyer tous les Endroit sous forme de Endroit[]
    public Endroit[] queryAllEndroit_tab() {
        Endroit tab_endroit[] = new Endroit[10];
        Cursor cursor = db.query(TABLE_ENDROIT, allColumns, null, null, null, null, null);
        Log.d("CursorCount", String.valueOf(cursor.getCount()));
        int i=0;
        if (cursor != null && cursor.getCount() >0 ) {
            cursor.moveToFirst();

            do {
                // Iterate over cursor
                tab_endroit[i] = new Endroit(cursorToEndroit1(cursor));
                Log.d("cursor LL move", (tab_endroit[i].toString())+" index="+i);
                i++;
            } while (cursor.moveToNext());
            // assurez-vous de la fermeture du curseur
            cursor.close();
        }
        return tab_endroit;
    }
    // Méthode permettant de renvoyer tous les Endroit sous forme de string
    public String queryAllEndroitAsString() {
        String allEntry = "";
        List<Endroit> list = queryAllEndroit();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                allEntry += list.get(i).toString() + "\n";
            }
        }
        else {
            allEntry = "no data";
        }
        return allEntry;
    }
    
    /* ALL QUERIES CHECK */
    // Méthode pour savoir si un Endroit (correspondant à l'id_endroit passé en paramètre existe)
    public boolean queryEndroit_IsIDEndroitExist(String id_endroit) {
        boolean isExisting =false;
        // On définit la projection qui représente quelles sont les colonnes que nous voulons récupérer
        // ==> SELECT KEY_COL_NOM
        String[] projections = new String[] {
                KEY_COL_ID_ENDROIT,
        };

        final int cursorIdLoginColNumber = 1, cursorLoginColNumber = 2, cursorEmailColNumber = 3, cursorPasswordColNumber = 4,
                cursorDATELCColNumber = 5, cursorHEURELCColNumber = 6, cursorStatutColNumber = 7, cursorMacWColNumber = 8,
                cursorMacGColNumber = 9;

        String selection= DB_Endroit.Constants.KEY_COL_ID_ENDROIT+"=?";
        String[] selectionArg=new String[] {id_endroit};

        // The groupBy clause:
        String groupBy = null;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = null;
        String orderBy = DB_Endroit.Constants.KEY_COL_ID_ENDROIT+"  ASC";
        // Maximal size of the results list
        String maxResultsListSize = "50";

        Cursor cursor = db.query(TABLE_ENDROIT, projections, selection, selectionArg, null, null, null);
        if (cursor != null) {
            isExisting = true;
        }
        return isExisting;
    }
    
    /* ALL Cursor Convertors */
    // Méthode permet de convertir un cursor en un Endroit
    public Endroit cursorToEndroit(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;
        c.moveToFirst();
        //On créé un Endroit
        String idEndroit = c.getString(0);
        String nom = c.getString(1);
        String adresse =c.getString(2);
        String codepostal = c.getString(3);
        String ville = c.getString(4);
        Double latitude = c.getDouble(5);
        Double longitude = c.getDouble(6);
        Double altitude = c.getDouble(7);
        Double accuracy = c.getDouble(8);

        Endroit endroit = new Endroit(idEndroit, nom, adresse, codepostal, ville, latitude, longitude, altitude, accuracy);
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return endroit;
    }
    // Méthode permettant de convertir un cursor en un Endroit pour de multiple Endroit
    public Endroit cursorToEndroit1(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        //c.moveToFirst();
        String idEndroit = c.getString(0);
        String nom = c.getString(1);
        String adresse =c.getString(2);
        String codepostal = c.getString(3);
        String ville = c.getString(4);
        Double latitude = c.getDouble(5);
        Double longitude = c.getDouble(6);
        Double altitude = c.getDouble(7);
        Double accuracy = c.getDouble(8);
        Endroit endroit = new Endroit(idEndroit, nom, adresse, codepostal, ville, latitude, longitude, altitude, accuracy);
        //On ferme le cursor
        //c.close();

        //On retourne le livre
        return endroit;
    }

    // Others
    public Endroit queryDBALLENDROIT(Cursor cursor) {
        // On définit la projection qui représente quelles sont les colonnes que nous voulons récupérer
        // ==> SELECT KEY_COL_NOM
        String[] projections = new String[] {
                KEY_COL_ID_ENDROIT,
                KEY_COL_NOM,
                KEY_COL_ADRESSE,
                KEY_COL_CODEPOSTAL,
                KEY_COL_VILLE,
                KEY_COL_LATITUDE,
                KEY_COL_LONGITUDE,
                KEY_COL_ALTITUDE,
                KEY_COL_ACCURACY
        };

        final int cursorIdColNumber = 1, cursorLoginColNumber = 2, cursorEmailColNumber = 3, cursorPasswordColNumber = 4,
                cursorDATELCColNumber = 5, cursorHEURELCColNumber = 6, cursorStatutColNumber = 7, cursorMacWColNumber = 8,
                cursorMacGColNumber = 9;

        // On ajoute une clause where à notre requête
        //String selection = DB_Profil_User.Constants.KEY_COL_ID_Endroit + "=?";
        String selection = null;
        //String[] selectionArg = new String[] { String.valueOf(id) };
        String[] selectionArg = null;
        // The groupBy clause:
        String groupBy = null;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = null;
        // Maximal size of the results list
        String maxResultsListSize = null;
        cursor = db.query(TABLE_ENDROIT, projections, selection, selectionArg, having, orderBy, maxResultsListSize);

        // Using the QueryBuilder to Create a Query SQLite object
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_ENDROIT);
        qb.setDistinct(true);
        cursor = qb.query(db, projections, selection, selectionArg, groupBy,
                having, orderBy);
        Endroit endroit_tmp = new Endroit();
        endroit_tmp = cursorToEndroit1(cursor);
        return endroit_tmp;
    }
    public Endroit queryDBALLENDROIT() {
        // On définit la projection qui représente quelles sont les colonnes que nous voulons récupérer
        // ==> SELECT KEY_COL_NOM
        String[] projections = new String[] {
                KEY_COL_ID_ENDROIT,
                KEY_COL_NOM,
                KEY_COL_ADRESSE,
                KEY_COL_CODEPOSTAL,
                KEY_COL_VILLE,
                KEY_COL_LATITUDE,
                KEY_COL_LONGITUDE,
                KEY_COL_ALTITUDE,
                KEY_COL_ACCURACY
        };

        final int cursorIdColNumber = 1, cursorLoginColNumber = 2, cursorEmailColNumber = 3, cursorPasswordColNumber = 4,
                cursorDATELCColNumber = 5, cursorHEURELCColNumber = 6, cursorStatutColNumber = 7, cursorMacWColNumber = 8,
                cursorMacGColNumber = 9;

        // On ajoute une clause where à notre requête
        //String selection = DB_Profil_User.Constants.KEY_COL_ID_Endroit + "=?";
        String selection = null;
        //String[] selectionArg = new String[] { String.valueOf(id) };
        String[] selectionArg = null;
        // The groupBy clause:
        String groupBy = null;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = null;
        // Maximal size of the results list
        String maxResultsListSize = null;
        //Cursor cursor = db.query(TABLE_ENDROIT, projections, selection, selectionArg, having, orderBy, maxResultsListSize);

        // Using the QueryBuilder to Create a Query SQLite object
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_ENDROIT);
        qb.setDistinct(true);
        Cursor cursor = qb.query(db, projections, selection, selectionArg, groupBy,
                having, orderBy);
        Endroit endroit_tmp = new Endroit();
        endroit_tmp = cursorToEndroit1(cursor);
        return endroit_tmp;
    }

    // Getters & setters
    public SQLiteDatabase getDb() {
        return db;
    }
    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }
    public DB_Endroit getdb_endroit() {
        return db_endroit;
    }
    public void setdb_endroit(DB_Endroit db_endroit) {
        this.db_endroit = db_endroit;
    }

}

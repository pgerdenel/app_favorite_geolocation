package app.locationfac.endroit;

import java.util.Random;

/**
 * Created by HcZ on 14/03/2017.
 * Permet de caractériser un objet Endroit (item de la listView)
 */

public class Endroit {

    String id_Endroit;
    String nom;
    String adresse;
    String codepostal;
    String ville;
    Double latitude;
    Double longitude;
    Double altitude;
    Double accuracy;

    public Endroit() {
        id_Endroit = Build_ID("0", "A");
        nom= "bat2";
        adresse ="2 rue des rouliers";
        codepostal = "51100";
        ville ="Reims";
        latitude = 0.0;
        longitude = 0.0;
        altitude = 0.0;
        accuracy = 0.0;
    }

    public Endroit(String nom, String adresse, String codepostal, String ville, Double latitude, Double longitude, Double altitude, Double accuracy) {
        this.id_Endroit = Build_ID("0", "A");
        this.nom = nom;
        this.adresse = adresse;
        this.codepostal = codepostal;
        this.ville = ville;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
    }

    public Endroit(String id_Endroit, String nom, String adresse, String codepostal, String ville, Double latitude, Double longitude, Double altitude, Double accuracy) {
        this.id_Endroit = id_Endroit;
        this.nom = nom;
        this.adresse = adresse;
        this.codepostal = codepostal;
        this.ville = ville;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
    }

    public Endroit(Endroit endroit) {
        this.id_Endroit =endroit.id_Endroit;
        this.nom = endroit.nom;
        this.adresse = endroit.adresse;
        this.codepostal = endroit.codepostal;
        this.ville = endroit.ville;
        this.latitude = endroit.latitude;
        this.longitude = endroit.longitude;
        this.altitude = endroit.altitude;
        this.accuracy = endroit.accuracy;
    }


    @Override
    public String toString() {
        return "Endroit{" +
                "id_Endroit='" + id_Endroit + '\'' +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", codepostal='" + codepostal + '\'' +
                ", ville='" + ville + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", accuracy=" + accuracy +
                '}';
    }

    public String getId_Endroit() {
        return id_Endroit;
    }

    public void setId_Endroit(String id_Endroit) {
        this.id_Endroit = id_Endroit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public String Build_ID(String entier_one, String letter_two) {
        String test="";
        test = getAleaOf(1, 5, 6);
        String str_id = entier_one+letter_two+test;
        return str_id;
    }

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
}

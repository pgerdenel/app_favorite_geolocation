package app.locationfac.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HcZ on 09/02/2017.
 * Class Objet MarkerPoints permettant de stocker les données d'un marker
 * Utilisé pour àinstancier plusieurs marqueurs sur une carte Google map
 */

public class MarkerPoints {

    LatLng latLng;
    String Title;
    String snippet; // other text shown in addition with title
    int icon;

    public MarkerPoints(LatLng latLng, String title, String snippet, int icon) {
        this.latLng = latLng;
        Title = title;
        this.snippet = snippet;
        this.icon = icon;
    }

    public LatLng getLatLng() {
        return latLng;
    }
    public double getLat() {
        return latLng.latitude;
    }
    public double getLng() {
        return latLng.longitude;
    }
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
}

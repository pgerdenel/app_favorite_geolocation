package app.locationfac.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

import app.locationfac.R;
import app.locationfac.endroit.Endroit;
import app.locationfac.ressources.localisation.GPSTracker;
import app.locationfac.ressources.stockage.local.UseBDD_Endroit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
    GoogleMap.OnInfoWindowClickListener {

    private GoogleApiClient mGoogleApiClient;   // API Google maps
    private SupportMapFragment mapFragment;     // Fragment qui héberge la map Google map
    private GoogleMap mMap;                     // MAP Google map
    private Marker mCurrLocationMarker;         // Marker de notre position
    private Marker mSpecLocationMarker;         // Marker de la location spécifiée
    private Marker mNewLocationMarker;          // Marker de la location ciblée et cliquée
    private LocationRequest mLocationRequest;   // Objet pour requêter les changements de la location

    private Location mLastLocation;             // Contient la dernière location

    private double latitude_current;
    private double longitude_current;

    private LatLng latLng_endroit;
    private double latitude_endroit;
    private double longitude_endroit;

    private String button_calling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        button_calling = "no_button_calling";

        /* Récupère les extra passé par l'activité précédente */
        if (getIntent().getDoubleExtra("latitude_endroit", 0.0) != 0.0) {
            latitude_endroit = getIntent().getDoubleExtra("latitude_endroit", 0.0);
        }
        if (getIntent().getDoubleExtra("longitude_endroit", 0.0) != 0.0) {
            longitude_endroit = getIntent().getDoubleExtra("longitude_endroit", 0.0);
        }
        if (getIntent().getStringExtra("button_calling") != null) {
            button_calling = getIntent().getStringExtra("button_calling");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // MAP_TYPE_NORMAL : vue grisé comme un jeu
        // MAP_TYPE_HYBRID : vue des batiments
        // MAP_TYPE_SATELLITE : vue des batiments

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        if (mSpecLocationMarker != null) {
            mSpecLocationMarker.remove();
        }
        if (mNewLocationMarker != null) {
            mNewLocationMarker.remove();
        }

        // Si le bouton appelant est l'endroit (1er)
        if (button_calling.equals("endroit")) {

            // Créer et place sur la google Map le marker de la position spécifiée (endroit cliqué de la listview)
            if (latitude_endroit != 0.0 && longitude_endroit != 0.0) {
                latLng_endroit = new LatLng(latitude_endroit, longitude_endroit);
                MarkerOptions markerOptions_spec = new MarkerOptions();
                markerOptions_spec.position(latLng_endroit);
                markerOptions_spec.title("Item Endroit " + "latitude : " + latitude_endroit + " | longitude : " + latitude_endroit);
                markerOptions_spec.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                mSpecLocationMarker = mMap.addMarker(markerOptions_spec);
            }

            // on centre la caméra sur l'endroit
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude_endroit, longitude_endroit)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18)); // 11 default
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
        }
        // Si le bouton appelant est l'endroit+position (2eme)
        else if (button_calling.equals("endroit+position")) {

            // Créer et place sur la google Map le marker de la position courante
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Ma Position "+"latitude : "+location.getLatitude()+" | longitude : "+location.getLongitude());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)); // couleur de marqueur rose --> HUE_MAGENTA // couelur de marqueur bleur --> HUE_AZURE
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            // Créer et place sur la google Map le marker de la position spécifiée (endroit cliqué de la listview)
            if (latitude_endroit != 0.0 && longitude_endroit != 0.0) {
                latLng_endroit = new LatLng(latitude_endroit, longitude_endroit);
                MarkerOptions markerOptions_spec = new MarkerOptions();
                markerOptions_spec.position(latLng_endroit);
                markerOptions_spec.title("Item Endroit " + "latitude : " + latitude_endroit + " | longitude : " + latitude_endroit);
                markerOptions_spec.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                mSpecLocationMarker = mMap.addMarker(markerOptions_spec);

                Log.d("L'endroit est à ", String.valueOf(getDistanceBetweenMarkers(latLng, latLng_endroit)*1000)+" de vous");
                Toast.makeText(MapsActivity.this, "L'endroit est à "+ String.valueOf(roundToTwo(getDistanceBetweenMarkers(latLng, latLng_endroit)))+" klm de vous , soit "+String.valueOf(roundToTwo(getDistanceBetweenMarkers(latLng, latLng_endroit))*1000)+" mètres de vous", Toast.LENGTH_SHORT).show();
            }

            // on centre la caméra sur la position de l'user
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18)); // 11 default
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
        }
        // Si la map n'est pas chargé par un bouton d'un item de la listview
        else {

            // Créer et place sur la google Map le marker de la position courante
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Ma Position "+"latitude : "+location.getLatitude()+" | longitude : "+location.getLongitude());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)); // couleur de marqueur rose --> HUE_MAGENTA // couelur de marqueur bleur --> HUE_AZURE
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            // on centre la caméra sur la position
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18)); // 11 default
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
        }

        //stop location updates
        /*if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }*/


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public void onResume() {
        Log.d("onResume", "onResume called");
        super.onResume();
        if (mGoogleApiClient != null &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    // Méthode permettant de calculer la distance en mètre de 2 marker
    public double getDistanceBetweenMarkers(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    // Méthode permettant d'arrondir un double au 10eme ou c100eme près
    private double roundToTwo(double a) {
        return (double) Math.round(a * 100) / 100;
    }


    // Méthode permettant de creer un marker
    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }


    /* Interface GoogleMap.OnMarkerDragListener */
    @Override
    public void onMarkerDragStart(Marker marker) {
        // Called when a marker starts being dragged
        Log.d("Marker", "Started");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // Called repeatedly while a marker is being dragged
        Log.d("Marker", "Dragging");
        LatLng markerLocation = marker.getPosition();
        marker.setSnippet("Latitude "+markerLocation.latitude+ " Longitude "+markerLocation.longitude);
        Log.d("MarkerPosition", markerLocation.toString());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // Called when a marker has finished being dragged
        LatLng markerLocation = marker.getPosition();
        // Mettre à jour le marker en BDD locale
        Toast.makeText(MapsActivity.this, markerLocation.toString(), Toast.LENGTH_LONG).show();
        Log.d("Marker", "finished");
    }

    /* Interface GoogleMap.OnMapLongClickListener */
    @Override
    public void onMapLongClick(LatLng latLng) {

        if (mNewLocationMarker != null) {
            mNewLocationMarker.remove();
        }
        MarkerOptions markerOptions_new = new MarkerOptions();
        markerOptions_new.position(latLng);
        markerOptions_new.title("Nouvelle position");
        markerOptions_new.snippet("Latitude "+latLng.latitude+ " Longitude "+latLng.longitude);
        markerOptions_new.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions_new.draggable(true);
        mNewLocationMarker = mMap.addMarker(markerOptions_new);
        onInfoWindowClick(mNewLocationMarker);

    }

    /* Interface GoogleMap.OnMapClickListener */
    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    /********** Interface GoogleMap.OnInfoWindowClickListener *********/
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("onInfoWindowClick", "onInfoWindowClick called");
        marker.getPosition();

        // enregistrer les coordonnées du marker dans un nouvel objet Endroit en BDD SQLite
        GPSTracker gpsTracker = new GPSTracker(this);
        // Convertir LatLng en adresse
        String[] list = gpsTracker.getLocationAdress(marker.getPosition().latitude, marker.getPosition().longitude);
        // Récupérer la ville de la conversion
        Log.d("list<string> adress", "list<string> adress size"+String.valueOf(list.length));
        for (int i=0;i<list.length;i++) {
            Log.d("list i", "list"+i+"="+list[i]);
        }
        Endroit endroit = new Endroit("MaPosition", list[0],   list[1].substring(0,5),   list[1].substring(5), marker.getPosition().latitude, marker.getPosition().longitude, 0.0, 0.0);
        UseBDD_Endroit useBDD_endroit =  new UseBDD_Endroit(this);
        useBDD_endroit.open();
        useBDD_endroit.insertRecord_Endroit(endroit);

        if (useBDD_endroit.queryEndroit_IsIDEndroitExist(endroit.getId_Endroit())) {
            endroit = useBDD_endroit.updateRecord_EndroitByIDEndroit(endroit, endroit.getId_Endroit());
            Log.d("IsIDEndroitExist", "queryEndroit_IsIDEndroitExist called");
        }
        else {
            endroit = useBDD_endroit.queryEndroit_IDEndroit(endroit.getId_Endroit());
            Log.d("queryEndroit_IDEndroit", "queryEndroit_IDEndroit called");
        }
        Log.d("endroit rec", endroit.toString());
        useBDD_endroit.close();

    }
}

package app.locationfac.maintabbed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.locationfac.R;
import app.locationfac.map.MapsActivity;
import app.locationfac.ressources.localisation.GPSTracker;
import app.locationfac.ressources.localisation.HttpDataHandler;


public class MainTab1_MonEmplacement_Fragment extends Fragment  {

    // Arguments du fragment
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Variables UI
    FloatingActionButton flt;
    Button btnShowCoord;
    EditText edtAddress;
    TextView txtCoord;

    Button btnGetAdd;
    EditText et_lat;
    EditText et_long;
    EditText et_alt;
    EditText et_acc;
    TextView tv_add;

    Button btn_getcoord;
    TextView tv_coord;
    FloatingActionButton btn_flt;

    // Variable users
    double latitude;
    double longitude;
    double accuracy;
    double altitude;
    Geocoder geocoder;


    GPSTracker gps;

    private OnFragmentInteractionListener mListener;

    public MainTab1_MonEmplacement_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainTab1_MonEmplacement_Fragment.
     */
    public static MainTab1_MonEmplacement_Fragment newInstance(String param1, String param2) {
        MainTab1_MonEmplacement_Fragment fragment = new MainTab1_MonEmplacement_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Pour cacher la barre de statut ( heure, etat du reseau etc ... )
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Pour masquer la barre de navigation
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        /* On vérifie si le GPS est activié et on récupère les coordonnées GPS */
        gps = new GPSTracker(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Pour cacher la barre de statut ( heure, etat du reseau etc ... )
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Pour masquer la barre de navigation
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab1_emplacement, container, false);
        // GetCoord of adresse
        btnShowCoord = (Button)rootView.findViewById(R.id.btnShowCoordinates);
        edtAddress = (EditText)rootView.findViewById(R.id.edtAddress);
        txtCoord = (TextView)rootView.findViewById(R.id.txtCoordinates);

        btnShowCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCoordinates().execute(edtAddress.getText().toString().replace(" ","+"));
            }
        });

        // Get Adresse
        btnGetAdd = (Button)rootView.findViewById(R.id.btn_getadd);
        et_lat = (EditText)rootView.findViewById(R.id.et_lat);
        et_long= (EditText)rootView.findViewById(R.id.et_long);
        et_alt = (EditText)rootView.findViewById(R.id.et_alt);
        et_acc= (EditText)rootView.findViewById(R.id.et_acc);
        tv_add = (TextView)rootView.findViewById(R.id.tv_add);


        btnGetAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        // GetCoord of position
        btn_getcoord = (Button)rootView.findViewById(R.id.btn_getcoord);
        tv_coord = (TextView)rootView.findViewById(R.id.tv_coord);

        btn_getcoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On récupere les coordonnées GPS
                // check if GPS enabled
                if(gps.canGetLocation())
                {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    altitude = gps.getAltitude();
                    accuracy = gps.getAccuracy();
                }

                tv_coord.setText(
                        "latitude "+longitude+"\n" +
                                "longitude "+latitude+"\n"
                );

            }
        });

        Button fab = (Button) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MapsActivity.class);
                startActivity(intent);

            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getLocation() {
        try {
            Log.d("lat", String.valueOf(Double.parseDouble(et_lat.getText().toString())));
            Log.d("long", String.valueOf(Double.parseDouble(et_long.getText().toString())));
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(et_lat.getText().toString()), Double.parseDouble(et_long.getText().toString()), 1);
            String cliAddr ="";
            if(addresses.size() > 0)
            {
                for (int i=0;i<addresses.get(0).getMaxAddressLineIndex();i++)
                    cliAddr += addresses.get(0).getAddressLine(i)+"\n";
            }
            Toast.makeText(getContext(), "adresse "+cliAddr, Toast.LENGTH_SHORT).show();
            tv_add.setText(cliAddr);
        }
        catch (IOException io) {
            Log.d("IOEXception", io.toString());
        }
        catch (NumberFormatException nfe) {
            Log.d("NumberFormatException", "double impossible à parser car champs vides");
        }
    }

    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response = http.getHTTPData(url);
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);
                Log.d("JsonObject", jsonObject.toString());
                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                txtCoord.setText(String.format("Coordinates : %s / %s ",lat,lng));

                if(dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

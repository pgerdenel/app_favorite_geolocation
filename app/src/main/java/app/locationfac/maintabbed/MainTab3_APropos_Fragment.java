package app.locationfac.maintabbed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import app.locationfac.R;
import app.locationfac.endroit.Endroit;
import app.locationfac.ressources.stockage.local.UseBDD_Endroit;
import app.locationfac.ressources.stockage.remote.okhttp.USE_OKHTTP3_Client;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainTab3_APropos_Fragment extends Fragment {
    // paramètres dy fragment
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Variables UI
    private Button bt_twitter;
    private Button bt_fb;
    private Button bt_email;
    private Button bt_lienweb;
    private Button bt_rec_bdd2;

    Gson gson;
    USE_OKHTTP3_Client USE_okhttp3_client;
    Integer myInteger_resp_tmp;
    private Context mContext;
    UseBDD_Endroit useBDD_endroit;

    private OnFragmentInteractionListener mListener;

    public MainTab3_APropos_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainTab3_APropos_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainTab3_APropos_Fragment newInstance(String param1, String param2) {
        MainTab3_APropos_Fragment fragment = new MainTab3_APropos_Fragment();
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
        mContext = getContext();
        USE_okhttp3_client = new USE_OKHTTP3_Client(getActivity().getApplicationContext());
        useBDD_endroit = new UseBDD_Endroit(mContext);
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
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab3_apropos, container, false);
        bt_twitter = (Button) rootView.findViewById(R.id.bt_twitter);
        bt_fb = (Button) rootView.findViewById(R.id.bt_fb);
        bt_lienweb = (Button) rootView.findViewById(R.id.bt_lienweb);
        bt_email = (Button) rootView.findViewById(R.id.bt_email);
        bt_rec_bdd2 = (Button) rootView.findViewById(R.id.bt_rec_bdd2);

        bt_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/USERID_OR_PROFILENAME"));
                }
                startActivity(intent);
            }
        });

        bt_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/<id_here>")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/<user_name_here>")));
                }
            }
        });

        bt_lienweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        bt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "some@email.address" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        bt_rec_bdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_localdb_to_remotedb();
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

    // Méthode permettant de stocker en BDD Local
    private void store_localdb_to_remotelocaldb() {
         /*
         *	Call permettant de svg la bdd local par un call OkHttp3 par HTTP
         *  Le Script encode les entrées de la bdd en json avec GSON
         *  On reçoie bien un Integer en retour pour nous confirmer ou non l'enregistrement
         */
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        gson = new Gson();
        //Endroit endroit = new Endroit("nom_test", "adresse_test", "cp_test", "ville_test", 0.0, 0.0, 0.0, 0.0);
        //String myJson = gson.toJson(endroit);
        useBDD_endroit.open();
        List<Endroit> list_endroit = useBDD_endroit.queryAllEndroit();
        useBDD_endroit.close();
        String json = gson.toJson(list_endroit);
        Log.d("myJson", "myJson= "+json);
        final Request myGetRequest = new Request.Builder()
                .url("https://192.168.1.17/locationfac/store_remote.php")
                .post(RequestBody.create(JSON_TYPE, json))
                .build();

        OkHttpClient okHttpClient = USE_okhttp3_client.getUnsafeOkHttpsClient();

        okHttpClient.newCall(myGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(getContext(), "backup failed", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("error call okhttp", e.toString());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("okhttp response :", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("response", "response is sucessfull=");
                    // On parse un Integer de la réponse
                    myInteger_resp_tmp = Integer.valueOf(response.body().string());
                    Log.d("myInteger_tmp", String.valueOf(myInteger_resp_tmp));
                }
                // Lance dans le thread UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // On public le résultat de la requête après traitement dans l'interface UI
                            if (myInteger_resp_tmp ==0) {
                                Toast.makeText(getContext(), "backup success", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getContext(), "backup failed", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("error call okhttp token", e.toString());
                            Toast.makeText(getContext(), "backup failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            public void onResponseTest(Response response) {
                Log.d("##############", "############ START RESPONSE BODY TEST DISPLAY ATTRIBUTES ###############################################");
                // On affiche tous les attributs du body de la response
                try {
                    Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY_CAST=" + Integer.valueOf(response.body().string().toString()));    // affiche la valeur de l'integer récupéré
                    //Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().string());    // affiche la valeur de l'integer récupéré
                } catch (IOException io) {
                    Log.d("ioexception", "iocexception" + io.toString());
                }
                Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().toString());
                Log.d("RESPONSE-BODY-CLASS--> ", "RESPONSE-BODY-CLASS=" + response.body().getClass().toString());
                Log.d("R-BODY-contentType-> ", "RESPONSE-BODY-contentType=" + response.body().contentType().toString());
                Log.d("R-BODY-contentLength-> ", "RESPONSE-BODY-contentLength=" + String.valueOf(response.body().contentLength()));
                Log.d("R-BODY-hashCode--> ", "RESPONSE-BODY-hashCode=" + String.valueOf(response.body().hashCode()));
                Log.d("##############", "############ END RESPONSE BODY TEST DISPLAY ATTRIBUTES ###############################################");

                Log.d("##############", "############ START RESPONSE TEST DISPLAY ATTRIBUTES ###############################################");
                // On affiche tous les attributs de la response
                Log.d("RESPONSE--> ", "RESPONSE=" + response.toString());
                Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().toString());
                Log.d("RESPONSE-HEADER--> ", "RESPONSE-HEADER=" + response.headers().toString());
                Log.d("RESPONSE-MESSAGE--> ", "RESPONSE-MESSAGE=" + response.message().toString());
                Log.d("RESP_CACHE_CONTROL--> ", "RESP_CACHE_CONTROL=" + response.cacheControl().toString());
                // Log.d("RESP_CACHE_RESPONSE--> ", "RESP_CACHE_RESPONSE="+response.cacheResponse().toString()); NULL ?
                Log.d("RESP_CHALLENGE--> ", "RESP_CHALLENGE=" + response.challenges().toString());
                // Log.d("RESP_HANDSHAKE--> ", "RESP_HANDSHAKE="+response.handshake().toString()); NULL ?
                Log.d("RESP_NETWRK-RESPE--> ", "RESP_NETWRK-RESPE=" + response.networkResponse().toString());
                // Log.d("RESP_PRIOR_RESP--> ", "RESP_PRIOR_RESP="+response.priorResponse().toString()); NULL ?
                Log.d("RESP_PROTOCOLE--> ", "RESP_PROTOCOLE=" + response.protocol().toString());
                Log.d("RESP_REQUEST--> ", "RESP_REQUEST=" + response.request().toString());
                Log.d("RESP_CLASS_RESPONSE--> ", "RESP_CLASS_RESPONSE=" + response.getClass().toString());
                Log.d("RESP_CODE--> ", "ESP_CODE=" + String.valueOf(response.code()));
                //Log.d("RESP_receivAtMillis--> ", "receivedResponseAtMillis=" + String.valueOf(response.receivedResponseAtMillis()));
                //Log.d("RESP_sentAtMillis--> ", "sentRequestAtMillis=" + String.valueOf(response.sentRequestAtMillis()));
                Log.d("##############", "############ END RESPONSE TEST DISPLAY ATTRIBUTES ###############################################");
            }
        });
    }

    // Méthode permettant de stocker en BDD distante
    private void store_localdb_to_remotedb() {
         /*
         *	Call permettant de svg la bdd local par un call OkHttp3 par HTTP
         *  Le Script encode les entrées de la bdd en json avec GSON
         *  On reçoie bien un Integer en retour pour nous confirmer ou non l'enregistrement
         */
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        gson = new Gson();
        //Endroit endroit = new Endroit("nom_test", "adresse_test", "cp_test", "ville_test", 0.0, 0.0, 0.0, 0.0);
        //String myJson = gson.toJson(endroit);
        useBDD_endroit.open();
        List<Endroit> list_endroit = useBDD_endroit.queryAllEndroit();
        useBDD_endroit.close();
        String json = gson.toJson(list_endroit);
        Log.d("myJson", "myJson= "+json);
        final Request myGetRequest = new Request.Builder()
                .url("https://ltslafrxpx.cluster023.hosting.ovh.net/test/store_remote.php")
                .post(RequestBody.create(JSON_TYPE, json))
                .build();

        OkHttpClient okHttpClient = USE_okhttp3_client.getUnsafeOkHttpsClient();

        okHttpClient.newCall(myGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(getContext(), "backup failed", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("error call okhttp", e.toString());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("okhttp response :", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("response", "response is sucessfull=");
                    // On parse un Integer de la réponse
                    myInteger_resp_tmp = Integer.valueOf(response.body().string());

                    Log.d("myInteger_tmp", String.valueOf(myInteger_resp_tmp));
                    //onResponseTest(response);
                }
                // Run view-related code back on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // On public le résultat de la requête après traitement dans l'interface UI
                            if (myInteger_resp_tmp ==0) {
                                Toast.makeText(getContext(), "backup success", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getContext(), "backup failed", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("error call okhttp token", e.toString());
                            Toast.makeText(getContext(), "backup failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            public void onResponseTest(Response response) {
                Log.d("##############", "############ START RESPONSE BODY TEST DISPLAY ATTRIBUTES ###############################################");
                // On affiche tous les attributs du body de la response
                try {
                    Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY_CAST=" + Integer.valueOf(response.body().string().toString()));    // affiche la valeur de l'integer récupéré
                    //Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().string());    // affiche la valeur de l'integer récupéré
                } catch (IOException io) {
                    Log.d("ioexception", "iocexception" + io.toString());
                }
                Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().toString());
                Log.d("RESPONSE-BODY-CLASS--> ", "RESPONSE-BODY-CLASS=" + response.body().getClass().toString());
                Log.d("R-BODY-contentType-> ", "RESPONSE-BODY-contentType=" + response.body().contentType().toString());
                Log.d("R-BODY-contentLength-> ", "RESPONSE-BODY-contentLength=" + String.valueOf(response.body().contentLength()));
                Log.d("R-BODY-hashCode--> ", "RESPONSE-BODY-hashCode=" + String.valueOf(response.body().hashCode()));
                Log.d("##############", "############ END RESPONSE BODY TEST DISPLAY ATTRIBUTES ###############################################");

                Log.d("##############", "############ START RESPONSE TEST DISPLAY ATTRIBUTES ###############################################");
                // On affiche tous les attributs de la response
                Log.d("RESPONSE--> ", "RESPONSE=" + response.toString());
                Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().toString());
                Log.d("RESPONSE-HEADER--> ", "RESPONSE-HEADER=" + response.headers().toString());
                Log.d("RESPONSE-MESSAGE--> ", "RESPONSE-MESSAGE=" + response.message().toString());
                Log.d("RESP_CACHE_CONTROL--> ", "RESP_CACHE_CONTROL=" + response.cacheControl().toString());
                // Log.d("RESP_CACHE_RESPONSE--> ", "RESP_CACHE_RESPONSE="+response.cacheResponse().toString()); NULL ?
                Log.d("RESP_CHALLENGE--> ", "RESP_CHALLENGE=" + response.challenges().toString());
                // Log.d("RESP_HANDSHAKE--> ", "RESP_HANDSHAKE="+response.handshake().toString()); NULL ?
                Log.d("RESP_NETWRK-RESPE--> ", "RESP_NETWRK-RESPE=" + response.networkResponse().toString());
                // Log.d("RESP_PRIOR_RESP--> ", "RESP_PRIOR_RESP="+response.priorResponse().toString()); NULL ?
                Log.d("RESP_PROTOCOLE--> ", "RESP_PROTOCOLE=" + response.protocol().toString());
                Log.d("RESP_REQUEST--> ", "RESP_REQUEST=" + response.request().toString());
                Log.d("RESP_CLASS_RESPONSE--> ", "RESP_CLASS_RESPONSE=" + response.getClass().toString());
                Log.d("RESP_CODE--> ", "ESP_CODE=" + String.valueOf(response.code()));
                //Log.d("RESP_receivAtMillis--> ", "receivedResponseAtMillis=" + String.valueOf(response.receivedResponseAtMillis()));
                //Log.d("RESP_sentAtMillis--> ", "sentRequestAtMillis=" + String.valueOf(response.sentRequestAtMillis()));
                Log.d("##############", "############ END RESPONSE TEST DISPLAY ATTRIBUTES ###############################################");
            }
        });
    }
}

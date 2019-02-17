package app.locationfac.maintabbed;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.List;

import app.locationfac.R;
import app.locationfac.endroit.Endroit;
import app.locationfac.endroit.EndroitAdapter;
import app.locationfac.endroit.EndroitAdapterListener;
import app.locationfac.ressources.stockage.local.UseBDD_Endroit;

public class MainTab2_Localisations_Fragment extends Fragment implements EndroitAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    // Paramètres du fragment
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // ArrayList de la ListView
    private List<Endroit> list_Endroit;

    FloatingActionButton btn_flt;

    // Composants de la vue
    private SwipeRefreshLayout mySwipeRefreshLayout_Endroit;
    private OnFragmentInteractionListener mListener_Endroit;
    private EndroitAdapter endroit_adapter;
    private ListView list;

    UseBDD_Endroit useBDD_endroit;

    public MainTab2_Localisations_Fragment() {
       // empty constructor required
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainTab2_Localisations_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainTab2_Localisations_Fragment newInstance(String param1, String param2) {
        MainTab2_Localisations_Fragment fragment = new MainTab2_Localisations_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        useBDD_endroit = new UseBDD_Endroit(getContext());

        useBDD_endroit.open();
        list_Endroit = useBDD_endroit.queryAllEndroit();
        useBDD_endroit.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.endroit_main, container, false);

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

        mySwipeRefreshLayout_Endroit = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_endroit);   // on recupere le swiperefreshlayout

        list = (ListView) view.findViewById(R.id.ListView01);       // on recupere la list

        // on instancie notre adapter avec la list de LL récupérer de la BDD
        Log.d("listendroit size", "taille de la list="+list_Endroit.size());
        endroit_adapter = new EndroitAdapter(getActivity(), list_Endroit, MainTab2_Localisations_Fragment.this); // on declare un nouvel adapter sur la list

        endroit_adapter.addListener(this);  // on declare un listenner sur les item de la list
        //mySwipeRefreshLayout_LastLove.setEnabled(false); // a désactiver pour que les fct° de rafraichissement fonctionne // on désative le rafraishisement et on l'active dés que le 1er item de la list est visible
        list.setAdapter(endroit_adapter);   // on applique un adapter sur la liste

        mySwipeRefreshLayout_Endroit.setOnRefreshListener(this);

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener_Endroit != null) {
            mListener_Endroit.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClickButtonImage(Endroit item, int position) {
    }
    @Override
    public void onClickItemLayout(Endroit item, int position) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener_Endroit = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener_Endroit = null;
    }

    /* Fonction qui enregistre les endroits en db distante */
    public void recInremoteDb() {

    }

    /* INTERFACE SwipeRefreshLayout.OnRefreshListener */
    @Override
    public void onRefresh() {

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                useBDD_endroit.open();
                List<Endroit> list_endroit_tmp = useBDD_endroit.queryAllEndroit();
                endroit_adapter = new EndroitAdapter(getContext(), list_endroit_tmp, MainTab2_Localisations_Fragment.this);
                list.setAdapter(endroit_adapter);
                //endroit_adapter.notifyDataSetChanged();
                mySwipeRefreshLayout_Endroit.setRefreshing(false);
                //Toast.makeText(getContext(), "Nouveaux Endroits disponibles", Toast.LENGTH_LONG).show();
            }
        }, 1000);

    }

    /**************** asynctask rec in db***********/

    private class RecInDbAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            // code where data is processing
            recInremoteDb();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "Vos endroits ont été sauvegardés en BDD distante", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle user action
                }
            });
            snackbar.show();
        }
    }
}

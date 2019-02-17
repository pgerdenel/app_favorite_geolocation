package app.locationfac.endroit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.locationfac.R;
import app.locationfac.maintabbed.MainTab2_Localisations_Fragment;
import app.locationfac.map.MapsActivity;
import app.locationfac.ressources.stockage.local.UseBDD_Endroit;

/**
 * Created by HcZ on 20/03/2017.
 * Permet la mise en forme personalisé des items de la ListView
 */

public class EndroitAdapter extends BaseAdapter {

    private List<Endroit> mListEndroit;
    private Context mContext;
    private LayoutInflater mInflater;
    private MainTab2_Localisations_Fragment mainTab2_localisations_fragment;
    private TextView tv_idEndroit;
    private TextView tv_nom;
    private TextView tv_adresse;
    private TextView tv_codepostal;
    private TextView tv_ville;
    private TextView tv_latitude;
    private TextView tv_longitude;
    private TextView tv_altitude;
    private TextView tv_accuracy;
    private Button bt_del;
    private Button bt_map;
    private Button bt_map_pos;

    // Shared Preferences
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public EndroitAdapter(Context context, List<Endroit> aListEndroit, MainTab2_Localisations_Fragment mainTab2_localisations_frag) {
        mContext = context;
        mListEndroit = aListEndroit;
        mInflater = LayoutInflater.from(mContext);
        mainTab2_localisations_fragment = mainTab2_localisations_frag;
    }

    public int getCount() {
        return mListEndroit.size();
    }

    public Object getItem(int position) {
        return mListEndroit.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {

        // On initialise les composants des preférences partagées
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = preferences.edit();

        final RelativeLayout layoutItem;
        if (convertView == null) {
        layoutItem = (RelativeLayout) mInflater.inflate(R.layout.fragment_tab2_localisation, parent, false);
        } else {
            layoutItem = (RelativeLayout) convertView;
        }

        // On récupère une référence sur les composants de la vue

        tv_idEndroit = (TextView)layoutItem.findViewById(R.id.tv_id_endroit);
        tv_nom = (TextView)layoutItem.findViewById(R.id.tv_nom);
        tv_adresse = (TextView)layoutItem.findViewById(R.id.tv_adresse);
        tv_codepostal = (TextView)layoutItem.findViewById(R.id.tv_codepostal);
        tv_ville = (TextView)layoutItem.findViewById(R.id.tv_ville);
        tv_latitude = (TextView)layoutItem.findViewById(R.id.tv_latitude);
        tv_longitude = (TextView)layoutItem.findViewById(R.id.tv_longitude);
        tv_altitude = (TextView)layoutItem.findViewById(R.id.tv_altitude);
        tv_accuracy = (TextView)layoutItem.findViewById(R.id.tv_accuracy);
        bt_del = (Button) layoutItem.findViewById(R.id.bt_supprimer);
        bt_map = (Button) layoutItem.findViewById(R.id.bt_afficher);
        bt_map_pos = (Button) layoutItem.findViewById(R.id.bt_afficheretposuser);

        // On assigne les valeurs aux champs
        tv_idEndroit.setText(mListEndroit.get(position).getId_Endroit());
        tv_nom.setText(mListEndroit.get(position).getNom());
        tv_adresse.setText(mListEndroit.get(position).getAdresse());
        tv_codepostal.setText(mListEndroit.get(position).getCodepostal());
        tv_ville.setText(mListEndroit.get(position).getVille());
        tv_latitude.setText(String.valueOf(mListEndroit.get(position).latitude));
        tv_longitude.setText(String.valueOf(mListEndroit.get(position).longitude));
        tv_altitude.setText(String.valueOf(mListEndroit.get(position).altitude));
        tv_accuracy.setText(String.valueOf(mListEndroit.get(position).accuracy));

        // Listenner sur clic d'un item (d'un Endroit) de la list
        bt_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (mListEndroit.size()>0) {
                    mListEndroit.remove(mListEndroit.get(position));
                }*/
                UseBDD_Endroit useBDD_endroit = new UseBDD_Endroit(mContext);
                useBDD_endroit.open();
                useBDD_endroit.deleteRecord_Endroit_WithIDEndroit(mListEndroit.get(position).getId_Endroit());
                useBDD_endroit.close();
                mainTab2_localisations_fragment.onRefresh();
            }
        });

        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @TODO à implémenter
                // On récupère l'objet LastLove en BDD SQLite correspondant à l'ID
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("button_calling", "endroit");
                intent.putExtra("latitude_endroit", mListEndroit.get(position).getLatitude());
                intent.putExtra("longitude_endroit", mListEndroit.get(position).getLongitude());
                intent.putExtra("altitude_endroit", mListEndroit.get(position).getAltitude());
                intent.putExtra("accuracy_endroit", mListEndroit.get(position).getAccuracy());
                intent.putExtra("activitycalling", "endroit_adapter");
                v.getContext().startActivity(intent);
            }
        });

        bt_map_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("button_calling", "endroit+position");
                intent.putExtra("latitude_endroit", mListEndroit.get(position).getLatitude());
                intent.putExtra("longitude_endroit", mListEndroit.get(position).getLongitude());
                intent.putExtra("altitude_endroit", mListEndroit.get(position).getAltitude());
                intent.putExtra("accuracy_endroit", mListEndroit.get(position).getAccuracy());
                intent.putExtra("activitycalling", "endroit_adapter");
                v.getContext().startActivity(intent);
            }
        });

        // Récupérer le layout afin de faire un onClick dessus
        layoutItem.setTag(position);
        layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mListEndroit.get(position).getId_Endroit();
                LayoutInflater factory = LayoutInflater.from(mContext);
                final View alertDialogView = factory.inflate(R.layout.alert_dialog_edit_endroit, null);
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setView(alertDialogView);
                adb.setTitle("Changer le nom de l'endroit");
                adb.setNegativeButton("Valider", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) alertDialogView.findViewById(R.id.et_nom);
                        ;
                        Endroit endroit = new Endroit(editText.getText().toString(), mListEndroit.get(position).getAdresse(), mListEndroit.get(position).getCodepostal(), mListEndroit.get(position).getVille(),
                        mListEndroit.get(position).getLatitude(), mListEndroit.get(position).getLongitude(), mListEndroit.get(position).getAltitude(), mListEndroit.get(position).getAccuracy());
                        UseBDD_Endroit useBDD_endroit = new UseBDD_Endroit(mContext);
                        useBDD_endroit.open();
                        useBDD_endroit.updateRecord_EndroitByIDEndroit(endroit, mListEndroit.get(position).getId_Endroit());
                        useBDD_endroit.close();
                    }
                });
                adb.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                adb.show();
            }
        });

        return layoutItem;
    }

    private ArrayList<EndroitAdapterListener> mListListener = new ArrayList<EndroitAdapterListener>();
    public void addListener(EndroitAdapterListener aListener) {
        mListListener.add(aListener);
    }

    private void sendListener(Endroit item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickButtonImage(item, position);
        }
    }

    private void sendListenerLayout(Endroit item, int position) {
        for(int i = mListListener.size()-1; i >= 0; i--) {
            mListListener.get(i).onClickItemLayout(item, position);
        }
    }

    public String cutShortMessage(String message) {
        int taille = message.length();
        taille = taille/3;
        return message.substring(taille);
    }

    public int getNumericValueOfString(String s) {
        int value=0;
        char[] charArray = s.toCharArray();
        for (int i=0;i<s.length();i++) {
            value += Character.getNumericValue(charArray[i]);
        }
        return value;
    }
}
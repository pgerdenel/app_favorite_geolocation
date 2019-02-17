package app.locationfac.maintabbed;

import android.net.Uri;

/**
 * Created by HKS on 12/10/2016.
 * Cet interface doit être implémenté par les activity qui contiennent ces fragments
 * Permet une interaction entre le fragment et l'activity pour communiquer
 * Permet de communiquer potentiellement avec les autres fragments
 */

public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
}

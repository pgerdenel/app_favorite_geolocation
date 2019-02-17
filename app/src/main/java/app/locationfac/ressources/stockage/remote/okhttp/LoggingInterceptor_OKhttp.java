package app.locationfac.ressources.stockage.remote.okhttp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HKS on 25/03/2017.
 * Class qui permet de créer et caractériser des objets
 */

class LoggingInterceptor_OKhttp implements Interceptor {
    //Code pasted from okHttp webSite itself
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();

        Log.i("##############", "############ START RESPONSE BODY TEST DISPLAY ATTRIBUTES ###############################################");

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        // On affiche tous les attributs du body de la response
        // On affiche pas la réponse car on peut appeler response.string() à juste une seule fois
        Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().toString());
        Log.d("RESPONSE-BODY-CLASS--> ", "RESPONSE-BODY-CLASS=" + response.body().getClass().toString());
        Log.d("R-BODY-contentType-> ", "RESPONSE-BODY-contentType=" + response.body().contentType().toString());
        Log.d("R-BODY-contentLength-> ", "RESPONSE-BODY-contentLength=" + String.valueOf(response.body().contentLength()));
        Log.d("R-BODY-hashCode--> ", "RESPONSE-BODY-hashCode=" + String.valueOf(response.body().hashCode()));
        Log.i("##############", "############ END RESPONSE BODY TEST DISPLAY ATTRIBUTES ###############################################");

        Log.i("##############", "############ START RESPONSE TEST DISPLAY ATTRIBUTES ###############################################");
        // On affiche tous les attributs de la response
        Log.d("RESPONSE--> ", "Sending request " + response.toString());
        Log.d("RESPONSE-BODY--> ", "RESPONSE-BODY=" + response.body().toString());
        Log.d("RESPONSE-HEADER--> ", String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers())); // response.headers().toString() donne la même réponse
        Log.d("RSPSE-HEADER+KEY/VAL", response.headers().names().toString());
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
        Log.d("RESP_receivAtMillis--> ", "receivedResponseAtMillis=" + String.valueOf(response.receivedResponseAtMillis()));
        Log.d("RESP_sentAtMillis--> ", "sentRequestAtMillis=" + String.valueOf(response.sentRequestAtMillis()));
        Log.i("##############", "############ END RESPONSE TEST DISPLAY ATTRIBUTES ###############################################");
        return response;
    }

    public Interceptor getInterceptor_okhttp() {
        return new LoggingInterceptor_OKhttp();
    }
}

package app.locationfac.ressources.stockage.remote.okhttp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Created by HKS on 01/11/2016.
 * Class permettant de caractériser et créer ...
 */
/*
Class qui permet de créer et caractériser des Interceptor permettantde zipper la requete
Provoque un null pointer sur l'objet récupéré
*/

final class GzipRequestInterceptor_OKhttp implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            Log.i("GZIP Interceptor ", "intercept() called");
            return chain.proceed(originalRequest);

        }

        Request compressedRequest = originalRequest.newBuilder()
                // On définti le gzip comme MimeType
                .header("Content-Encoding", "gzip")
                // On définti comme contenu le corps (RequestBody) initial compressé
                .method(originalRequest.method(), gzip(originalRequest.body()))
                .build();
        return chain.proceed(compressedRequest);
    }

    private RequestBody gzip(final RequestBody body) {
        Log.i("GZIP Interceptor gzip()", "gzip() called");
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // We don't know the compressed length in advance!
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}

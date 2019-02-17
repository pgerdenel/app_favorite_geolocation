package app.locationfac.ressources.stockage.remote.okhttp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by HKS on 25/03/2017.
 * Class qui permet de créer et caractériser un Objet Use_OKHTTP3_Client pour créer des clients OKHTTP différent
 * getUnsafeOkHttpsClient() renvoie un client okhttp qui support le https mais qui fait confiance à tous les certificats
 * @TODO implémenter la seule prise ne charge de mon certificat locale
 * @TODO implémenter la prise en charge avec un certificat signé
 */

public class USE_OKHTTP3_Client {

    static Context mContext;

    public USE_OKHTTP3_Client(Context context) {
        mContext = context;
    }

    // Renvoie un client Okhttp pour du https avec compression GZIP
    public static OkHttpClient getUnsafeOkHttpsClient_ZIP() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //Assigning a CacheDirectory
        File myCacheDir = new File(mContext.getCacheDir(), "OkHttpCache");
        //you should create it...
        int cacheSize = 1024 * 1024;
        Cache cacheDir = new Cache(myCacheDir, cacheSize);
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            okHttpClient = builder.build();
            okHttpClient = builder
                    .cache(cacheDir)
                    .addInterceptor(new LoggingInterceptor_OKhttp())
                    .addInterceptor(new GzipRequestInterceptor_OKhttp())
                    .build();

        } catch (KeyManagementException kme) {
            Log.d("getUnsafeOkHttpsClient", "getUnsafeOkHttpsClient error KeyManagementException=" + kme.toString());
        } catch (NoSuchAlgorithmException nsae) {
            Log.d("getUnsafeOkHttpsClient", "getUnsafeOkHttpsClient error NoSuchAlgorithmException=" + nsae.toString());
        }

        return okHttpClient;
    }

    // Renvoie un client Okhttp pour du https sans compression
    public static OkHttpClient getUnsafeOkHttpsClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //Assigning a CacheDirectory
        File myCacheDir = new File(mContext.getCacheDir(), "OkHttpCache");
        //you should create it...
        int cacheSize = 1024 * 1024;
        Cache cacheDir = new Cache(myCacheDir, cacheSize);
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            okHttpClient = builder.build();
            okHttpClient = builder
                    .cache(cacheDir)
                    .addInterceptor(new LoggingInterceptor_OKhttp())
                    //.addInterceptor(new GzipRequestInterceptor_OKhttp())
                    .build();

        } catch (KeyManagementException kme) {
            Log.d("getUnsafeOkHttpsClient", "getUnsafeOkHttpsClient error KeyManagementException=" + kme.toString());
        } catch (NoSuchAlgorithmException nsae) {
            Log.d("getUnsafeOkHttpsClient", "getUnsafeOkHttpsClient error NoSuchAlgorithmException=" + nsae.toString());
        }

        return okHttpClient;
    }

}

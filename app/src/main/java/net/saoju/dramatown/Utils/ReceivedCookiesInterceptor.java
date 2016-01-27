package net.saoju.dramatown.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {

    private SharedPreferences preferences;

    public ReceivedCookiesInterceptor(Context context) {
        preferences = context.getSharedPreferences("cookies", 0);
    }

    public SharedPreferences getPreferences(){
        return preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            getPreferences().edit()
                    .putStringSet("cookies", cookies)
                    .commit();
        }

        return originalResponse;
    }
}

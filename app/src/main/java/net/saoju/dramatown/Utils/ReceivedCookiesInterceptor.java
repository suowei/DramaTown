package net.saoju.dramatown.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

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
            SharedPreferences.Editor editor = getPreferences().edit();
            for (String header : originalResponse.headers("Set-Cookie")) {
                String[] cookie = header.split(";", 2);
                String[] value = cookie[0].split("=", 2);
                editor.putString(value[0], value[1]);
            }
            editor.commit();
        }
        return originalResponse;
    }
}

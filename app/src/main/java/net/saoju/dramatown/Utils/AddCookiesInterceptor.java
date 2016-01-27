package net.saoju.dramatown.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    private SharedPreferences preferences;

    public AddCookiesInterceptor(Context context) {
        preferences = context.getSharedPreferences("cookies", 0);
    }

    public SharedPreferences getPreferences(){
        return preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) getPreferences().getStringSet("cookies", new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}

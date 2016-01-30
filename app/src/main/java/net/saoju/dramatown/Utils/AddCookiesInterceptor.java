package net.saoju.dramatown.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Map;

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
        StringBuilder cookie = new StringBuilder("");
        Map<String, ?> all = getPreferences().getAll();
        for(Map.Entry<String, ?> entry : all.entrySet()){
            if (!cookie.equals("")) {
                cookie.append("; ");
            }
            cookie.append(entry.getKey());
            cookie.append("=");
            cookie.append(entry.getValue());
        }
        if (!cookie.equals("")) {
            builder.addHeader("Cookie", cookie.toString());
        }
        return chain.proceed(builder.build());
    }
}

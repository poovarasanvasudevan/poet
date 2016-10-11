package in.shpt.app.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import in.shpt.app.networking.APIProvider;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

/**
 * Created by poovarasanv on 7/10/16.
 */

public class SHPTBase extends Application {

    static SHPTBase shptBase;
    static APIProvider apiProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                Log.i("Error", e.getMessage());
            }
        });
    }

    public static SHPTBase getInstance() {
        if (shptBase == null) {
            shptBase = new SHPTBase();
        }

        return shptBase;
    }


    public APIProvider getApiProvider() {
        if (apiProvider == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.1.104:8080/")
                    .build();

            apiProvider = retrofit.create(APIProvider.class);

        }

        return apiProvider;
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

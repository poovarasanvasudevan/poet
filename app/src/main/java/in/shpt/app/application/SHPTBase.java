package in.shpt.app.application;

import android.app.Application;

import in.shpt.app.networking.APIProvider;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by poovarasanv on 7/10/16.
 */

public class SHPTBase extends Application {

    static SHPTBase shptBase;
    static APIProvider apiProvider;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static SHPTBase getInstance() {
        if (shptBase != null) {
            shptBase = new SHPTBase();
        }

        return shptBase;
    }

    public APIProvider getApiProvider() {
        if (apiProvider != null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("")
                    .build();

            apiProvider = retrofit.create(APIProvider.class);

        }

        return apiProvider;
    }
}

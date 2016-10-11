package in.shpt.app.networking;


import in.shpt.app.config.URLConfig;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by poovarasanv on 7/10/16.
 */

public interface APIProvider {

    @GET("index.php?route=api/mobile/banner")
    Observable<ResponseBody> getBanner();

    @GET("index.php?route=api/mobile/categories")
    Observable<ResponseBody> getCategories();


    @GET("index.php?route=api/mobile/popular")
    Observable<ResponseBody> getPopular(@Query("limit") int limit);
}

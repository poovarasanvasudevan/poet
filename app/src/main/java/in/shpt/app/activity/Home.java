package in.shpt.app.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.minimize.android.rxrecycleradapter.RxDataSource;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.shpt.app.R;
import in.shpt.app.adapter.BannerAdapter;
import in.shpt.app.application.SHPTBase;
import in.shpt.app.config.URLConfig;
import in.shpt.app.databinding.ActivityHomeBinding;
import in.shpt.app.databinding.ProductCardBinding;
import in.shpt.app.event.ConnectionChangeEvent;
import in.shpt.app.models.Banner;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Home extends AppCompatActivity {

    private BannerAdapter adapterViewPager;
    ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        setSupportActionBar(activityHomeBinding.toolbar);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        activityHomeBinding.popularProducts.setLayoutManager(llm);


        activityHomeBinding.searchButton.setImageDrawable(new IconicsDrawable(this, Ionicons.Icon.ion_ios_star).sizeDp(24).color(Color.DKGRAY));

        activityHomeBinding.navigationView.setNavigationItemSelectedListener(item -> {
            activityHomeBinding.drawer.closeDrawers();
            return false;
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityHomeBinding.drawer, activityHomeBinding.toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        activityHomeBinding.drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        if (SHPTBase.getInstance().isOnline(getApplicationContext())) {
            doTask();
        } else {
            activityHomeBinding.networkProblemLayout.setVisibility(View.VISIBLE);
            activityHomeBinding.contentLayout.setVisibility(View.GONE);
        }
    }

    public Drawable getIcon(String cat) {
        Drawable drawable = null;
        switch (cat) {
            case "BOOKS": {
                drawable = new IconicsDrawable(getApplicationContext(),Ionicons.Icon.ion_ios_book_outline);
                break;
            }
            case "AUDIO": {
                drawable = new IconicsDrawable(getApplicationContext(),Ionicons.Icon.ion_ios_musical_notes);
                break;
            }

            case "VIDEO": {
                drawable = new IconicsDrawable(getApplicationContext(),Ionicons.Icon.ion_ios_videocam_outline);
                break;
            }

            case "OTHERS": {
                drawable = new IconicsDrawable(getApplicationContext(),Ionicons.Icon.ion_quote);
                break;
            }

        }

        return drawable;
    }

    public void doTask() {
        SHPTBase.getInstance()
                .getApiProvider()
                .getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody.string());
                        Menu menu1 = activityHomeBinding.navigationView.getMenu();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //SubMenu parent = menu1.addSubMenu(jsonArray.getJSONObject(i).optString("name"));

                            //MenuItem parent = menu1.add(jsonArray.getJSONObject(i).optString("name"));
                            JSONArray children = jsonArray.getJSONObject(i).optJSONArray("children");
                            for (int j = 0; j < children.length(); j++) {
                                SubMenu child1 = menu1.addSubMenu(jsonArray.getJSONObject(i).optString("name") + " BY " + children.optJSONObject(j).optString("name"));
                                //MenuItem children_1 = parent.getSubMenu().add(children.optJSONObject(j).optString("name"));
                                JSONArray children2 = children.optJSONObject(j).optJSONArray("children");
                                for (int k = 0; k < children2.length(); k++) {
                                    Log.i("Item", children2.optJSONObject(k).optString("name"));
                                    // MenuItem children_2 = children_1.getSubMenu().add(children2.optJSONObject(k).optString("name"));
                                    child1.add(children2.optJSONObject(k).optString("name")).setIcon(getIcon(jsonArray.getJSONObject(i).optString("name")));
                                }
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


        SHPTBase.getInstance()
                .getApiProvider()
                .getBanner()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody.string());
                        List<Banner> banners = new ArrayList<Banner>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Banner banner = new Banner(
                                    jsonArray.getJSONObject(i).optString("title"),
                                    jsonArray.getJSONObject(i).optString("link").substring(jsonArray.getJSONObject(i).optString("link").indexOf("product_id"), jsonArray.getJSONObject(i).optString("link").length()),
                                    jsonArray.getJSONObject(i).optString("image").replace("localhost", "192.168.1.104"),
                                    Html.fromHtml(jsonArray.getJSONObject(i).optJSONObject("product").optString("description")).toString(),
                                    jsonArray.getJSONObject(i).optJSONObject("product").optString("author"),
                                    jsonArray.getJSONObject(i).optJSONObject("product").optString("item_language"),
                                    jsonArray.getJSONObject(i).optJSONObject("product").optString("price").substring(0, jsonArray.getJSONObject(i).optJSONObject("product").optString("price").length() - 2)
                            );
                            banners.add(banner);
                        }


                        adapterViewPager = new BannerAdapter(getSupportFragmentManager(), banners);
                        activityHomeBinding.banner.setAdapter(adapterViewPager);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


        SHPTBase
                .getInstance()
                .getApiProvider()
                .getPopular(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody.string());

                        List<Banner> banners1 = new ArrayList<Banner>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Banner banner = new Banner(
                                    jsonArray.getJSONObject(i).optString("name"),
                                    jsonArray.getJSONObject(i).optString("product_id"),
                                    jsonArray.getJSONObject(i).optString("image").replace(".jpg", "-550x550.jpg"),
                                    Html.fromHtml(jsonArray.getJSONObject(i).optString("description")).toString(),
                                    jsonArray.getJSONObject(i).optString("author"),
                                    jsonArray.getJSONObject(i).optString("item_language"),
                                    jsonArray.getJSONObject(i).optString("price").substring(0, jsonArray.getJSONObject(i).optString("price").length() - 2)

                            );
                            banners1.add(banner);
                        }


                        RxDataSource<Banner> bannerRxDataSource = new RxDataSource<Banner>(banners1);
                        bannerRxDataSource
                                .<ProductCardBinding>bindRecyclerView(activityHomeBinding.popularProducts, R.layout.product_card)
                                .subscribe(viewHolder -> {

                                    Log.i("Product_name", URLConfig.IMAGE_URL + viewHolder.getItem().getImage());
                                    ProductCardBinding b = viewHolder.getViewDataBinding();
                                    b.produtName.setText(viewHolder.getItem().getTitle());
                                    b.productPrice.setText(viewHolder.getItem().getPrice());

                                    Glide
                                            .with(this)
                                            .load(URLConfig.IMAGE_URL + viewHolder.getItem().getImage())
                                            .centerCrop()
                                            .placeholder(R.drawable.no_image)
                                            .crossFade()
                                            .into(b.productImage);


                                });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        menu.findItem(R.id.cart).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_cart).actionBar().color(Color.WHITE));
        menu.findItem(R.id.notification).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_bell).actionBar().color(Color.WHITE));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConnectionChangeEvent event) {
        if (event.getMessage() == true) {
            activityHomeBinding.networkProblemLayout.setVisibility(View.GONE);
            activityHomeBinding.contentLayout.setVisibility(View.VISIBLE);
            doTask();
        } else {
            activityHomeBinding.networkProblemLayout.setVisibility(View.VISIBLE);
            activityHomeBinding.contentLayout.setVisibility(View.GONE);
        }
    }
}

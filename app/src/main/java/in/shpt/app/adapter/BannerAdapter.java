package in.shpt.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

import in.shpt.app.fragment.BannerFragment;
import in.shpt.app.models.Banner;

/**
 * Created by iampo on 10/8/2016.
 */

public class BannerAdapter extends FragmentPagerAdapter{
    private List<Banner> banners;

    public BannerAdapter(FragmentManager fm,List<Banner> banners) {
        super(fm);
        this.banners = banners;
    }


    @Override
    public Fragment getItem(int position) {
        return BannerFragment.newInstance(banners.get(position));
    }

    @Override
    public int getCount() {
        return banners.size();
    }
}

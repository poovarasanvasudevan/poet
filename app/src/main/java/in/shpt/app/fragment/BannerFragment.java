package in.shpt.app.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import in.shpt.app.R;
import in.shpt.app.databinding.FragmentBannerBinding;
import in.shpt.app.models.Banner;

/**
 * Created by iampo on 10/8/2016.
 */

public class BannerFragment extends Fragment {

    FragmentBannerBinding fragmentBannerBinding;

    // Store instance variables
    private Banner banner;


    // newInstance constructor for creating fragment with arguments
    public static BannerFragment newInstance(Banner banner1) {
        BannerFragment fragmentFirst = new BannerFragment();
        Bundle args = new Bundle();
        args.putParcelable("banner", banner1);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        banner = getArguments().getParcelable("banner");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBannerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_banner, container, false);
        fragmentBannerBinding.productName.setText(banner.getTitle());
        Glide
                .with(this)
                .load(banner.getImage())
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .crossFade()
                .into(fragmentBannerBinding.bannerImage);

        fragmentBannerBinding.productPrice.setText(banner.getPrice());
        fragmentBannerBinding.productAuthor.setText(banner.getAuthor());


        return fragmentBannerBinding.getRoot();
    }
}

package ca.bcit.timberproject;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageAdapter extends PagerAdapter {
    private Context imageContext;
    private int[] imgArray = {R.drawable.falls_lake, R.drawable.lightning_loop, R.drawable.pender_hill};

    ImageAdapter(Context c) {
        imageContext = c;
    }

    @Override
    public int getCount() {
        return imgArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imgView = new ImageView(imageContext);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgView.setImageResource(imgArray[position]);
        container.addView(imgView, 0);
        return imgView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}

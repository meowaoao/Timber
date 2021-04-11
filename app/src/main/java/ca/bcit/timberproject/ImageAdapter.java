package ca.bcit.timberproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class ImageAdapter extends PagerAdapter {
    private Context imageContext;
//    private int[] imgArray = {R.drawable.falls_lake, R.drawable.lightning_loop, R.drawable.pender_hill};
    private String[] imgUrlArray;

    ImageAdapter(Context c, int position) {
        imageContext = c;
        String sampleUrl = Hike.hikes[position].getImageID();
        String subString = sampleUrl.substring(0, sampleUrl.length()-5);
        System.out.println("<-----------" + subString + "----------->");
        imgUrlArray = new String[6];
        for (int i = 0; i < 6; i++) {
            String url = subString + (i+1) + ".jpg";
            imgUrlArray[i] = url;
            System.out.println("<-----------" + url + "----------->");
        }
        //https://www.vancouvertrails.com/images/photos/pender-hill-4.jpg
    }

    @Override
    public int getCount() {
//        return imgArray.length;
        return imgUrlArray.length;
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
//        imgView.setImageResource(imgArray[position]);
        Picasso.get().load(imgUrlArray[position]).into(imgView);
        container.addView(imgView, 0);
        return imgView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}

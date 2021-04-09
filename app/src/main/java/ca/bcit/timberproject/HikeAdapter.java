package ca.bcit.timberproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {
    private Hike[] hikes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView item;

        public ViewHolder(CardView v) {
            super(v);
            item = v;
        }
    }

    /**
     * Accepts a list of hikes, this can be the entire list of hikes for the home page or
     * the recent hike list of a user to render the recent hike fragment.
     * @param hikeList
     */
    public HikeAdapter(Hike[] hikeList) {
        this.hikes = hikeList;
    }

    @NonNull
    @Override
    public HikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hike_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder.item;

        CardView card = cardView.findViewById(R.id.hikeCard);
//        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) card.getLayoutParams();
//        GradientDrawable shape = new GradientDrawable();
//        shape.setShape(GradientDrawable.RECTANGLE);
//        shape.setCornerRadius(20);
//        card.setBackground(shape);


        String url = hikes[position].getImageID();
        new DownloadImageTask((ImageView) cardView.findViewById(R.id.hikeImage)).execute(url);

//        ImageView imgView = cardView.findViewById(R.id.hikeImage);
//        imgView.setImageResource(hikes[position].getImageID());

        TextView nameView = cardView.findViewById(R.id.hikeName);
        nameView.setText(hikes[position].getName());
        TextView locView = cardView.findViewById(R.id.hikeLocation);
        locView.setText(hikes[position].getRegion());
        TextView diffView = cardView.findViewById(R.id.hikeDifficulty);
        diffView.setText(hikes[position].getDifficulty());
        TextView timeView = cardView.findViewById(R.id.hikeTimeLength);
        timeView.setText(hikes[position].getTimeLength());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cardView.getContext(), ViewHike.class);
                intent.putExtra("position", position);
                cardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hikes.length;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

package ca.bcit.timberproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Review[] reviews;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView item;

        public ViewHolder(CardView v) {
            super(v);
            item = v;
        }
    }

    public ReviewAdapter(Review[] reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);
        return new ReviewAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder.item;

        CardView card = cardView.findViewById(R.id.reviewerCard);
        TextView name = card.findViewById(R.id.reviewerName);
        name.setText(reviews[position].getName());
        TextView desc = card.findViewById(R.id.reviewerReview);
        desc.setText(reviews[position].getDescription());
        RatingBar rate = card.findViewById(R.id.reviewerStars);
        rate.setNumStars(reviews[position].getStars());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.length;
    }
}

package ca.bcit.timberproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ConnectAdapter extends RecyclerView.Adapter<ConnectAdapter.ViewHolder> {
    private User[] users;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView item;

        public ViewHolder(CardView v) {
            super(v);
            item = v;
        }
    }

    /**
     * Accepts a list of users to display for the user connect feature.
     * @param userList array.
     */
    public ConnectAdapter(User[] userList) {
        this.users = userList;
    }

    @NonNull
    @Override
    public ConnectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);
        return new ConnectAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder.item;

        CardView card = cardView.findViewById(R.id.userCard);
        ImageView imgView = cardView.findViewById(R.id.userCardImage);
        imgView.setImageResource(users[position].getProfileImage());
        TextView nameView = cardView.findViewById(R.id.userCardName);
        nameView.setText(users[position].getName());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cardView.getContext(), ViewProfile.class);
                cardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.length;
    }
}

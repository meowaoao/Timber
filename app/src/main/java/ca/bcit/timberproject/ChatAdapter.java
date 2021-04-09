package ca.bcit.timberproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<Message> messages;
    private String user;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout item;

        public ViewHolder(FrameLayout v) {
            super(v);
            item = v;
        }
    }

    /**
     * Accepts a list of users to display for the user connect feature.
     * @param messages array.
     */
    public ChatAdapter(ArrayList<Message> messages) {
        this.messages = messages;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout cv = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_message, parent, false);
        return new ChatAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        final FrameLayout msgFrame = holder.item;

        CardView card = msgFrame.findViewById(R.id.messageCard);
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) card.getLayoutParams();

        if (messages.get(position).getUserID().equals(user)) {
            int userColor = 0xFF0D5C63;
            card.setCardBackgroundColor(userColor);
            param.gravity = GravityCompat.END;
            param.setMarginStart(100);
            param.setMarginEnd(5);
        } else {
            int partnerColor = 0xFF2A2B2A;
            card.setCardBackgroundColor(partnerColor);
            param.gravity = GravityCompat.START;
            param.setMarginStart(5);
            param.setMarginEnd(100);
        }
        card.setLayoutParams(param);
        TextView textBody = msgFrame.findViewById(R.id.messageBody);
        textBody.setText(messages.get(position).getTextBody());
        TextView textTime = msgFrame.findViewById(R.id.messageTime);
        textTime.setText(messages.get(position).getTextTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}

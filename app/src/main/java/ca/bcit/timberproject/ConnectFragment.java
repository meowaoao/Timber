package ca.bcit.timberproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ConnectFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_connect, container, false);

        RecyclerView connectRecycler = view.findViewById(R.id.connectRecycler);
        User[] userList = User.members;

        ConnectAdapter adapter = new ConnectAdapter(userList);
        connectRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        connectRecycler.setLayoutManager(lm);

        return view;
    }
}
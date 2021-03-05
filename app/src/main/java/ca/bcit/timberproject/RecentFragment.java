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

public class RecentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recent, container, false);

        RecyclerView recentRecycler = view.findViewById(R.id.recentRecycler);
        Hike[] hikeList = Hike.recentHikes;

        HikeAdapter adapter = new HikeAdapter(hikeList);
        recentRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recentRecycler.setLayoutManager(lm);

        return view;
    }
}

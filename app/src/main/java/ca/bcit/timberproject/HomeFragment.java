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

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView hikeRecycler = view.findViewById(R.id.hikeRecycler);
        Hike[] hikeList = Hike.hikes;
        System.out.println("<--- hikeList in HomeFragment ---->");
        System.out.println("<----- length of hikeList is " + hikeList.length);

        HikeAdapter adapter = new HikeAdapter(hikeList);
        hikeRecycler.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        hikeRecycler.setLayoutManager(lm);

        return view;
    }
}

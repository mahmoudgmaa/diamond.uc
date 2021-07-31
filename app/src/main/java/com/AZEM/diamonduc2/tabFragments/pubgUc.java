package com.AZEM.diamonduc2.tabFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.adapters.rewardAdapter;
import com.AZEM.diamonduc2.models.mainModel;

import java.util.ArrayList;


public class pubgUc extends Fragment {
    private RecyclerView recyclerView;
    private rewardAdapter adapter;
    private ArrayList<mainModel> pubgayaArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pubg_uc, container, false);
        recyclerView = view.findViewById(R.id.pubgRec);
        recyclerView.setHasFixedSize(true);
        pubgayaArrayList = new ArrayList<>();
        readpubgayat();
        adapter = new rewardAdapter(getContext(), pubgayaArrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void readpubgayat() {
        pubgayaArrayList.clear();
        mainModel pubg1 = new mainModel("60 UC", "3000 D", 3000, "pubg");
        mainModel pubg4 = new mainModel("325 UC", "12000 D", 12000, "pubg");
        mainModel pubg5 = new mainModel("690 UC", "30000 D", 30000, "pubg");
        mainModel pubg6 = new mainModel("1800 UC", "65000 D", 65000, "pubg");
        mainModel pubg7 = new mainModel("3850 UC", "95000 D", 95000, "pubg");
        pubgayaArrayList.add(pubg1);
        pubgayaArrayList.add(pubg4);
        pubgayaArrayList.add(pubg5);
        pubgayaArrayList.add(pubg6);
        pubgayaArrayList.add(pubg7);

    }
}
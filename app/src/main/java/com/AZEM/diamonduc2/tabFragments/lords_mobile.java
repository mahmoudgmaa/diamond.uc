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


public class lords_mobile extends Fragment {
    ArrayList<mainModel> arrayList;
    private RecyclerView recyclerView;
    private rewardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lords_mobile, container, false);
        recyclerView = view.findViewById(R.id.lordsMobileRec);
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        readLord();
        adapter = new rewardAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void readLord() {
        arrayList.clear();
        mainModel pubg1 = new mainModel("60 Diamonds", "2000 D", 2000, "lord");
        mainModel pubg3 = new mainModel("150 Diamonds", "3000 D", 3000, "lord");
        mainModel pubg4 = new mainModel("300 Diamonds", "6000 D", 60000, "lord");
        mainModel pubg5 = new mainModel("600 Diamonds", "11000 D", 11000, "lord");
        mainModel pubg6 = new mainModel("1497 Diamonds", "26000 D", 26000, "lord");
        arrayList.add(pubg1);
        arrayList.add(pubg3);
        arrayList.add(pubg4);
        arrayList.add(pubg5);
        arrayList.add(pubg6);

    }
}
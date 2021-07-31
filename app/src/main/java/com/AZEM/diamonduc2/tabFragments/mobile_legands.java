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


public class mobile_legands extends Fragment {
    private RecyclerView mobileLegandsRec;
    private ArrayList<mainModel> arrayList;
    private rewardAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mobile_legands, container, false);

        mobileLegandsRec=view.findViewById(R.id.mobileLegandsRec);
        mobileLegandsRec.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        readLegand();
        adapter = new rewardAdapter(getContext(), arrayList);
        mobileLegandsRec.setAdapter(adapter);
        return view;
    }

    private void readLegand() {
        arrayList.clear();
        mainModel pubg1 = new mainModel("21 Diamonds", "1000 D", 1000, "legand");
        mainModel pubg3 = new mainModel("31 Diamonds", "2000 D", 2000, "legand");
        mainModel pubg4 = new mainModel("155 Diamonds", "6000 D", 6000, "legand");
        mainModel pubg5 = new mainModel("311 Diamonds", "11000 D", 11000, "legand");
        mainModel pubg6 = new mainModel("933 Diamonds", "30000 D", 30000, "legand");
        arrayList.add(pubg1);
        arrayList.add(pubg3);
        arrayList.add(pubg4);
        arrayList.add(pubg5);
        arrayList.add(pubg6);
    }
}
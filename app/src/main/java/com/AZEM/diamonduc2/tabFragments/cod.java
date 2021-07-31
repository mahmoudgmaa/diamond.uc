package com.AZEM.diamonduc2.tabFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.adapters.rewardAdapter;
import com.AZEM.diamonduc2.models.mainModel;

import java.util.ArrayList;

public class cod extends Fragment {
    private RecyclerView recyclerView;
    private rewardAdapter adapter;
    private ArrayList<mainModel> freeFireayaArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cod, container, false);
        recyclerView=view.findViewById(R.id.codFireRec);
        recyclerView.setHasFixedSize(true);
        freeFireayaArrayList = new ArrayList<>();
        readFreeFireat();
        adapter = new rewardAdapter(getContext(), freeFireayaArrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void readFreeFireat() {
        freeFireayaArrayList.clear();
        mainModel freeFireaya = new mainModel("31 CP", "1500 D", 1500, "cod");
        mainModel freeFireaya1 = new mainModel("62 CP", "2000 D", 2000, "cod");
        mainModel freeFireaya2 = new mainModel("127 CP", "3500 D", 3500, "cod");
        mainModel freeFireaya3 = new mainModel("317 CP", "7000 D", 7000, "cod");
        mainModel freeFireaya4 = new mainModel("634 CP", "14000 D", 14000, "cod");
        mainModel freeFireaya5 = new mainModel("1373 CP", "35000 D", 35000, "cod");
        freeFireayaArrayList.add(freeFireaya);
        freeFireayaArrayList.add(freeFireaya1);
        freeFireayaArrayList.add(freeFireaya2);
        freeFireayaArrayList.add(freeFireaya3);
        freeFireayaArrayList.add(freeFireaya4);
        freeFireayaArrayList.add(freeFireaya5);
    }
}
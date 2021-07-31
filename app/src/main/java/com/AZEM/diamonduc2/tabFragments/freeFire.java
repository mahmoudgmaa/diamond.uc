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


public class freeFire extends Fragment {
    private RecyclerView recyclerView;
    private rewardAdapter adapter;
    private ArrayList<mainModel> freeFireayaArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_fire, container, false);
        recyclerView = view.findViewById(R.id.freeFireRec);
        recyclerView.setHasFixedSize(true);
        freeFireayaArrayList = new ArrayList<>();
        readFreeFireat();
        adapter = new rewardAdapter(getContext(), freeFireayaArrayList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void readFreeFireat() {
        freeFireayaArrayList.clear();
        mainModel freeFireaya = new mainModel("55 Diamond", "1500 D", 1500, "freefire");
        mainModel freeFireaya1 = new mainModel("110 Diamond", "2000 D", 2000, "freefire");
        mainModel freeFireaya2 = new mainModel("231 Diamond", "3500 D", 3500, "freefire");
        mainModel freeFireaya3 = new mainModel("530 Diamond", "10000 D", 10000, "freefire");
        mainModel freeFireaya4 = new mainModel("1088 Diamonds", "35000 D", 35000, "freefire");
        mainModel freeFireaya5 = new mainModel("2200 Diamonds", "64000 D", 64000, "freefire");
        freeFireayaArrayList.add(freeFireaya);
        freeFireayaArrayList.add(freeFireaya1);
        freeFireayaArrayList.add(freeFireaya2);
        freeFireayaArrayList.add(freeFireaya3);
        freeFireayaArrayList.add(freeFireaya4);
        freeFireayaArrayList.add(freeFireaya5);
    }
}
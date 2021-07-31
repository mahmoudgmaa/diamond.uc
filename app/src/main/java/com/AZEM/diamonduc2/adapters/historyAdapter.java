package com.AZEM.diamonduc2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AZEM.diamonduc2.models.history;
import com.AZEM.diamonduc2.R;

import java.util.ArrayList;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.viewHolder> {
    private ArrayList<history> historyArrayList;
    private Context context;

    public historyAdapter(ArrayList<history> historyArrayList, Context context) {
        this.historyArrayList = historyArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        history history1 = historyArrayList.get(position);
        holder.details.setText(history1.getDetails());
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView details;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            details = itemView.findViewById(R.id.transitionDetails);
        }
    }
}

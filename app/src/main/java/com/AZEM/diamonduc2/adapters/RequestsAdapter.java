package com.AZEM.diamonduc2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AZEM.diamonduc2.R;
import com.AZEM.diamonduc2.models.Requests;

import java.util.ArrayList;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.viewHolder> {
    private Context mContext;
    private List<Requests> requests1 = new ArrayList<>();

    public RequestsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.taskitem, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Requests request = requests1.get(position);
        String type = request.getRequestType();
        switch (type) {
            case "pubg":
                holder.imageView.setImageResource(R.drawable.ppuubbgg);
                break;
            case "cash":
                holder.imageView.setImageResource(R.drawable.cash4);
                break;
            case "freefire":
                holder.imageView.setImageResource(R.drawable.freefire1);
                break;
            case "lord":
                holder.imageView.setImageResource(R.drawable.lords);
                break;
            case "googleplay":
                holder.imageView.setImageResource(R.drawable.cardgg);
                break;
            case "netflix":
                holder.imageView.setImageResource(R.drawable.netflix);
                break;
            case "lol":
                holder.imageView.setImageResource(R.drawable.lol2);
                break;
            case "crossfire":
                holder.imageView.setImageResource(R.drawable.crossfire);
                break;
            case "fortnite":
                holder.imageView.setImageResource(R.drawable.fortnite);
                break;
            case "legand":
                holder.imageView.setImageResource(R.drawable.mobilelegends);
                break;
        }
        holder.taskText.setText(request.getRequestName());
        holder.taskDetails.setText(request.getRequestCondition());
    }

    @Override
    public int getItemCount() {
        return requests1.size();
    }

    public void setRequests(List<Requests> requests) {
        this.requests1 = requests;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView taskText;
        private TextView taskDetails;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.taskImage);
            taskText = itemView.findViewById(R.id.taskName);
            taskDetails = itemView.findViewById(R.id.taskDetails);
        }
    }
}

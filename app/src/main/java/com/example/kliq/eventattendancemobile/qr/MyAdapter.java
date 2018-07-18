package com.example.kliq.eventattendancemobile.qr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kliq.eventattendancemobile.R;

import java.util.List;

/**
 * Created by ajmal on 7/10/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private List<EventItem> eventItems;
    private Context context;



    public MyAdapter(List<EventItem> eventItems, Context context) {
        this.eventItems = eventItems;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(EventItem item);
    }
    private  OnItemClickListener listener;

    public MyAdapter(List<EventItem> items, OnItemClickListener listener) {
        this.eventItems = items;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EventItem eventItem = eventItems.get(position);
        holder.textViewHead.setText(eventItem.getHead());
//        holder.textViewDesc.setText(eventItem.getDesc());
        holder.bind(eventItems.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return eventItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
        }

        public void bind(final EventItem item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);

                }
            });
        }
    }
}
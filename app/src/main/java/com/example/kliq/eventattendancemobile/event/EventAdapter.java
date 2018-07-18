package com.example.kliq.eventattendancemobile.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.data.model.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {


    private List<Event> eventItems;
    private Context context;



    public EventAdapter(List<Event> eventItems, Context context) {
        this.eventItems = eventItems;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(Event item);
    }
    private EventAdapter.OnItemClickListener listener;

    public EventAdapter(List<Event> items, EventAdapter.OnItemClickListener listener) {
        this.eventItems = items;
        this.listener = listener;
    }
    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new EventAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {

        Event eventItem = eventItems.get(position);
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

        public void bind(final Event item, final EventAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);

                }
            });
        }
    }
}

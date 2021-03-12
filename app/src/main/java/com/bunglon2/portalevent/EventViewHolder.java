package com.bunglon2.portalevent;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class EventViewHolder extends RecyclerView.ViewHolder {
    TextView tvName,tvDate, tvLocation, tvLink, tvCategory;
    View mView;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        //initialize views with item_event
        tvName = itemView.findViewById(R.id.NameEvent);
        tvDate = itemView.findViewById(R.id.Date);
        tvLocation = itemView.findViewById(R.id.Location);
        tvLink = itemView.findViewById(R.id.Link);
        tvCategory = itemView.findViewById(R.id.Category);
    }

    private EventViewHolder.ClickListener mClickListener;
    //Interface for click listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(EventViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }




}

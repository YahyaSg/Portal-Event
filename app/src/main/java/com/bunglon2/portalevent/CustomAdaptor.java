package com.bunglon2.portalevent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdaptor extends RecyclerView.Adapter<EventViewHolder> {

    MainActivity mainActivity;
    List<EventModel> modelList;
    Context context;

    public CustomAdaptor(MainActivity mainActivity, List<EventModel> modelList) {
        this.mainActivity = mainActivity;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        //inflate layout
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_event, viewGroup, false);

        EventViewHolder eventViewHolder = new EventViewHolder(itemView);

        //handle item clicks
        eventViewHolder.setOnClickListener(new EventViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //akan dipanggil ketika user clik item


                //tampilkan data pada toast
                String nama = (modelList.get(position).nama);
                String tanggal = (modelList.get(position).tanggal);
                String tempat = (modelList.get(position).tempat);
                String reglink = (modelList.get(position).reglink);
                String kategori = (modelList.get(position).kategori);
                String deskripsi = (modelList.get(position).deskripsi);

                Toast.makeText(mainActivity, nama+"\n"+kategori, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                //akan dipanggil ketika user long clik item
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                //Display Alert Dialog
                String[] option = {"Update", "Delete"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //Update click
                            String id = (modelList.get(position).id);
                            String nama = (modelList.get(position).nama);
                            String tanggal = (modelList.get(position).tanggal);
                            String tempat = (modelList.get(position).tempat);
                            String reglink = (modelList.get(position).reglink);
                            String kategori = (modelList.get(position).kategori);
                            String deskripsi = (modelList.get(position).deskripsi);

                            //inten to addevent
                            Intent intent = new Intent(mainActivity, AddEventActivity.class);
                            //put data in intent
                            intent.putExtra("pid", id);
                            intent.putExtra("pnama", nama);
                            intent.putExtra("ptanggal", tanggal);
                            intent.putExtra("ptempat", tempat);
                            intent.putExtra("preglink", reglink);
                            intent.putExtra("pkategori", kategori);
                            intent.putExtra("pdeskripsi", deskripsi);

                            // start activity
                            mainActivity.startActivity(intent);

                        }
                        if (which == 1) {
                            //delete click
                            mainActivity.deleteData(position);
                        }
                    }
                }).create().show();
            }
        });


        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int i) {
        //bind views/set data
        holder.tvName.setText(modelList.get(i).nama);
        holder.tvDate.setText(modelList.get(i).tanggal);
        holder.tvLocation.setText(modelList.get(i).tempat);
        holder.tvLink.setText(modelList.get(i).reglink);
        holder.tvCategory.setText(modelList.get(i).kategori);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}

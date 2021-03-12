package com.bunglon2.portalevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<EventModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;

    //layout manager for recyelerview
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton mAddBtn;

    //firebase instance
    FirebaseFirestore db;

    CustomAdaptor adaptor;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inisiasi firebase firestore
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recycler_view);
        mAddBtn = findViewById(R.id.addEvent);


        // set RecyelerView properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //inisialisai Progres dialog
        pd= new ProgressDialog(this);

        //Show data in recycler
        showData();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showData(){

        //set Progres dialog
        pd.setTitle("Loading Data..");
        pd.show();

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        //called when data is retrived
                        pd.dismiss();
                        //showdata
                        for(DocumentSnapshot events: task.getResult()){

                            EventModel eventModel = new EventModel(events.getString("id"),
                            events.getString("nama"),
                            events.getString("tanggal"),
                            events.getString("tempat"),
                            events.getString("reglink"),
                            events.getString("kategori"),
                            events.getString("deskripsi"));

                            modelList.add(eventModel);
                        }

                        //adapter
                        adaptor = new CustomAdaptor(MainActivity.this, modelList);

                        //set adaptor to recylerview
                        mRecyclerView.setAdapter(adaptor);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // called when error
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteData(int index){
        pd.setTitle("Deleting Data...");
        pd.show();

        db.collection("events").document(modelList.get(index).id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Deleted...", Toast.LENGTH_SHORT).show();
                        showData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchData(String query) {
        pd.setTitle("Searching...");
        pd.show();

        db.collection("events").whereEqualTo("search", query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //dipanggil ketika berhasil search
                        modelList.clear();
                        pd.dismiss();
                        //showdata
                        for(DocumentSnapshot events: task.getResult()){

                            EventModel eventModel = new EventModel(events.getString("id"),
                                    events.getString("nama"),
                                    events.getString("tanggal"),
                                    events.getString("tempat"),
                                    events.getString("reglink"),
                                    events.getString("kategori"),
                                    events.getString("deskripsi"));

                            modelList.add(eventModel);
                        }

                        //adapter
                        adaptor = new CustomAdaptor(MainActivity.this, modelList);

                        //set adaptor to recylerview
                        mRecyclerView.setAdapter(adaptor);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //dipanggil ketika gagal search
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    //menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu_main.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //search view
        MenuItem item = menu.findItem(R.id.action_serach);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // dipanggil ketika ditekan search
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handel mennu lain
        if(item.getItemId() == R.id.action_setting){
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

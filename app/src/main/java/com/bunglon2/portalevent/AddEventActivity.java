package com.bunglon2.portalevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    //view
    EditText EtNama, EtTanggal, Reglink, EtTempat, EtDeskripsi;
    Spinner EtKategori;
    Button buttonSubmitEvent, btnBack;
    int mYear, mMonth, mDay, mHour, mMinute;

    ProgressDialog pd;

    //firebase instance
    FirebaseFirestore db;

    String pid,pnama,ptempat,ptanggal,preglink,pkategori,pdeskripsi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");


        //inisiasi view dengan xml

        EtNama = findViewById(R.id.EtNama);
        EtTanggal = findViewById(R.id.EtTanggal);
        EtTempat = findViewById(R.id.EtTempat);
        Reglink = findViewById(R.id.reglink);
        EtKategori = findViewById(R.id.kategori);
        EtDeskripsi = findViewById(R.id.EtDeskripsi);
        buttonSubmitEvent = findViewById(R.id.submitEvent);
        btnBack = findViewById(R.id.btnbck);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //Update data
            actionBar.setTitle("Update");
            buttonSubmitEvent.setText("Update");
        }
        else{
            actionBar.setTitle("Add Data");
            buttonSubmitEvent.setText("Add Event");
            //get data
            pid = bundle.getString("pid");
            pnama = bundle.getString("pnama");
            ptanggal = bundle.getString("ptanggal");
            ptempat = bundle.getString("ptempat");
            preglink = bundle.getString("preglink");
            pkategori = bundle.getString("pkategori");
            pdeskripsi = bundle.getString("pdeskripsi");

            //Set Data
            EtNama.setText("pnama");
            EtTanggal.setText("ptanggal");
            EtTempat.setText("ptempat");
            Reglink.setText("preglink");
           EtKategori.getSelectedItem().toString();
            EtDeskripsi.setText("pdeskripsi");

        }

        String[] arraySpinner = new String[]{
                "Seminar", "Workshop", "Konser", "Pameran"
        };

        EtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                showTimeDialog();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        EtKategori.setAdapter(adapter);

        //inisiasi progres dialog
        pd = new ProgressDialog(this);

        //inisiasi firebase firestore
        db = FirebaseFirestore.getInstance();

        //click button to save data

        buttonSubmitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null){
                    //Updateing
                    String id = pid;
                    String namaEvent = EtNama.getText().toString().trim();
                    String tanggal = EtTanggal.getText().toString().trim();
                    String tempat = EtTempat.getText().toString().trim();
                    String reglink = Reglink.getText().toString().trim();
                    String kategori = EtKategori.getSelectedItem().toString().trim();
                    String deskripsi = EtDeskripsi.getText().toString().trim();
                    uploadData (id,namaEvent,tanggal,tempat,reglink,kategori,deskripsi);

                }
                else {
                    String namaEvent = EtNama.getText().toString().trim();
                    String tanggal = EtTanggal.getText().toString().trim();
                    String tempat = EtTempat.getText().toString().trim();
                    String reglink = Reglink.getText().toString().trim();
                    String kategori = EtKategori.getSelectedItem().toString().trim();
                    String deskripsi = EtDeskripsi.getText().toString().trim();

                    uploaddata (namaEvent,tanggal,tempat,reglink,kategori,deskripsi);
                }

                //Save Data
                String namaEvent = EtNama.getText().toString().trim();
                String tanggal = EtTanggal.getText().toString().trim();
                String tempat = EtTempat.getText().toString().trim();
                String reglink = Reglink.getText().toString().trim();
                String kategori = EtKategori.getSelectedItem().toString().trim();
                String deskripsi = EtDeskripsi.getText().toString().trim();

                //function upload data
                uploaddata (namaEvent,tanggal,tempat,reglink,kategori,deskripsi);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEventActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void uploadData(String id, String namaEvent, String tanggal, String tempat, String reglink, String kategori, String deskripsi) {
    //Title Progres Bar
        pd.setTitle("Updating Data...");
        pd.show();

        db.collection("events").document(id)
                .update("nama",namaEvent,"search", namaEvent.toLowerCase(), "tanggal",tanggal,"tempat",tempat,"reglink",reglink,"kategori",kategori,"deskripsi",deskripsi)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    //dipanggil ketika berhasil update
                        pd.dismiss();
                        Toast.makeText(AddEventActivity.this, "Update...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    //dipanggil ketika update gagal/error
                        pd.dismiss();
                        Toast.makeText(AddEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void showTimeDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        EtTanggal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void uploaddata(String namaEvent, String tanggal, String tempat, String reglink, String kategori, String deskripsi) {
        //set titleof progres bar
        pd.setTitle("Add Data..");
        //Show progres
        pd.show();
        //random id for each data to be stored
        String id = UUID.randomUUID().toString();

        Map<String,Object> event = new HashMap<>();
        event.put("id", id);
        event.put("nama", namaEvent);
        event.put("Search", namaEvent.toLowerCase());
        event.put("tanggal", tanggal);
        event.put("tempat", tempat);
        event.put("reglink", reglink);
        event.put("kategori", kategori);
        event.put("deskripsi", deskripsi);

    //add this data
        db.collection("events").document(id).set(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //yang akan dipanggil ketika data di add
                        pd.dismiss();
                        Toast.makeText(AddEventActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //yang akan dipanggil ketika error
                        pd.dismiss();
                        Toast.makeText(AddEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

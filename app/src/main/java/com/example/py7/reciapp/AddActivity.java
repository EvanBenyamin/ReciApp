package com.example.py7.reciapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxNama;
    Spinner TxJenisHidangan;
    EditText TxAlamat;
    EditText TxLangkah;
    long id;
    CircularImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxNama = (EditText)findViewById(R.id.txNama_Add);
        TxJenisHidangan = (Spinner) findViewById(R.id.spJw_Add);
        TxAlamat = (EditText)findViewById(R.id.txBahan_Add);
        TxLangkah = (EditText)findViewById(R.id.tx_Langkah_Add);
        imageView = (CircularImageView)findViewById(R.id.image_profile);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(AddActivity.this);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_add:
                String nama = TxNama.getText().toString().trim();
                String jenisHidangan = TxJenisHidangan.getSelectedItem().toString().trim();
                String bahan = TxAlamat.getText().toString().trim();
                String langkah = TxLangkah.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_nama, nama);
                values.put(DBHelper.row_jenis_hidangan, jenisHidangan);
                values.put(DBHelper.row_bahan, bahan);
                values.put(DBHelper.row_langkah, langkah);
                values.put(DBHelper.row_foto, String.valueOf(uri));

                if ( nama.equals("") || jenisHidangan.equals("") || bahan.equals("") || langkah.equals("")){
                    Toast.makeText(AddActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    helper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)){
                uri = imageuri;
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}
                        , 0);
            }else{
                startCrop(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageView.setImageURI(result.getUri());
                uri = result.getUri();
            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
        uri = imageuri;
    }
}
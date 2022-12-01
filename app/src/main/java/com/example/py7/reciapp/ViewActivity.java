package com.example.py7.reciapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ViewActivity extends AppCompatActivity {
    DBHelper helper;
    TextView TxNama, TxJenisHidangan, TxBahan, TxLangkah;
    long id;
    CircularImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);
        TxNama = (TextView) findViewById(R.id.txNama_View);
        TxJenisHidangan = (TextView) findViewById(R.id.txJenisHidangan_View);
        TxBahan = (TextView) findViewById(R.id.txAlamat_View);
        TxLangkah = (TextView)findViewById(R.id.tx_Langkah_View);
        imageView = (CircularImageView) findViewById(R.id.image_profile);
        getData();
    }

    private void getData() {
        Cursor cursor = helper.oneData(id);
        if (cursor.moveToFirst()) {
            String nama = cursor.getString(cursor.getColumnIndex(DBHelper.row_nama));
            String jenisHidangan = cursor.getString(cursor.getColumnIndex(DBHelper.row_jenis_hidangan));
            String bahan = cursor.getString(cursor.getColumnIndex(DBHelper.row_bahan));
            String langkah = cursor.getString(cursor.getColumnIndex(DBHelper.row_langkah));
            String foto = cursor.getString(cursor.getColumnIndex(DBHelper.row_foto));

            TxNama.setText(nama);

            TxLangkah.setText(langkah);
            TxJenisHidangan.setText(jenisHidangan);
            TxBahan.setText(bahan);

            if (foto.equals("null")) {
                imageView.setImageResource(R.drawable.iconfood);
            } else {
                imageView.setImageURI(Uri.parse(foto));
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(ViewActivity.this,LoginActivity.class);
            startActivity(intent);
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
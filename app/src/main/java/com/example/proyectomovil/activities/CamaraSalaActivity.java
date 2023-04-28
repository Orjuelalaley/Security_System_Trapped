package com.example.proyectomovil.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.CamaraSalaBinding;
import com.example.proyectomovil.utils.AlertUtils;


public class CamaraSalaActivity extends AppCompatActivity {
    private static final String TAG = CamaraSalaActivity.class.getName();
    CamaraSalaBinding binding;

    private final int CAMERA_VIDEO_PERMISSION_ID = 101;

    private final int GALLERY_VIDEO_PERMISSION_ID = 102;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 103;
    String camaraPerm = android.Manifest.permission.CAMERA;
    String storagePerm  = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    String storageWritePerm = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    String currentVideoPath;
    private Uri videoUri;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CamaraSalaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.video.setVisibility(View.VISIBLE);
        binding.buttonTake.setOnClickListener(view -> {
            if(requestPermission(CamaraSalaActivity.this, new String[]{camaraPerm,storageWritePerm}, CAMERA_VIDEO_PERMISSION_ID)){
                startCameraVideo(binding.getRoot());
            }
        });
        binding.buttonGallery.setOnClickListener(view -> {
            if (requestGalleryPermission(this, storagePerm, GALLERY_VIDEO_PERMISSION_ID)){
                startGalleryVideo(binding.getRoot());
            }
        });
    }

    private boolean requestPermission(Activity context, String[] permission, int id) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    permission,
                    id);
            Log.w(TAG, "requestPermission: HOLA");
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    permission,
                    id);
            Log.w(TAG, "requestPermission: HOLA");
            return false;
        }
        return true;
    }

    private boolean requestGalleryPermission(Activity context, String permission, int id){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    context,
                    new String[]{permission},
                    id);
            return false;
        }
        return true;
    }


    private void initView(){
        if (ContextCompat.checkSelfPermission(this, camaraPerm)
                != PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "initView: no pude obtener el permiso :(");
            AlertUtils.indefiniteSnackbar(binding.getRoot(), getString(R.string.permission_denied_label));
        }else {
            Log.i(TAG, "initView: si pude obtener el permiso :)");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_VIDEO_PERMISSION_ID){
            startCameraVideo(binding.getRoot());
        }
        if(requestCode == GALLERY_VIDEO_PERMISSION_ID){
            startGalleryVideo(binding.getRoot());
        }
    }

    public void startCameraVideo(View view){
        if (ContextCompat.checkSelfPermission(this, camaraPerm)
                == PackageManager.PERMISSION_GRANTED){

            openCameraVideo();

        }else {
            AlertUtils.indefiniteSnackbar(binding.getRoot(), getString(R.string.permission_denied_label));
        }
    }

    private void openCameraVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                startActivityForResult(takeVideoIntent, CAMERA_VIDEO_PERMISSION_ID);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

        }
    }

    public void startGalleryVideo(View view){
        Intent pickGalleryVideo = new Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickGalleryVideo, GALLERY_VIDEO_PERMISSION_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_VIDEO_PERMISSION_ID -> {
                    assert data != null;
                    videoUri = data.getData();
                    binding.video.setVideoURI(videoUri);
                    binding.video.start();
                    binding.video.setMediaController(new MediaController(this));
                    saveVideoToGallery();
                    Log.i(TAG, "onActivityResult: video capturado correctamente");
                }
                case GALLERY_VIDEO_PERMISSION_ID -> {
                    assert data != null;
                    videoUri = data.getData();
                    binding.video.setVideoURI(videoUri);
                    binding.video.start();
                    binding.video.setMediaController(new MediaController(this));
                    Log.i(TAG, "onActivityResult: video cargado correctamente.");
                }
            }
        }
    }
    private void saveVideoToGallery() {
        // Agregar el archivo a la galería
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, "Video Title");
        values.put(MediaStore.Video.Media.DESCRIPTION, "Video Description");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoUri.getPath());
        getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        Toast.makeText(this, "Video guardado en la galería", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

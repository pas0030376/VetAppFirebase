package com.example.rachel.vetApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AdopcionesAnadir extends AppCompatActivity {
    EditText etTipoAnimal,etNombre,etDescripcion,etTelefono,etRefugio,etEmail,etCiudad,etPais;
    FloatingActionButton btnPublicar;
    ImageView foto_gallery;
    private DatabaseReference mRef;
    private Task<Void> mDatabase;
    private static final String IMAGE_DIRECTORY = "/vetApp";
    private int GALLERY = 1, CAMERA = 2;
    private Uri contentURI;


    FirebaseStorage storage = FirebaseStorage.getInstance();


    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopciones_anadir);

        etTipoAnimal = (EditText) findViewById(R.id.etTipoAnimal);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        etRefugio = (EditText) findViewById(R.id.etRefugio);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etCiudad = (EditText) findViewById(R.id.etCiudad);
        etPais = (EditText) findViewById(R.id.etPais);
        btnPublicar = (FloatingActionButton) findViewById(R.id.btnPublicar);

        foto_gallery = (ImageView)findViewById(R.id.foto_gallery);

        mStorage=FirebaseStorage.getInstance().getReference();

        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guadarNuevaAdopcion();
            }
        });

    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Seleccione una opción");
        String[] pictureDialogItems = {
                "Seleccionar foto desde galería",
                "Hacer foto" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    foto_gallery.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            foto_gallery.setImageBitmap(thumbnail);
            String path=saveImage(thumbnail);
            //contentURI=Uri.parse(path);
            contentURI=Uri.fromFile(new File(path));

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void guadarNuevaAdopcion(){
        String nom =etNombre.getText().toString();
        String type = etTipoAnimal.getText().toString();
        String desc = etDescripcion.getText().toString();
        String telefono = etTelefono.getText().toString();
        String refugio=etRefugio.getText().toString();
        String email=etEmail.getText().toString();
        String city = etCiudad.getText().toString();
        String pais=etPais.getText().toString();
        //creamos la carpeta fotos dentro del storage de Firebase
        StorageReference filePath=mStorage.child("fotos"+contentURI.getLastPathSegment());
        filePath.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdopcionesAnadir.this,"Adopción creada correctamente",Toast.LENGTH_LONG).show();
                Adopcion adopcion=new Adopcion(type,nom,desc,telefono,city,pais,taskSnapshot.getDownloadUrl().toString(),refugio,email);
                //Guardo la clase en firebase database
                mRef =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
                String mId = nom;
                //String uploadId=mRef.push().getKey();
                mDatabase = mRef.child("Adopcion").child(mId).setValue(adopcion);

            }
        });


    }



}

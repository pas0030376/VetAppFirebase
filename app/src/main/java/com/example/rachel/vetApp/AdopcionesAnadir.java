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
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AdopcionesAnadir extends AppCompatActivity {
    EditText etTipoAnimal,etNombre,etDescripcion,etTelefono,etCiudad,etPais;
    Button btnPublicar;
    ImageView foto_gallery;
    private DatabaseReference mRef;
    private Task<Void> mDatabase;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE= 2;
    private String userChoosenTask;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference mReference;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopciones_anadir);

        etTipoAnimal = (EditText) findViewById(R.id.etTipoAnimal);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        etCiudad = (EditText) findViewById(R.id.etCiudad);
        etPais = (EditText) findViewById(R.id.etPais);
        btnPublicar = (Button)findViewById(R.id.btnPublicar);

        foto_gallery = (ImageView)findViewById(R.id.foto_gallery);

        storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");

        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageOfAdoptedPet();
            }
        });
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeAdoptedPet();
            }
        });

    }

    private void writeAdoptedPet() {


        String nom =etNombre.getText().toString();
        String type = etTipoAnimal.getText().toString();
        String desc = etDescripcion.getText().toString();
        String telefono = etTelefono.getText().toString();
        String city = etCiudad.getText().toString();
        String pais=etPais.getText().toString();


        Adopcion adopcion = new Adopcion(type, nom, city, pais, desc, telefono);
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
        mDatabase = mRef.child("Adoption").child(nom).setValue(adopcion);

        CharSequence text = "Pet added.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    private void saveImageOfAdoptedPet() {

        final CharSequence[] items = { "Take a photo", "Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AdopcionesAnadir.this);
        builder.setTitle("Añade una foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String p1 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                @SuppressLint({"NewApi", "LocalSuppress"}) int result=getApplicationContext().checkSelfPermission(p1);

                if (items[item].equals("Take a photo")) {
                    userChoosenTask ="Take a photo";
                    if(result == 0)
                        cameraIntent();

                } else if (items[item].equals("Gallery")) {
                    userChoosenTask ="Gallery";
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take a photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        foto_gallery.setDrawingCacheEnabled(true);
        foto_gallery.buildDrawingCache();

        final Bitmap bitmap = (Bitmap) data.getExtras().get("data");


        String nom = etNombre.getText().toString();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] item = baos.toByteArray();

        mReference = storageRef.child(nom + ".jpg");
        UploadTask uploadTask = mReference.putBytes(item);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Context context = getApplicationContext();
                CharSequence text = "Picture not uploaded. Please try again.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                foto_gallery.setImageBitmap(bitmap);
                Context context = getApplicationContext();
                CharSequence text = "Image saved.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });


    }

    @SuppressLint("RestrictedApi")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] item = baos.toByteArray();

                String nom = etNombre.getText().toString();

                mReference = storageRef.child(nom + ".jpg");
                UploadTask uploadTask = mReference.putBytes(item);
                final Bitmap finalBm = bm;
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Context context = getApplicationContext();
                        CharSequence text = "Picture not uploaded. Please try again.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        foto_gallery.setImageBitmap(finalBm);
                        Context context = getApplicationContext();
                        CharSequence text = "Image saved.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        foto_gallery.setImageBitmap(bm);

    }

}

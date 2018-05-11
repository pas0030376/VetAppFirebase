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
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A placeholder fragment containing a simple view.
 */
public class addPetActivityFragment extends Fragment {

    EditText etName, etSpecies, etBreed, etGender, etBirthdate, etPeso, etEdad, etEsterilizado, etAlergias, etEnfermedades;
    Button btnSavePet;
    ImageView imgImageAddPet;

    private DatabaseReference mRef;
    private Task<Void> mDatabase;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE= 2;
    private String userChoosenTask;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String id = user.getUid().toString();


    StorageReference mReference;
    StorageReference storageRef;

    private View view;


    public addPetActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_add_pet, container, false);

        getActivity().setTitle("Añadir una mascota");

        etName = view.findViewById(R.id.nameAddPet);
        etSpecies = view.findViewById(R.id.species);
        etBreed = view.findViewById(R.id.breed);
        etGender = view.findViewById(R.id.genderAddPet);
        etBirthdate = view.findViewById(R.id.bdateAddPet);
        etEdad = view.findViewById(R.id.edad);
        etEsterilizado = view.findViewById(R.id.esterilizado);
        etPeso = view.findViewById(R.id.peso);
        etAlergias = view.findViewById(R.id.alergias);
        etEnfermedades = view.findViewById(R.id.enfermedades);
        btnSavePet = view.findViewById(R.id.savePet);
        imgImageAddPet = view.findViewById(R.id.imageAddPet);


        storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");

        imgImageAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveImageOfPet();
            }
        });

        btnSavePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               writeNewPet(id);

            }
        });

        return view;
    }

    private void writeNewPet(String id) {

        String nameAddPet =etName.getText().toString();
        String species = etSpecies.getText().toString();
        String breed = etBreed.getText().toString();
        String bdateAddPet = etBirthdate.getText().toString();
        String genderAddPet = etGender.getText().toString();
        String edad = etEdad.getText().toString();
        String peso = etPeso.getText().toString();
        String alergias = etAlergias.getText().toString();
        String enfermedades = etEnfermedades.getText().toString();
        String esterilizado = etEnfermedades.getText().toString();
        String imageAddPet=nameAddPet+".jpg";

        Pets pets = new Pets(nameAddPet, species, breed, bdateAddPet, genderAddPet, peso, edad, esterilizado, alergias, enfermedades);
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
        mDatabase = mRef.child(id).child(nameAddPet).setValue(pets);

        CharSequence text = "Pet added.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), text, duration);
        toast.show();


    }

    private void saveImageOfPet() {
        final CharSequence[] items = { "Take a photo", "Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Añade una foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String p1 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                @SuppressLint({"NewApi", "LocalSuppress"}) int result=getContext().checkSelfPermission(p1);

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
        intent.setAction(Intent.ACTION_GET_CONTENT);//
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
            else if (requestCode == REQUEST_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgImageAddPet.setImageBitmap(imageBitmap);
            }
        }


    }

    private void onCaptureImageResult(Intent data) {

        imgImageAddPet.setDrawingCacheEnabled(true);
        imgImageAddPet.buildDrawingCache();

        final Bitmap bitmap = (Bitmap) data.getExtras().get("data");


        String nom = etName.getText().toString();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] item = baos.toByteArray();

        mReference = storageRef.child(id+"_"+nom + ".jpg");
        UploadTask uploadTask = mReference.putBytes(item);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Context context = getContext();
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
                imgImageAddPet.setImageBitmap(bitmap);
                Context context = getContext();
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

                String nom = etName.getText().toString();


                mReference = storageRef.child(id+"_"+nom + ".jpg");
                UploadTask uploadTask = mReference.putBytes(item);
                final Bitmap finalBm = bm;
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Context context = getContext();
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
                        imgImageAddPet.setImageBitmap(finalBm);
                        Context context = getContext();
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

        imgImageAddPet.setImageBitmap(bm);

    }




}

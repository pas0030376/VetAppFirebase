package com.example.rachel.vetApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class addPetActivityFragment extends Fragment {

    EditText etName, etSpecies, etBreed, etGender, etBirthdate, etPeso, etEdad, etEsterilizado, etAlergias, etEnfermedades;
    Button btnSavePet;
    ImageView foto_gallery;
    private Uri contentURI;

    private DatabaseReference mRef;
    private Task<Void> mDatabase;
    private static final String IMAGE_DIRECTORY = "/vetApp";
    private int GALLERY = 1, CAMERA = 2;

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
        foto_gallery = view.findViewById(R.id.imageAddPet);


        storageRef = storage.getReferenceFromUrl("gs://vetapp-98f0d.appspot.com/");

        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();
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
        String image = id+nameAddPet;

        Pets pets = new Pets(nameAddPet, species, breed, bdateAddPet, genderAddPet, peso, edad, esterilizado, alergias, enfermedades,image);
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vetapp-98f0d.firebaseio.com/");
        mDatabase = mRef.child(id).child(nameAddPet).setValue(pets);
        if(contentURI != null) {
            String nameImage = id+nameAddPet+".jpg";
            StorageReference childRef = storageRef.child(nameImage);
            UploadTask uploadTask = childRef.putFile(contentURI);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Carga exitosa", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Carga fallida -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show();
        }




        CharSequence text = "Pet added.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), text, duration);
        toast.show();
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this.getContext());
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
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(this.getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();//***
                    foto_gallery.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this.getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            foto_gallery.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(this.getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(this.getContext(),
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

    /*
    private void onCaptureImageResult(Intent data) {

        foto_gallery.setDrawingCacheEnabled(true);
        foto_gallery.buildDrawingCache();

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
                foto_gallery.setImageBitmap(bitmap);
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
                        foto_gallery.setImageBitmap(finalBm);
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

        foto_gallery.setImageBitmap(bm);

    }*/

}

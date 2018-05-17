package com.example.rachel.vetApp;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static java.security.AccessController.getContext;

public class DetallesAdopcion extends AppCompatActivity {
Adopcion adopcion=null;
TextView tvNombre,tvTipo,tvDescripcion,tvTelefono,tvRefugio,tvCiudad,tvPais,tvEmail;
ImageView imagenAdopcion;
ImageButton btnLlamar;
private final int CALL_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_adopcion);

        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvTipo = (TextView) findViewById(R.id.tvTipo);
        tvDescripcion = (TextView) findViewById(R.id.tvDescripcion);
        tvTelefono = (TextView) findViewById(R.id.tvTelefono);
        tvRefugio = (TextView) findViewById(R.id.tvRefugio);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvCiudad = (TextView) findViewById(R.id.tvCiudad);
        tvPais = (TextView) findViewById(R.id.tvPais);
        imagenAdopcion = (ImageView) findViewById(R.id.imagenAdopcion);
        btnLlamar = (ImageButton) findViewById(R.id.btnLlamar);

        //recogemos el objeto adopcion entero
        adopcion = (Adopcion) getIntent().getExtras().getSerializable("adopcion");

        tvNombre.setText(adopcion.getNombre());
        tvTipo.setText(adopcion.getTipoAnimal());
        tvDescripcion.setText(adopcion.getDesc());
        tvTelefono.setText(adopcion.getTelefono());
        tvRefugio.setText(adopcion.getRefugio());
        tvEmail.setText(adopcion.getEmail());
        tvCiudad.setText(adopcion.getCiudad());
        tvPais.setText(adopcion.getPais());
        Glide.with(getApplicationContext()).load(adopcion.getUrl()).into(imagenAdopcion);

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoConfirmar(); //mostramos un cuadro de diálogo con las opciones llamar o cancelar

            }
        });
    }

    private void dialogoConfirmar() {

        final PrettyDialog dialog = new PrettyDialog(this);
        dialog.setTitle("Atención")
                .setMessage("¿Desea realizar una llamada a " + adopcion.getRefugio() + " ?")
                .addButton(
                        "Llamar",     // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_green,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                aceptar();
                            }
                        }
                )
                .addButton(
                        "Cancelar",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_red,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        }
                );
        dialog.show();
    }

    public void aceptar() {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + adopcion.getTelefono()));
            startActivity(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                aceptar();
            }
            else
            {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }








}

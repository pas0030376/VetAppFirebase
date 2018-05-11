package com.example.rachel.vetApp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Bundle parametros = new Bundle();
    FirebaseAuth mAuth;
    Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/vetapp-98f0d.appspot.com/o/noimage.png?alt=media&token=c1a1e275-1e37-4374-8cf2-be1e0b0dd2ad");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //we inflate the header view as it is not inflated yet.
        NavigationView navigationViewHeader = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationViewHeader.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.tvName);
        ImageView profilePhoto=(ImageView)hView.findViewById(R.id.profilePhoto);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        nav_user.setText(user.getDisplayName());

        if (user.getPhotoUrl() != null){
            Log.w("Google result",user.getPhotoUrl().toString());
        Glide.with(getApplicationContext())
                .load(user.getPhotoUrl())
                .into(profilePhoto);
        }
        else
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(profilePhoto);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_cita) {
            Intent intent = new Intent(getApplicationContext(),CitasActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_adopcion) {
            Intent intent = new Intent(getApplicationContext(),AdopcionesListado.class);
            startActivity(intent);
        } else if (id == R.id.nav_vetsMap) {
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_perfilMascota) {
            Intent intent = new Intent(getApplicationContext(), PerfilMascotaActivity.class);
            intent.putExtras(parametros);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(getApplicationContext(), addVacunasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logOut) {
            signOut();
            /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);*/
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //sign out user
    private void signOut() {

        mAuth.signOut();
        AuthUI.getInstance()
                .signOut(Navigation.this)
                .addOnCompleteListener(new OnCompleteListener<Void>(){

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        FirebaseAuth.getInstance().signOut();
                        CharSequence text = "Signed out.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                });


    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int width = 0;
        int height = 0;

        //hacemos que la imagen sea cuadrada
            if(bitmap.getWidth() < bitmap.getHeight()){
                width = bitmap.getWidth();
                height = bitmap.getWidth();
            } else {
                width = bitmap.getHeight();
                height =bitmap.getHeight();
            }


        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect;
        rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        //hacemos que la foto sea redonda
        final float roundPx = 360;

       paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect,paint);

        return output;
    }

}

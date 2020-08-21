package com.example.salesman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,mainfragment.maininterface,profileframgent.profileinterface {


    ImageView navimage;
    TextView navname;
    TextView navmail;


    public Toolbar toolbar;
    public DrawerLayout drawer;
    public NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headview = navigationView.getHeaderView(0);


        navimage = headview.findViewById(R.id.nav_profile_image);
        navname = headview.findViewById(R.id.navname);
        navmail = headview.findViewById(R.id.navmail);



        displayselecteditem(R.id.nav_Home);



        SharedPreferences preferences = getSharedPreferences("Profile",MODE_PRIVATE);
        String image = preferences.getString("image", "Default");
        String name = preferences.getString("name", "Default");
        String mail = preferences.getString("mail", "Default");

        navimage.setImageBitmap(decodeBase64(image));
        navname.setText(name);
        navmail.setText(mail);


    }

    @Override
    protected void onStart() {
        super.onStart();





        checkpass checkpass = new checkpass(getApplicationContext());
        checkpass.iftrue();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_about) {


            return true;
        }
        if (id == R.id.nav_Homesetting) {

            Fragment fragment = null;
            fragment = new mainfragment();

            if(fragment != null)
            {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main,fragment);
                ft.commit();


            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void displayselecteditem(int id) {
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_Home:
                fragment = new mainfragment();
                break;

            case R.id.nav_profile:
                fragment = new profileframgent();
                break;

            case R.id.nav_contact:
                fragment = new contact_frag();
                break;


            case R.id.nav_location:
                Intent mapactivity = new Intent(MainActivity.this,MapLocationActivity.class);
                startActivity(mapactivity);

                break;

            case R.id.nav_logout:
                SharedPreferences preferences = getSharedPreferences("Profile",MODE_PRIVATE);
                preferences.edit().remove("name").apply();
                preferences.edit().remove("mail").apply();
                preferences.edit().remove("address").apply();
                preferences.edit().remove("phone").apply();
                preferences.edit().remove("gender").apply();
                preferences.edit().remove("city").apply();
                preferences.edit().remove("image").apply();

                checkpass checkpass = new checkpass(getApplicationContext());
                checkpass.dofalse();


                Intent intent = new Intent(this,login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;


        }


        if(fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main,fragment);
            ft.commit();


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

        @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
            displayselecteditem(id);
        return true;
    }

    @Override
    public void onmain() {

    }

    @Override
    public void onprofile() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

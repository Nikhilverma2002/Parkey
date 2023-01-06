package com.ontech.com.parkey;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawer;
    TextView add_vehicle;
    LinearLayout scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarTransparent();


        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.navView);
        drawer = findViewById(R.id.drawer);
        add_vehicle = findViewById(R.id.add);
        setSupportActionBar(toolbar);
        scanner = findViewById(R.id.scanner);

       // navView = findViewById(R.id.navView);
        scanner.setOnClickListener(v->{
            MainActivity.this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new QRScanner(), "list_announcement")
                    .commit();
        });

        //set default home fragment and its title
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, new add_vehicle()).commit();
        navView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.main_color));
        toggle.syncState();

        add_vehicle.setOnClickListener(v->{
             MainActivity.this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new add_vehicle(), "list_announcement")
                    .commit();
        });
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new add_vehicle();
                        drawer.closeDrawer(GravityCompat.START);
                       // callFragment(fragment);
                        break;

                    case R.id.nav_contactUs:
                        fragment = new add_vehicle();
                        drawer.closeDrawer(GravityCompat.START);
                        //callFragment(fragment);
                        break;

                    case R.id.nav_aboutUs:
                        fragment = new add_vehicle();
                        drawer.closeDrawer(GravityCompat.START);
                        //  getSupportActionBar().setTitle("About US");
                        //callFragment(fragment);
                        break;

                }
                return true;
            }
        });



    }

    private void callFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setStatusBarTransparent () {
        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

}


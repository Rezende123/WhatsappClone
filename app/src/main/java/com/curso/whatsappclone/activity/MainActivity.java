package com.curso.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.adapter.TabAdapter;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.services.SlidingTabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        configToolbar();

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.str_layout);
        viewPager = (ViewPager) findViewById(R.id.vp_page);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_singnOut:
                singnOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void configToolbar() {
        toolbar.setTitle("Whatsapp");
        setSupportActionBar(toolbar);
    }

    private void singnOut() {
        auth = FirebaseConfig.getFirebaseAuth();
        auth.signOut();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

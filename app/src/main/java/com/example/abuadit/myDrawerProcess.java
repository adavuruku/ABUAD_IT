package com.example.abuadit;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;

public class myDrawerProcess {
//    public void initNavigationDrawer() {
//        navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                Intent intent;
//                int id = menuItem.getItemId();
//                drawerLayout.closeDrawers();
//                switch (id){
//                    case R.id.about:
//                        intent = new Intent(getApplicationContext(), about.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        finish();
//                        break;
//                }
//                drawerLayout.closeDrawers();
//                Toast.makeText(getApplicationContext(),"Copied", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
//            @Override
//            public void onDrawerClosed(View v){
//                super.onDrawerClosed(v);
//            }
//
//            @Override
//            public void onDrawerOpened(View v) {
//                super.onDrawerOpened(v);
//            }
//        };
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//
//    }
}

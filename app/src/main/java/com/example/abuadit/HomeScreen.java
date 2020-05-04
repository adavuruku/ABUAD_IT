package com.example.abuadit;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class HomeScreen extends AppCompatActivity implements itoffice.OnFragmentInteractionListener,
        notice.OnFragmentInteractionListener, profile.OnFragmentInteractionListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences MyId;
    String userID,DrawerFullname,DrawerEmail;
    Toolbar toolbar;
    dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setSaveEnabled(true);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);
        userID = MyId.getString("MyId", "");
        //retrieve student information
        dbHelper = new dbHelper(getApplicationContext());


        initNavigationDrawer();
    }
    public void initNavigationDrawer() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                drawerLayout.closeDrawers();
                switch (id) {
                    case R.id.about:
                        intent = new Intent(getApplicationContext(), about.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.appstatus:
                        intent = new Intent(getApplicationContext(), StudentCompany.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.register:
                        intent = new Intent(getApplicationContext(), studentAttendanceRecord.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.close:
                        SharedPreferences.Editor editor;
                        editor = MyId.edit();
                        editor.putString("MyId", "");
                        editor.apply();

                        intent = new Intent(getApplicationContext(), LoginOption.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        break;
                }
                return true;
            }
        });

        byte[] image_data=null;
        Cursor profpics = dbHelper.getAProfilePics(userID);
        if (profpics.getCount() >= 1) {
            profpics.moveToFirst();
            image_data = profpics.getBlob(profpics.getColumnIndex(dbColumnList.userProfilePics.COLUMN_PROFILEPICS));
        }
        profpics.close();

        profpics = dbHelper.getAStudent(userID);
        if (profpics.getCount() >= 1) {
            profpics.moveToFirst();
            DrawerEmail = profpics.getString(profpics.getColumnIndex(dbColumnList.abuadstudent.COLUMN_EMAIL));
            DrawerFullname = profpics.getString(profpics.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FULLNAME));
        }
        profpics.close();

        View header = navigationView.getHeaderView(0);
        TextView tv_email =  header.findViewById(R.id.email);
        TextView tv_fullname =  header.findViewById(R.id.uname);
        tv_email.setText(DrawerEmail);
        tv_fullname.setText(DrawerFullname);
        ImageView imageV = header.findViewById(R.id.profile_image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_data, 0, image_data.length);
        imageV.setImageBitmap(bitmap);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.about:
                intent = new Intent(getApplicationContext(), about.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.appstatus:
                intent = new Intent(getApplicationContext(), StudentCompany.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.register:
                intent = new Intent(getApplicationContext(), studentAttendanceRecord.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.close:
                SharedPreferences.Editor editor;
                editor = MyId.edit();
                editor.putString("MyId", "");
                editor.apply();

                intent = new Intent(getApplicationContext(), LoginOption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    profile profile = new profile();
                    profile.setRetainInstance(true);
                    return profile;
                case 1:
                    notice notice = new notice();
                    notice.setRetainInstance(true);
                    return notice;
                case 2:
                    itoffice itoffice = new itoffice();
                    itoffice.setRetainInstance(true);
                    return itoffice;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PROFILE";
                case 1:
                    return "NOTICE";
                case 2:
                    return "IT OFFICES";

            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
}

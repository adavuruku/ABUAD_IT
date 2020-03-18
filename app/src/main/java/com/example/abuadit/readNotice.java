package com.example.abuadit;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class readNotice extends AppCompatActivity {
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    TextView title, author, date;
    TextView body;
    dbHelper dbHelper;
    String noticeid, userID, DrawerFullname, DrawerEmail;
    private NavigationView navigationView;
    private SharedPreferences MyId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_notice);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        noticeid = getIntent().getStringExtra("NEWSID");
        DrawerFullname = getIntent().getStringExtra("FULLNAME");
        DrawerEmail = getIntent().getStringExtra("EMAIL");

        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        author = findViewById(R.id.author);
        body = findViewById(R.id.body);

        //retrieve info from sqlite
        dbHelper = new dbHelper(getApplicationContext());
        Cursor cursor = dbHelper.getANotice(noticeid);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            title.setText(cursor.getString(cursor.getColumnIndex(dbColumnList.abuadNotice.COLUMN_TITLE)));
            date.setText(cursor.getString(cursor.getColumnIndex(dbColumnList.abuadNotice.COLUMN_NOTICEDATE)));
            author.setText(cursor.getString(cursor.getColumnIndex(dbColumnList.abuadNotice.COLUMN_AUTHOR)));
            body.setText(cursor.getString(cursor.getColumnIndex(dbColumnList.abuadNotice.COLUMN_DESCRIPTION)));
        }

        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);
        userID = MyId.getString("MyId", "");

        initNavigationDrawer();
    }

    public void initNavigationDrawer() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                drawerLayout.closeDrawers();
                switch (id){
                    case R.id.about:
                        intent = new Intent(getApplicationContext(), about.class);
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
}

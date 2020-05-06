package com.example.abuadit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LoginOption extends AppCompatActivity {
    private SharedPreferences MyId,MyCompanyId,MyStaffId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);
        Button studLogin = findViewById(R.id.btnStudent);
        Button staffLogin = findViewById(R.id.btnStaff);
        Button companyLogin = findViewById(R.id.btnCompany);



        MyStaffId = this.getSharedPreferences("MyStaffId", this.MODE_PRIVATE);
        final String staffId= MyStaffId.getString("MyStaffId", "");
        staffLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (staffId.equals("")){
                    Intent intent = new Intent(getApplicationContext(), staffLogin.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }else{
                    Intent intent = new Intent(getApplication(), staffHome.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }

            }
        });

        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);
        final String userID = MyId.getString("MyId", "");
        studLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userID.equals("")){
                    Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }else{
                    Intent intent = new Intent(getApplication(), HomeScreen.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }

            }
        });

        MyCompanyId = this.getSharedPreferences("MyCompanyId", this.MODE_PRIVATE);
        final String MyCompany= MyCompanyId.getString("MyCompanyId", "");
        companyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyCompany.equals("")){
                    Intent intent = new Intent(getApplicationContext(), CompanyLogin.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }else{
                    Intent intent = new Intent(getApplicationContext(), companyHome.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

            }
        });
    }

    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loginmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                Intent intent = new Intent(getApplicationContext(), about.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.website:
                Intent i = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://abuadit.000webhostapp.com")
                );
                startActivity(i);
                break;
            case R.id.close:
                finish();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

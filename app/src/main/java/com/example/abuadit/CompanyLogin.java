package com.example.abuadit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CompanyLogin extends AppCompatActivity {

    Button login;
    TextView userid, password;
    ProgressDialog pd;
    AlertDialog.Builder builder;
//    String address = "http://192.168.1.64/abuadit/abuadrest.php";
    String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    String allResult, userName, userPassword, studentName;
    SharedPreferences MyCompanyId;
    public byte[] byteArray=null;
    private dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);
        Button login = findViewById(R.id.btnSave);
        userid  = findViewById(R.id.userid);
        password  = findViewById(R.id.password);

        MyCompanyId = this.getSharedPreferences("MyCompanyId", this.MODE_PRIVATE);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = userid.getText().toString().trim();
                userPassword = password.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)) {
                    displayMessage("Invalid Data's Provided - Please Verify");
                } else {
                    //login online
                    volleyJsonArrayRequest(address);
                }
            }
        });

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Processing Request ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
    }


    public void displayMessage(String msg) {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }


    public void volleyJsonArrayRequest(String url){
        if(pd.isShowing()){
            pd.cancel();
            pd.hide();
        }
        pd.show();
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (response.length()<=0){
                            displayMessage("Error: Wrong Username Or Password !!!");
                        }else{
                            allResult = response;
                            new ReadJSON().execute();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pd.isShowing()){
                            pd.hide();
                        }
                        displayMessage("Error: No Internet Connection !!!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "loginCompany");
                params.put("userID", userName);
                params.put("userPassword", userPassword);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }


    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                SharedPreferences.Editor editor;
                JSONObject jsonobject = new JSONObject(allResult);
                //must be arranged exact way it comes from server
                dbHelper = new dbHelper(getApplicationContext());

                studentName =  jsonobject.getString("companyName");

                dbHelper.SaveCompanyInformation(
                        jsonobject.getString("companyName"),jsonobject.getString("companyPhone"),
                        jsonobject.getString("companyEmail"),jsonobject.getString("companyAddress"),
                        jsonobject.getString("companyDescription"),jsonobject.getString("companyState"),
                        jsonobject.getString("companyLocalGov"),jsonobject.getString("companyId")
                );


                //userID
                editor = MyCompanyId.edit();
                editor.putString("MyCompanyId",jsonobject.getString("companyId"));
                editor.apply();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(pd.isShowing()){
                    pd.cancel();
                    pd.hide();
                }
                Toast.makeText(getApplicationContext(),"Welcome "+ studentName + " To ABUAD IT - MOBILE APP",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), companyHome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                displayMessage("Error: No Internet Connection !!!");
            }

            super.onPostExecute(s);
        }
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
        Intent intent;
        switch (id) {
            case R.id.about:
                intent = new Intent(getApplicationContext(), about.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.close:
                SharedPreferences.Editor editor;
                editor = MyCompanyId.edit();
                editor.putString("MyCompanyId", "");
                editor.apply();
                intent = new Intent(getApplicationContext(), LoginOption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

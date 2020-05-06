package com.example.abuadit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {
    Button login;
    TextView userid, password;
    ProgressDialog pd;
    AlertDialog.Builder builder;
//    String address = "http://192.168.1.64/abuadit/abuadrest.php";
    String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    String allResult, userName, userPassword, studentName;
    SharedPreferences MyId;
    public byte[] byteArray=null;
    private dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Button login = findViewById(R.id.btnSave);
        userid  = findViewById(R.id.userid);
        password  = findViewById(R.id.password);

        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);


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
        if(pd.isShowing()){
            pd.hide();
            pd.cancel();
        }
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
                        if (response.length()<=2){
                            displayMessage("Error: Wrong Username Or Password !!!");
                        }else{
                            allResult = response;
//                            Toast.makeText(getApplicationContext(), response ,Toast.LENGTH_LONG).show();
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
                params.put("opr", "login");
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

                studentName = jsonobject.getString("studName");

                //empty all existing student record
                dbHelper.deleteStudent();
                dbHelper.saveStudentInformation(
                        jsonobject.getString("regno"),jsonobject.getString("studName"),
                        jsonobject.getString("studFaculty"),jsonobject.getString("studDept"),
                        jsonobject.getString("studPhone"),jsonobject.getString("studEmail"),
                        jsonobject.getString("contactAddress"),jsonobject.getString("gender"),
                        jsonobject.getString("degree"),jsonobject.getString("mode"),
                        jsonobject.getString("itState"),jsonobject.getString("itLgov"),
                        jsonobject.getString("itLevel")
                );

                dbHelper.deleteLeturer();
                dbHelper.saveLecturerInformation(
                        jsonobject.getString("staffid"),jsonobject.getString("lecturerName"),
                        jsonobject.getString("lecturerFaculty"),jsonobject.getString("lecturerDept"),
                        jsonobject.getString("lecturerPhone"),jsonobject.getString("lecturerEmail"),
                        jsonobject.getString("staffaddress")
                );

                dbHelper.deleteItInformation();
                dbHelper.saveITInformation(
                        jsonobject.getString("regno"),jsonobject.getString("startDate"),
                        jsonobject.getString("endDate"),jsonobject.getString("duration"),
                        jsonobject.getString("staffid"),jsonobject.getString("companyId")
                );

                dbHelper.deleteCompany();
                dbHelper.SaveCompanyInformation(
                        jsonobject.getString("companyName"),jsonobject.getString("companyPhone"),
                        jsonobject.getString("companyEmail"),jsonobject.getString("companyAddress"),
                        jsonobject.getString("companyDescription"),jsonobject.getString("companyState"),
                        jsonobject.getString("companyLocalGov"),jsonobject.getString("companyId")
                );
                String MyPics ="https://abuadit.000webhostapp.com/resource/"+jsonobject.getString("profilePics");
                Bitmap bitmap =  Picasso.get().load(MyPics).get();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

                dbHelper.deleteProfilePics();
                dbHelper.saveProfilePics(jsonobject.getString("regno"),
                        byteArray );

                //empty all other preferences
                //userID
                editor = MyId.edit();
                editor.putString("MyId",jsonobject.getString("regno"));
                editor.apply();

                getApplicationContext().getSharedPreferences("MyStaffId",0).edit().clear().apply();
                getApplicationContext().getSharedPreferences("MyCompanyId",0).edit().clear().apply();


            } catch (JSONException | IOException e) {
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
//                Toast.makeText(getApplicationContext(),"Welcome "+ studentName + " To ABUAD IT - MOBILE APP",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), HomeScreen.class);
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
            case R.id.close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.abuadit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class listStaffITStudent extends Fragment {
    private List<myModels.studentModel> allNoticeList;
    private static int SPLASH_TIME_OUT = 500;//0.5seconds
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String fullname, email,comingNews;
    private boolean isConnected = false;
    RecyclerView recyclerView;
    private studentAdapter recyclerAdapter;
    String search, allResult,userID,companyID;
    ProgressBar progressBar;
    dbHelper dbHelper;
    byte[] byteArray;
    private SharedPreferences MyStaffId;
    ArrayList<myModels.companyModel> noticeList;
    //    String address = "http://192.168.1.64/abuadit/abuadrest.php";
    String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    private OnFragmentInteractionListener mListener;

    public listStaffITStudent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_staff_itstudent, container, false);


        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        progressBar =  rootView.findViewById(R.id.simpleProgressBar);

        allNoticeList = new ArrayList<>();

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyJsonArrayRequest(address);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        MyStaffId = getActivity().getSharedPreferences("MyStaffId", getActivity().MODE_PRIVATE);
        userID = MyStaffId.getString("MyStaffId", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                volleyJsonArrayRequest(address);
            }
        },SPLASH_TIME_OUT);

        pd = new ProgressDialog(getContext());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Processing Request ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(true);

        return rootView;
    }


    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim() == ""){
                            new LoadLocalData().execute();
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
                        new LoadLocalData().execute();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "loadStaffITStudents");
                params.put("MyStaffId", userID);
                return params;
            }
        };
        AppSingleton.getInstance(getContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                dbHelper = new dbHelper(getContext());
                allNoticeList.clear();
                JSONArray jsonarray = new JSONArray(allResult);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    dbHelper.saveStudentInformation(
                            jsonobject.getString("regno"),jsonobject.getString("studName"),
                            jsonobject.getString("studFaculty"),jsonobject.getString("studDept"),
                            jsonobject.getString("studPhone"),jsonobject.getString("studEmail"),
                            jsonobject.getString("contactAddress"),jsonobject.getString("gender"),
                            jsonobject.getString("degree"),jsonobject.getString("mode"),
                            jsonobject.getString("itState"),jsonobject.getString("itLgov"),
                            jsonobject.getString("itLevel")
                    );

                    if(!jsonobject.getString("companyId").equals("")){
                        dbHelper.SaveCompanyInformation(
                                jsonobject.getString("companyName"),jsonobject.getString("companyPhone"),
                                jsonobject.getString("companyEmail"),jsonobject.getString("companyAddress"),
                                jsonobject.getString("companyDescription"),jsonobject.getString("companyState"),
                                jsonobject.getString("companyLocalGov"),jsonobject.getString("companyId")
                        );
                    }

                    dbHelper.saveITInformation(
                            jsonobject.getString("regno"),
                            jsonobject.getString("startDate"),
                            jsonobject.getString("endDate"),
                            jsonobject.getString("duration"),
                            userID,
                            jsonobject.getString("companyId")
                    );

                    String MyPics ="https://abuadit.000webhostapp.com/resource/"+jsonobject.getString("profilePics");
                    Bitmap bitmap =  Picasso.get().load(MyPics).get();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    dbHelper.saveProfilePics(jsonobject.getString("regno"),
                            byteArray );


                        myModels.studentModel studentModel = new myModels().new studentModel(
                                jsonobject.getString("studName"),
                                jsonobject.getString("studFaculty"),
                                jsonobject.getString("studDept"),
                                jsonobject.getString("itLevel"),
                                jsonobject.getString("itState"),
                                jsonobject.getString("itLgov"),
                                jsonobject.getString("gender"),
                                jsonobject.getString("studEmail"),
                                jsonobject.getString("studPhone"),
                                jsonobject.getString("regno"),
                                byteArray,
                                jsonobject.getString("mode"),
                                jsonobject.getString("degree"),
                                jsonobject.getString("contactAddress"),"",
                                jsonobject.getString("companyId")

                        );

                        allNoticeList.add(studentModel);

                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            loadData();
            super.onPostExecute(s);
        }
    }

    //load local no internet connection
    class LoadLocalData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                dbHelper = new dbHelper(getContext());
                allNoticeList.clear();
                Cursor allCompany = dbHelper.getAllStaffITStudent(userID);
                if (allCompany.getCount() > 0) {
                    while (allCompany.moveToNext()) {

                        Cursor aStudent = dbHelper.getAStudent(
                                allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_REGNO))
                        );
                        if (aStudent.getCount() > 0) {
                            aStudent.moveToFirst();

                            //get student picture
                            Cursor studPics = dbHelper.getAProfilePics(allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_REGNO)));
                            if (studPics.getCount() > 0) {
                                studPics.moveToFirst();
                                byteArray = studPics.getBlob(studPics.getColumnIndex(dbColumnList.userProfilePics.COLUMN_PROFILEPICS));
                            }
                            studPics.close();

                            myModels.studentModel studentModel = new myModels().new studentModel(
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FULLNAME)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FACULTY)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_DEPARTMENT)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_LEVEL)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_STATE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_LGOV)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_GENDER)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_EMAIL)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_PHONE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_REGNO)),
                                    byteArray,
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_MODE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_DEGREE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_CONTACTADD)),"",
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_COMPANYID))
                            );
                            allNoticeList.add(studentModel);
                        }
                    }
                }
            } finally { }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            loadData();
            super.onPostExecute(s);
        }
    }


    public void loadData(){
        recyclerAdapter = new studentAdapter( allNoticeList, getContext(), new studentAdapter.OnItemClickListener() {
            @Override
            public void onNameClick(View v, int position) {

            }

            @Override
            public void onMarkClick(View v, int position) {

            }

            @Override
            public void onImageClick(View v, int position) {
                View snackView = getLayoutInflater().inflate(R.layout.dialogview, null);

                ImageView imv = snackView.findViewById(R.id.profile_pic);
                Bitmap bitmap = BitmapFactory.decodeByteArray(allNoticeList.get(position).getProfilePic(), 0,allNoticeList.get(position).getProfilePic().length);
                imv.setImageBitmap(bitmap);

                TextView myvi = snackView.findViewById(R.id.txtUser);
                TextView pname = snackView.findViewById(R.id.pname);

                myvi.setText(allNoticeList.get(position).getStudName());
                pname.setText(allNoticeList.get(position).getStudName());

                TextView pfaculty = snackView.findViewById(R.id.pfaculty);
                pfaculty.setText(
                        "Faculty Of "+allNoticeList.get(position).getStudFaculty() + " / "+ allNoticeList.get(position).getStudDept()
                );

                TextView pregno = snackView.findViewById(R.id.pregno);
                pregno.setText(
                        allNoticeList.get(position).getRegno() + " / "+ allNoticeList.get(position).getStudLevel() + " Level"
                );

                TextView pemailphone = snackView.findViewById(R.id.pemailphone);
                pemailphone.setText(
                        allNoticeList.get(position).getStudEmail() + " / "+ allNoticeList.get(position).getStudPhone()
                );

                TextView pmodetype = snackView.findViewById(R.id.pmodetype);
                pmodetype.setText(
                        allNoticeList.get(position).getMode() + " / "+ allNoticeList.get(position).getDegree()
                );

                TextView pstate = snackView.findViewById(R.id.pstate);
                pstate.setText(
                        allNoticeList.get(position).getItState() + " State / "+ allNoticeList.get(position).getItLgov()
                );

                TextView pcontact = snackView.findViewById(R.id.pcontact);
                pcontact.setText(
                        allNoticeList.get(position).getContactAddress()
                );
                //get Company Information for dialog
                dbHelper = new dbHelper(getContext());
                Cursor cur =dbHelper.getACompany(allNoticeList.get(position).getAppStatus());

                TextView pitphoneemail = snackView.findViewById(R.id.pitphoneemail);
                TextView pitaddress = snackView.findViewById(R.id.pitaddress);
                TextView pit = snackView.findViewById(R.id.pit);
                if(cur.getCount()>0){
                    cur.moveToFirst();
                    pitphoneemail.setText(
                            cur.getString(cur.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYPHONE)) + " / " +
                                    cur.getString(cur.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYEMAIL))
                    );
                    pitaddress.setText(
                            cur.getString(cur.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYADDRESS))
                    );
                    pit.setText(
                            cur.getString(cur.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYNAME))
                    );
                }else{
                    pitphoneemail.setVisibility(View.GONE);
                    pitaddress.setVisibility(View.GONE);
                    pit.setVisibility(View.GONE);
                }


                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setCanceledOnTouchOutside(true);
                d.setContentView(snackView);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.show();

            }

        });
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

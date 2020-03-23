package com.example.abuadit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class studentapplication extends Fragment {


    private OnFragmentInteractionListener mListener;

    public studentapplication() {
    }

    private List<myModels.companyModel> allNoticeList;
    private static int SPLASH_TIME_OUT = 500;//0.5seconds
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String fullname, email,comingNews;
    private boolean isConnected = false;
    RecyclerView recyclerView;
    private companyAdapter recyclerAdapter;
    String search, allResult,userID,companyID;
    ProgressBar progressBar;
    dbHelper dbHelper;
    private SharedPreferences MyId;
    ArrayList<myModels.companyModel> noticeList;
//    String address = "http://192.168.1.64/abuadit/abuadrest.php";
String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_studentapplication, container, false);


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

        MyId = getActivity().getSharedPreferences("MyId", getActivity().MODE_PRIVATE);
        userID = MyId.getString("MyId", "");

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

    public void displayMessage(String msg) {
        if(pd.isShowing()){
            pd.cancel();
            pd.hide();
        }
        builder = new AlertDialog.Builder(getContext());
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
                params.put("opr", "loadStudentApplication");
                params.put("userId", userID);
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

                    dbHelper.saveApplication(
                            jsonobject.getString("userId"),
                            jsonobject.getString("companyId"),
                            jsonobject.getString("appStatus")
                    );

                    myModels.companyModel companyList = new myModels().new companyModel(
                            jsonobject.getString("companyDescription"),
                            jsonobject.getString("companyAddress"),
                            jsonobject.getString("companyState"),
                            jsonobject.getString("companyLocalGov"),
                            jsonobject.getString("companyPhone"),
                            jsonobject.getString("companyEmail"),
                            jsonobject.getString("companyName"),
                            jsonobject.getString("companyId"),
                            jsonobject.getString("appStatus")
                    );

                    allNoticeList.add(companyList);
                }

            } catch (JSONException e) {
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

    class LoadLocalData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                dbHelper = new dbHelper(getContext());
                allNoticeList.clear();
                Cursor application = dbHelper.getAllStudentApplication(userID);
                if (application.getCount() > 0) {
                    while (application.moveToNext()) {
                        Cursor allCompany = dbHelper.getACompany(application.getString(application.getColumnIndex(dbColumnList.applicationList.COLUMN_REGNO)));
                        if (allCompany.getCount() > 0) {
                            allCompany.moveToFirst();
                            myModels.companyModel noticeList = new myModels().new companyModel(
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYDESCRIPTION)),
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYADDRESS)),
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYSTATE)),
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYLGOV)),
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYPHONE)),
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYEMAIL)),
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYNAME)),
                                    allCompany.getString(allCompany.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYID)),
                                    application.getString(application.getColumnIndex(dbColumnList.applicationList.COLUMN_ACCEPTSTATUS))
                            );

                            allNoticeList.add(noticeList);
                        }
                        allCompany.close();
                    }
                }
                application.close();
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
        recyclerAdapter = new companyAdapter( allNoticeList, getContext(), new companyAdapter.OnItemClickListener() {
            @Override
            public void onNameClick(View v, int position) {
                companyID =  allNoticeList.get(position).getCompanyId();
                if (pd.isShowing()){
                    pd.cancel();
                    pd.hide();
                }
                pd.show();
                volleyApplicationRequest(address);
            }
        });
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
    }


    public void volleyApplicationRequest(String url){
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (pd.isShowing()){
                            pd.cancel();
                            pd.hide();
                        }
                        if (response.trim()==""){
                            displayMessage("Fail To Accept Offer. Retry !");
                        }else{
                            dbHelper.saveApplication(userID,companyID,"2");
                            displayMessage("You Have Successfully Accept The Offer. !");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pd.isShowing()){
                            pd.cancel();
                            pd.hide();
                        }
                        displayMessage("Fail To Accept Offer. Retry !");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "updateApplication");
                params.put("companyID", companyID);
                params.put("userId", userID);
                return params;
            }
        };
        AppSingleton.getInstance(getContext()).addToRequestQueue(postRequest, REQUEST_TAG);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

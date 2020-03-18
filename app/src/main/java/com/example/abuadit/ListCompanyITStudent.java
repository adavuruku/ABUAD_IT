package com.example.abuadit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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


public class ListCompanyITStudent extends Fragment {
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
    byte[] byteArray;
    private SharedPreferences MyId;
    ArrayList<myModels.companyModel> noticeList;
    String address = "http://192.168.1.64/abuadit/abuadrest.php";
    private OnFragmentInteractionListener mListener;

    public ListCompanyITStudent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_company_itstudent, container, false);


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
                params.put("opr", "loadCompanyApplication");
                params.put("companyId", userID);
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

                    /**
                    * Save to abuadApplication
                     * Save to abuadStudents
                     * so any time you pic from abuadApplication  u can as well pick from
                     * abuadStudents
                     *
                    * */

                    dbHelper.saveApplication(
                            jsonobject.getString("regno"),
                            jsonobject.getString("companyId"),
                            jsonobject.getString("appStatus")
                    );

                    dbHelper.saveStudentInformation(
                            jsonobject.getString("regno"),jsonobject.getString("studName"),
                            jsonobject.getString("studFaculty"),jsonobject.getString("studDept"),
                            jsonobject.getString("studPhone"),jsonobject.getString("studEmail"),
                            jsonobject.getString("contactAddress"),jsonobject.getString("gender"),
                            jsonobject.getString("degree"),jsonobject.getString("mode"),
                            jsonobject.getString("itState"),jsonobject.getString("itLgov"),
                            jsonobject.getString("itLevel")
                    );

                    String MyPics = "http://192.168.1.64/abuadit/resource/"+jsonobject.getString("profilePics");

                    Bitmap bitmap =  Picasso.get().load(MyPics).get();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    dbHelper.saveProfilePics(jsonobject.getString("regno"),
                            byteArray );

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

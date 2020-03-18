package com.example.abuadit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class notice extends Fragment {

    private OnFragmentInteractionListener mListener;

    public notice() {
        // Required empty public constructor
    }

    private dbHelper dbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private List<myModels.notice> allNoticeList;
    private static int SPLASH_TIME_OUT = 500;//5seconds
    SwipeRefreshLayout mSwipeRefreshLayout;
    String fullname, email,comingNews;
    private boolean isConnected = false;
    RecyclerView recyclerView;
    private noticeAdapter recyclerAdapter;
    String search, allResult;
    ProgressBar progressBar;
    ArrayList<myModels.notice> noticeList;
    String address = "http://192.168.1.64/abuadit/abuadrest.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notice, container, false);

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                volleyJsonArrayRequest(address);
            }
        },SPLASH_TIME_OUT);

        return rootView;
    }


    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (response.length()<=0){
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
                params.put("opr", "loadnews");
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
                    dbHelper.saveNotice(
                            jsonobject.getString("noticeid"), jsonobject.getString("NoticeDescription"),
                            jsonobject.getString("author"), jsonobject.getString("title"),
                            jsonobject.getString("noticeDate"), jsonobject.getString("delStatus")
                    );
                    myModels.notice noticeList = new myModels().new notice(
                            jsonobject.getString("NoticeDescription"),
                            jsonobject.getString("title"),
                            jsonobject.getString("author"),
                            jsonobject.getString("noticeDate"),
                            jsonobject.getString("noticeid")
                    );

                    allNoticeList.add(noticeList);
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
                Cursor allnews = dbHelper.getAllNotice();
                if (allnews.getCount() > 0) {
                    while (allnews.moveToNext()) {

                        myModels.notice noticeList = new myModels().new notice(
                                allnews.getString(allnews.getColumnIndex(dbColumnList.abuadNotice.COLUMN_DESCRIPTION)),
                                allnews.getString(allnews.getColumnIndex(dbColumnList.abuadNotice.COLUMN_TITLE)),
                                allnews.getString(allnews.getColumnIndex(dbColumnList.abuadNotice.COLUMN_AUTHOR)),
                                allnews.getString(allnews.getColumnIndex(dbColumnList.abuadNotice.COLUMN_NOTICEDATE)),
                                allnews.getString(allnews.getColumnIndex(dbColumnList.abuadNotice.COLUMN_ID))
                        );

                        allNoticeList.add(noticeList);
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
    recyclerAdapter = new noticeAdapter( allNoticeList, getContext(), new noticeAdapter.OnItemClickListener() {
        @Override
        public void onNameClick(View v, int position) {
            String postid =  allNoticeList.get(position).getNoticeID();

//            Toast.makeText(getContext(),"Welcome "+ studentName + " To ABUAD IT - MOBILE APP",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), readNotice.class);
            intent.putExtra("NEWSID",postid);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            //getActivity().finish();
        }
    });
    recyclerAdapter.notifyDataSetChanged();
    recyclerView.setAdapter(recyclerAdapter);
    progressBar.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.GONE);
}
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof itoffice.OnFragmentInteractionListener) {
            mListener = (notice.OnFragmentInteractionListener) context;
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
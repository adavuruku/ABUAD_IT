package com.example.abuadit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class companies extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public companies() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private List<myModels.itfOffice> arrayliststat;
    private static int SPLASH_TIME_OUT = 500;//5seconds
    SwipeRefreshLayout mSwipeRefreshLayout;
    String fullname, email,comingNews;
    private boolean isConnected = false;
    RecyclerView recyclerView;
    private companyAdapter recyclerAdapter;
    String search;
    ProgressBar progressBar;
    ArrayList<myModels.itfOffice> newList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_itoffice, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        progressBar =  rootView.findViewById(R.id.simpleProgressBar);

        arrayliststat = new ArrayList<>();

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        },SPLASH_TIME_OUT);

        return rootView;
    }

    public void loadData(){
        arrayliststat.clear();
        String json;
        try {
            InputStream is = getActivity().getAssets().open("companydata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String (buffer,"UTF-8");
            JSONArray jsonarray = new JSONArray(json);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                myModels.itfOffice itflist = new myModels().new itfOffice(
                        jsonobject.getString("CompanyName"),
                        jsonobject.getString("ContactAdd"),
                        jsonobject.getString("State"),
                        jsonobject.getString("PhoneNo"),
                        jsonobject.getString("Email")
                );

                arrayliststat.add(itflist);

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        recyclerAdapter = new companyAdapter( arrayliststat, getContext(), new companyAdapter.OnItemClickListener() {
            @Override
            public void onNameClick(View v, int position) {
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
            mListener = (companies.OnFragmentInteractionListener) context;
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
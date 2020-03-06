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


public class itoffice extends Fragment implements SearchView.OnQueryTextListener{

    private OnFragmentInteractionListener mListener;

    public itoffice() {
        // Required empty public constructor
    }




    private List<myModels.itfOffice> arrayliststat;
    private static int SPLASH_TIME_OUT = 500;//5seconds
    SwipeRefreshLayout mSwipeRefreshLayout;
    String fullname, email,comingNews;
    private boolean isConnected = false;
    RecyclerView recyclerView;
    private itfOfficeAdapter recyclerAdapter;
    String search;
    ProgressBar progressBar;
    ArrayList<myModels.itfOffice> newList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
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
            InputStream is = getActivity().getAssets().open("itoffice.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String (buffer,"UTF-8");
            JSONArray jsonarray = new JSONArray(json);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                myModels.itfOffice itflist = new myModels().new itfOffice(
                        jsonobject.getString("AreaOffice"),
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
        recyclerAdapter = new itfOfficeAdapter( arrayliststat, getContext(), new itfOfficeAdapter.OnItemClickListener() {
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(getContext(),"Welcome To ATBU 37 NAMSN ELITE YEAR BOOK",Toast.LENGTH_LONG).show();
        newText = newText.toLowerCase();
        newList = new ArrayList<>();
        for(myModels.itfOffice  cont : arrayliststat){
            String name_ = cont.getState().toLowerCase();
            String depart_ = cont.getAreaOffice().toLowerCase();
            if(name_.contains(newText) || depart_.contains(newText)){
                newList.add(cont);
            }
        }
        recyclerAdapter.setFilter(newList);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_screen,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        super.onCreateOptionsMenu(menu,inflater);
        //return true;
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

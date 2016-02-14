package com.propulsion.rocketjobs;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ContentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomListAdapter mAdapter;
    private ArrayList<ListingItem> mMessages;
    private String mUsername =  Utils.getUsername();
    private String mRoomName;
    private JSONObject newData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.v("mainFragment", "create");

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.v("mainFragment", "start");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        final SwipeRefreshLayout refreshView = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        refreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mMessages = new ArrayList<>();

        final ListView lv1 = (ListView) refreshView.findViewById(R.id.custom_list);
        mAdapter = new CustomListAdapter(getContext(), mMessages);
        showDummyData();
        lv1.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("mainFragment", "paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("mainFragment", "stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("mainFragment", "destroyed");

    }

    private void showDummyData() {
        if (!Utils.isConnected) return;
        mMessages.add(makeDummyData("Receiptionist", "Playphone", "1234567890"));
        mMessages.add(makeDummyData("Cashier", "KFC", "0987654321"));
        mMessages.add(makeDummyData("Loading/Unloading", "FedEx", "0987654321"));
        mAdapter.notifyDataSetChanged();
    }

    public static ListingItem makeDummyData(String headline, String reporter, String date) {
        ListingItem dummy = new ListingItem();
        dummy.setHeadline(headline);
        dummy.setReporterName(reporter);
        dummy.setDate(date);
        return dummy;
    }
}

package com.propulsion.rocketjobs;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        mAdapter = new CustomListAdapter(getActivity(), mMessages);
        lv1.setAdapter(mAdapter);

        new GetJobsListTask(this).execute(Utils.SERVER_URL_JOBS);

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


    public void updateUi(JSONArray result) {
        if (result == null) {
            showDummyData();
            mAdapter.notifyDataSetChanged();
            return;
        }

        for (int i=0; i<result.length();i++) {
            JSONObject item = result.optJSONObject(i);

            String company = item.optString("company");
            String title = item.optString("title");
            String payment = item.optString("amount");
            String desc = item.optString("description");
            String status = item.optString("status");

            ListingItem lItem = createNewListingItem(title, desc, company, payment, status);
            mMessages.add(lItem);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showDummyData() {
        if (!Utils.isConnected) return;
        mMessages.add(createNewListingItem("Receiptionist", "Playphone", "Awesome job, check it out", "$60", "Available"));
        mMessages.add(createNewListingItem("Cashier", "KFC", "Don't work here", "$40", "Available"));
        mMessages.add(createNewListingItem("Loading/Unloading", "FedEx", "You will be working alongside ...", "70", "Unavailable"));
        mAdapter.notifyDataSetChanged();
    }

    public static ListingItem createNewListingItem(String jobTitle, String jobDesc, String company, String paymentInfo, String status) {
        ListingItem dummy = new ListingItem();
        dummy.setJobTitle(jobTitle);
        dummy.setCompanyName(company);
        dummy.setJobDesc(jobDesc);
        dummy.setPaymentInfo(paymentInfo);
        dummy.setStatus(status);
        return dummy;
    }

    public static ListingItem makeDummyData(String headline, String reporter, String desc, String payment) {
        ListingItem dummy = new ListingItem();
        dummy.setJobTitle(headline);
        dummy.setCompanyName(reporter);
        dummy.setJobDesc(desc);
        dummy.setPaymentInfo(payment);
        return dummy;
    }

    public class GetJobsListTask extends AsyncTask<String, Void, JSONArray> {

        Fragment callingFragment;

        public GetJobsListTask(Fragment f) {
            callingFragment = f;
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray json = null;
            String resultToDisplay = "";

            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    resultToDisplay += inputLine;
                in.close();

                json = new JSONArray(resultToDisplay);
            } catch (Exception e) {

            }
            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray result) {
            ContentFragment fragment = (ContentFragment) callingFragment;
            fragment.updateUi(result);
        }
    }
}

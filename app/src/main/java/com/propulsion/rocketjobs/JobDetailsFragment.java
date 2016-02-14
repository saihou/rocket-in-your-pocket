package com.propulsion.rocketjobs;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class JobDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "jobTitle";
    private static final String ARG_PARAM2 = "companyName";

    // TODO: Rename and change types of parameters
    private String jobTitle;
    private String companyName;

    private String TAG = "JobDetailsFragment";

    public JobDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jobTitle Parameter 1.
     * @param companyName Parameter 2.
     * @return A new instance of fragment JobDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobDetailsFragment newInstance(String jobTitle, String companyName) {
        JobDetailsFragment fragment = new JobDetailsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jobTitle);
        args.putString(ARG_PARAM2, companyName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobTitle = getArguments().getString(ARG_PARAM1);
            companyName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_details, container, false);

        WebView webview = (WebView) view.findViewById(R.id.webPageView);
        webview.setWebViewClient(new WebViewClient());
        //webview.loadUrl("https://www.google.com/search?q=give+me+a+job+now");
        webview.loadUrl("https://www.google.com");

        FloatingActionButton launchChat = (FloatingActionButton) view.findViewById(R.id.launch_chat);
        launchChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("company", companyName);
                Log.d("title", jobTitle);

                Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                chatIntent.putExtra("companyName", companyName);
                chatIntent.putExtra("jobTitle", jobTitle);
                getActivity().startActivity(chatIntent);
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

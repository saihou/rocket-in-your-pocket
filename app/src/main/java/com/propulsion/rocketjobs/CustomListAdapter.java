package com.propulsion.rocketjobs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Huiwen on 16/1/16.
 */
public class CustomListAdapter extends BaseAdapter {
    private ArrayList<ListingItem> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public CustomListAdapter(Activity aActivity, ArrayList<ListingItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aActivity.getApplicationContext());
        activity = aActivity;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.jobTitleName = (TextView) convertView.findViewById(R.id.title);
            holder.companyName = (TextView) convertView.findViewById(R.id.reporter);
            holder.tapDetailsButton = (TextView) convertView.findViewById(R.id.chatroom_btn);
            holder.jobId = (TextView) convertView.findViewById(R.id.jobId);
            holder.profilePicView = (ImageView) convertView.findViewById(R.id.profile_picture);
            holder.jobDesc = (TextView) convertView.findViewById(R.id.job_desc);
            holder.paymentInfo = (TextView) convertView.findViewById(R.id.payment_info);
            holder.statusButton = (ImageView) convertView.findViewById(R.id.status_button);
            holder.statusName = (TextView) convertView.findViewById(R.id.status_text);
            holder.tapChat = (TextView) convertView.findViewById(R.id.textView);
            holder.reporter = (TextView) convertView.findViewById(R.id.chatroom_btn);

            ImageView background = (ImageView) convertView.findViewById(R.id.imageView);
            setRandomBackground(background, position);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Resources res = activity.getResources();
        int resourceIdFemale = res.getIdentifier(
                "avatar_female", "drawable", activity.getApplicationContext().getPackageName() );

        holder.profilePicView.setImageResource(resourceIdFemale);
        holder.jobTitleName.setText(listData.get(position).getJobTitle());
        holder.companyName.setText(listData.get(position).getCompanyName());
        holder.jobId.setText(listData.get(position).getId());
        holder.paymentInfo.setText(CustomListAdapter.prependDollarSign((listData.get(position).getPaymentInfo())));
        holder.jobDesc.setText(listData.get(position).getJobDesc());

        holder.statusName.setText(CustomListAdapter.capitalize(listData.get(position).getStatus()));
        System.out.println(holder.statusName.getText().toString());
        int color = (holder.statusName.getText().toString().equalsIgnoreCase("Available")) ?
                                R.drawable.green : R.drawable.grey;
//                                (holder.statusName.getText().toString().equalsIgnoreCase("Busy")) ?
//                                R.drawable.yellow : R.drawable.grey;
        holder.statusButton.setImageResource(color);

        if (holder.statusName.getText().toString().equalsIgnoreCase("Unavailable")) {
            holder.tapChat.setVisibility(View.INVISIBLE);
            holder.reporter.setVisibility(View.INVISIBLE);
        } else {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String jobTitle = holder.jobTitleName.getText().toString().trim();
                    final String companyName = holder.companyName.getText().toString().trim();
                    final String jobDesc = holder.jobDesc.getText().toString().trim();

                    Intent launchChat = new Intent(activity, ChatActivity.class);
                    launchChat.putExtra("jobTitle", jobTitle);
                    launchChat.putExtra("companyName", companyName);
                    launchChat.putExtra("jobDesc", jobDesc);
                    activity.startActivity(launchChat);

//                    JobDetailsFragment fragment = JobDetailsFragment.newInstance(jobTitle, companyName);
//                    android.app.FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frame,fragment).addToBackStack(null).commit();
                }
            });
        }

        return convertView;
    }

    public static String prependDollarSign(final String line) {
        return "$" + line;
    }

    public static String capitalize(final String line) {
        if (line.length() > 0) {
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        }
        else {
            return line;
        }
    }

    public static void setRandomBackground(ImageView bg, int position) {
        int rdm = position%6;
        switch (rdm) {
            case 0:
                bg.setBackgroundResource(R.drawable.cafe);
                break;
            case 1:
                bg.setBackgroundResource(R.drawable.golden_gate);
                break;
            case 2:
                bg.setBackgroundResource(R.drawable.palace_of_finearts);
                break;
            case 3:
                bg.setBackgroundResource(R.drawable.ferry_building);
                break;
            case 4:
                bg.setBackgroundResource(R.drawable.houses);
                break;
            case 5:
                bg.setBackgroundResource(R.drawable.chinatown);
                break;
        }
    }

    static class ViewHolder {
        TextView jobTitleName;
        TextView companyName;
        TextView tapDetailsButton;
        TextView jobId;
        TextView jobDesc;
        TextView paymentInfo;
        ImageView profilePicView;
        ImageView statusButton;
        TextView statusName;
        TextView reporter;
        TextView tapChat;
    }
}
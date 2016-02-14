package com.propulsion.rocketjobs;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
            holder.reportedChatroomBtn = (TextView) convertView.findViewById(R.id.chatroom_btn);
            holder.jobId = (TextView) convertView.findViewById(R.id.jobId);
            holder.profilePicView = (ImageView) convertView.findViewById(R.id.profile_picture);

            ImageView background = (ImageView) convertView.findViewById(R.id.imageView);
            setRandomBackground(background, position);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity.getApplicationContext(), "HELLO", Toast.LENGTH_LONG).show();
                    JobDetailsFragment fragment = JobDetailsFragment.newInstance("", "");
                    android.app.FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment).addToBackStack("").commit();
                }
            });
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
        return convertView;
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
        TextView reportedChatroomBtn;
        TextView jobId;
        ImageView profilePicView;
    }
}
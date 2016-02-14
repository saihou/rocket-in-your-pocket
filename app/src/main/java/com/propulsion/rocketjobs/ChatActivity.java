package com.propulsion.rocketjobs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.magnet.max.android.User;
import com.magnet.mmx.client.api.ListResult;
import com.magnet.mmx.client.api.MMX;
import com.magnet.mmx.client.api.MMXChannel;
import com.magnet.mmx.client.api.MMXMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    public static final String TEMP_CHANNEL_NAME = "MyFirstChat";
    private ChatCustomListAdapter mAdapter;

    private MMXChannel channel;
    private MMX.EventListener eventListener;

    // TODO: edit these variables and get the value from Intent
    private String chatTitle = "from_detail"; // name of the other user of this private chat
    private String mRoomName; // the room for this private chat

    // TODO: change mUsername to facebook user name
    private String mUsername = Utils.getUsername();
    private ArrayList<ChatItem> mMessagesChat;
    private JSONObject newData;

    private String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().hide();

        // Search private channel
        MMXChannel.findPrivateChannelsByName(TEMP_CHANNEL_NAME, 10, 0, new MMXChannel.OnFinishedListener<ListResult<MMXChannel>>() {
            @Override
            public void onSuccess(ListResult<MMXChannel> result) {
                if (result.totalCount > 0) {
                    channel = result.items.get(0);
                    System.out.println(channel);
                    subscribeToMagnetChannel();
                } else {
                    //no channels found!
                    System.out.println("Cannot find!!");
                    //createNewMagnetChannel();
                }
            }

            @Override
            public void onFailure(MMXChannel.FailureCode failureCode, Throwable throwable) {
                Log.v(TAG, failureCode.toString());
            }
        });

        eventListener = new MMX.EventListener() {
                    public boolean onMessageReceived(MMXMessage message) {
                        MMXChannel msgChannel = message.getChannel();

                        String content = message.getContent().containsKey("message") ? message.getContent().get("message") : "";
                        ChatItem cItem = new ChatItem(content, message.getSender().getUserName());
                        mMessagesChat.add(cItem);
                        mAdapter.notifyDataSetChanged();
                        return false;
                    }
                };
        MMX.registerListener(eventListener);
// IMPORTANT: be sure to make the corresponding call to MMX.unregisterListener(eventListener) to prevent leaking the listener


        mUsername = Utils.getUsername();
        chatTitle = getIntent().getStringExtra("companyName");
        String topic = getIntent().getStringExtra("jobTitle");
        TextView intro = (TextView) findViewById(R.id.intro_to_chat);
        intro.setText("You are applying for the role of \"" + topic + "\"");
        TextView title = (TextView) findViewById(R.id.chat_title);
        title.setText(" " + chatTitle);

        mMessagesChat = new ArrayList<>();

        ListView lv = (ListView) findViewById(R.id.custom_list_chat);
        mAdapter = new ChatCustomListAdapter(getApplicationContext(), mMessagesChat);
        lv.setAdapter(mAdapter);
        final EditText post_reply = (EditText) findViewById(R.id.chat_text);

        FloatingActionButton sendBtn = (FloatingActionButton) findViewById(R.id.chat_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reply = post_reply.getText().toString();
                if (reply.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter a longer reply! Do not spam short replies", Toast.LENGTH_LONG);
                } else {

                    post_reply.setText("");
//                    mMessagesChat.add(ContentFragment.makeDummyData(reply, mUsername, "desc", "$1000"));
//                    mMessagesChat.add(ContentFragment.makeDummyData(reply, "I'm definitely not " + mUsername, "desc", "$500"));
//                    mAdapter.notifyDataSetChanged();

                    HashMap<String, String> content = new HashMap<>();
                    content.put("message", reply);
                    MMXMessage.Builder builder = new MMXMessage.Builder();
                    builder.channel(channel).content(content);
                    MMXMessage message = builder.build();
                    channel.publish(message, new MMXChannel.OnFinishedListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Log.d(TAG, "Message published successfully!");
                        }

                        @Override
                        public void onFailure(MMXChannel.FailureCode failureCode, Throwable throwable) {
                            Log.d(TAG, "Message publishing failed!");
                        }
                    });

                }
            }
        });
    }

    private void createNewMagnetChannel() {
        String name = TEMP_CHANNEL_NAME;
        String uid = User.getCurrentUserId();
        Set<String> userIds = new HashSet<>(10);
        userIds.add(uid);
        String summary = "Chat channel for myself";
        // Create the channel with predefined users
        MMXChannel.create(name, summary, false, MMXChannel.PublishPermission.SUBSCRIBER, userIds, new MMXChannel.OnFinishedListener<MMXChannel>() {
            @Override
            public void onSuccess(MMXChannel mmxChannel) {
                channel = mmxChannel;
                Log.d(TAG, "Successfully created channel " + channel.getName());
//                subscribeToMagnetChannel();
            }

            @Override
            public void onFailure(MMXChannel.FailureCode failureCode, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void subscribeToMagnetChannel() {
        channel.subscribe(new MMXChannel.OnFinishedListener<String>() {
            @Override
            public void onSuccess(String subId) {
                Log.d(TAG, "Successfully subscribed to " + channel.getName());
                Toast.makeText(getApplicationContext(), "Successfully subscribed to " + channel.getName(), Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(MMXChannel.FailureCode failureCode, Throwable throwable) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MMX.unregisterListener(eventListener);
    }
}

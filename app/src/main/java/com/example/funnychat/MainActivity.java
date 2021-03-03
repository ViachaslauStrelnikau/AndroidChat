package com.example.funnychat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.funnychat.adapters.ChannelsAdapter;
import com.example.funnychat.adapters.MessageAdapter;
import com.example.funnychat.adapters.UsersAdapter;
import com.example.funnychat.models.Channel;
import com.example.funnychat.models.Message;
import com.example.funnychat.models.User;
import com.example.funnychat.util.AssetsHandler;
import com.example.funnychat.widgets.AnimatedToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public int selfUserId = 0;
    private int selectedChannelID = 0;
    private int selectedUserId = 0;

    final ArrayList<Channel> channels = new ArrayList<>();
    final ArrayList<User> users = new ArrayList<>();
    final ArrayList<User> usersAll = new ArrayList<>();
    final ArrayList<Message> messages = new ArrayList<>();
    private final List<String> channelNames = new ArrayList<>();

    ChannelsAdapter channelsAdapter = null;
    UsersAdapter usersAdapter = null;
    MessageAdapter messageAdapter = null;
    ArrayAdapter<String> goToAdapter = null;
    AssetsHandler assetsHandler = null;

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(this.getClass().getSimpleName() + ":onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(this.getClass().getSimpleName() + ":onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println(this.getClass().getSimpleName() + ":onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selfUserId", selfUserId);
        outState.putInt("selectedChannelID", selectedChannelID);
        outState.putInt("selectedUserId", selectedUserId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // восстановление параметров--------
        if (savedInstanceState != null) {
            selfUserId = (int) savedInstanceState.get("selfUserId");
            selectedChannelID = (int) savedInstanceState.get("selectedChannelID");
            selectedUserId = (int) savedInstanceState.get("selectedUserId");
        }
        //-------------------------------------

        loadMainScreen();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (data != null) {
                selfUserId = data.getIntExtra("selfUserId", 0);
            }
            loadMainScreen();
        }
    }

    public void loadMainScreen() {
        if (selfUserId == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 0);
            return;
        }

        setContentView(R.layout.activity_main);

        assetsHandler = new AssetsHandler(this);

        channelsAdapter = new ChannelsAdapter(this, channels);
        usersAdapter = new UsersAdapter(this, users);
        messageAdapter = new MessageAdapter(this, messages);
        goToAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, channelNames);

        final AutoCompleteTextView goToView = findViewById(R.id.goto_field);
        final LinearLayout leftBlock = findViewById(R.id.left_block);
        final AnimatedToggleButton buttonMenu = findViewById(R.id.button_menu);
        final ImageButton sendButton = findViewById(R.id.send_button);
        final TextView selfName = findViewById(R.id.self_name);

        ListView channelList = findViewById(R.id.channel_list);
        ListView usersList = findViewById(R.id.user_list);
        ListView messagesList = findViewById(R.id.message_list);


        channelList.setAdapter(channelsAdapter);
        usersList.setAdapter(usersAdapter);
        goToView.setAdapter(goToAdapter);
        messagesList.setAdapter(messageAdapter);

        this.loadChannelList();
        this.loadUserList();
        this.loadMessageList();

        // устанавливаем имя пользователя под которым вошли
        for (User user : usersAll) {
            if (user.getId() == selfUserId) {
                selfName.setText(user.getName());
                break;
            }
        }

        //---------------Слушатели
        buttonMenu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                leftBlock.setVisibility(View.VISIBLE);
            } else {
                leftBlock.setVisibility(View.GONE);
            }
        });

        channelList.setOnItemClickListener((parent, view, position, id) -> {
            Channel channelItem = (Channel) parent.getItemAtPosition(position);
            setSelectedChannelID(channelItem.getId());
        });
        usersList.setOnItemClickListener((parent, view, position, id) -> {
            User user = (User) parent.getItemAtPosition(position);
            setSelectedUserId(user.getId());
        });

        goToView.setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            for (Channel channel : channels) {
                if (channel.getName().equals(item)) {
                    goToView.setText("");
                    setSelectedChannelID(channel.getId());
                    break;
                }
            }
        });

        sendButton.setOnClickListener((view) -> {
            sendMessage();
        });

        selfName.setOnClickListener(v -> {
            PopupMenu userMenu = new PopupMenu(this.getApplicationContext(), selfName);
            userMenu.getMenuInflater().inflate(R.menu.user_menu, userMenu.getMenu());
            userMenu.show();

            userMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.exit: {
                        selfUserId = 0;
                        finish();
                        startActivity(getIntent());
                    }
                    break;
                }

                return false;
            });
        });

    }

    public void loadChannelList() {
        JSONObject channelJsonObject = assetsHandler.parseJSONfile("channels.json");
        if (channelJsonObject != null) {
            try {
                JSONArray channelArray = channelJsonObject.getJSONArray("channels");
                channels.clear();

                for (int i = 0; i < channelArray.length(); i++) {
                    JSONObject channelObject = channelArray.getJSONObject(i);
                    Channel channel = new Channel(
                            channelObject.getInt("id"),
                            channelObject.getString("name"));

                    channel.setSelected(getSelectedChannelID() == channel.getId());
                    channels.add(channel);
                    channelNames.add(channel.getName());
                }
                channelsAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Cant get json array");
            }
        }
    }

    public void loadUserList() {
        JSONObject userJsonObject = assetsHandler.parseJSONfile("users.json");
        if (userJsonObject != null) {
            try {
                JSONArray userArray = userJsonObject.getJSONArray("users");
                users.clear();
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject userObject = userArray.getJSONObject(i);
                    User user = new User(
                            userObject.getInt("id"),
                            userObject.getInt("channelId"),
                            userObject.getString("name"),
                            userObject.getString("sex"),
                            userObject.getInt("age"));
                    user.setSelected(user.getId() == selectedUserId);
                    user.setLoggedIn(userObject.getBoolean("loggedIn"));
                    if (user.getId() == selfUserId) {
                        user.setLoggedIn(true);
                        user.setChannelId(selectedChannelID);
                    }

                    usersAll.add(user);

                    if (user.getChannelId() == getSelectedChannelID() || userObject.getInt("id") == 0) {
                        users.add(user);
                    }
                }
                usersAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Cant get json array");
            }

        }
    }

    public void loadMessageList() {
        JSONObject messageJsonObject = assetsHandler.parseJSONfile("messages.json");
        if (messageJsonObject != null) {
            try {
                JSONArray messageArray = messageJsonObject.getJSONArray("messages");
                messages.clear();
                for (int i = 0; i < messageArray.length(); i++) {
                    JSONObject messageObject = messageArray.optJSONObject(i);

                    if (messageObject.getInt("channelId") != selectedChannelID) {
                        continue;
                    } else {
                        if (selectedUserId != 0 && messageObject.getInt("ownUserId") != selectedUserId)
                            continue;
                    }

                    messages.add(new Message(
                            messageObject.getInt("id"),
                            messageObject.getInt("ownUserId"),
                            messageObject.getInt("channelId"),
                            messageObject.getInt("rcpUserId"),
                            messageObject.getLong("time"),
                            messageObject.getString("text")
                    ));

                }
                Collections.sort(messages, (o1, o2) -> (int) (o1.getTime() - o2.getTime()));
                messageAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Cant get json array");
            }
        }
    }

    public ArrayList<User> getUsersAll() {
        return usersAll;
    }

    public int getNewMessageId(int userId) {
        final List<Integer> myMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getOwnUserId() == userId) {
                myMessages.add(message.getId());
            }
        }
        Collections.sort(myMessages);
        if (myMessages.size() == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            final String stringId = stringBuilder.append(userId).append(1).toString();
            return Integer.parseInt(stringId);
        } else {
            return myMessages.get(myMessages.size() - 1) + 1;
        }
    }

    public void sendMessage() {
        final EditText stringInput = findViewById(R.id.message_field);
        if (stringInput.getText().toString().isEmpty()) {
            return;
        }
        final Message message = new Message(
                getNewMessageId(selfUserId),
                selfUserId,
                getSelectedChannelID(),
                selectedUserId,
                System.currentTimeMillis() / 1000L,
                stringInput.getText().toString()
        );
        messages.add(message);

        Collections.sort(messages, (o1, o2) -> (int) (o1.getTime() - o2.getTime()));
        messageAdapter.notifyDataSetChanged();
        stringInput.setText("");

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(stringInput.getWindowToken(), 0);
    }

    public int getSelectedChannelID() {
        return selectedChannelID;
    }

    public void setSelectedChannelID(int selectedChannelID) {
        this.selectedChannelID = selectedChannelID;
        this.setSelectedUserId(0);

        loadChannelList();
        loadUserList();
        loadMessageList();
    }

    public int getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(int selectedUserId) {
        this.selectedUserId = selectedUserId;
        loadUserList();
        loadMessageList();
    }
}
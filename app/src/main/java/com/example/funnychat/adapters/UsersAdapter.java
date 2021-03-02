package com.example.funnychat.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.funnychat.R;
import com.example.funnychat.models.User;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {
    private final LayoutInflater layoutInflater;

    public UsersAdapter(@NonNull Context context, @NonNull List<User> objects) {
        super(context, -1, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.user_list_item, parent, false);
        }
        final User user = getItem(position);
        final TextView userName = convertView.findViewById(R.id.user_name);
        final ImageView userPic = convertView.findViewById(R.id.user_state_image);
        userPic.setVisibility(View.VISIBLE);

        if (user.getSex().equals("male")) {
            if (user.isLoggedIn()) {
                userName.setTextColor(getContext().getResources().getColor(R.color.colorTextField));
                userPic.setBackgroundResource(R.drawable.ic_male_online);
            } else {
                userName.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                userPic.setBackgroundResource(R.drawable.ic_male_offline);
            }
        } else if (user.getSex().equals("female")) {
            if (user.isLoggedIn()) {
                userName.setTextColor(getContext().getResources().getColor(R.color.colorTextField));
                userPic.setBackgroundResource(R.drawable.ic_female_online);
            } else {
                userName.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                userPic.setBackgroundResource(R.drawable.ic_female_offline);
            }
        } else {
            userPic.setVisibility(View.INVISIBLE);
        }

        if (user.isSelected())
            userName.setTypeface(Typeface.DEFAULT_BOLD);
        else
            userName.setTypeface(Typeface.DEFAULT);

        userName.setText(user.getName());

        return convertView;
    }
}

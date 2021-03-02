package com.example.funnychat.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.funnychat.R;
import com.example.funnychat.models.Channel;

import java.util.List;

public class ChannelsAdapter  extends ArrayAdapter<Channel> {
    private final LayoutInflater layoutInflater;

    public ChannelsAdapter(@NonNull Context context, @NonNull List<Channel> objects) {
        super(context,-1,  objects);
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.channel_list_item,parent,false);
        }
        final Channel channel=getItem(position);
        final TextView channelName= convertView.findViewById(R.id.channel_name);
        channelName.setText(channel.getName());

        if(channel.isSelected())
            channelName.setTypeface(Typeface.DEFAULT_BOLD);

        else
            channelName.setTypeface(Typeface.DEFAULT);

        return convertView;
    }
}

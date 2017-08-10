package com.example.sidarta.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sidarta.myapplication.R;

import java.util.List;

/**
 * Created by sidarta on 04/08/17.
 */

public class FollowersListAdapter extends RecyclerView.Adapter<FollowersListAdapter.ViewHolder> {

    private List<String> mDataSet;

    public FollowersListAdapter(List<String> dataSet){
        mDataSet = dataSet;
    }

    public interface FollowerClickInterface{
        void followerClick(View view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follower_item, parent, false);

        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mFollowerName.setText(mDataSet.get(position));
        holder.mFollowerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FollowerClickInterface.followerClick(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mFollowerName;


        public ViewHolder(View v) {
            super(v);
            mFollowerName = v.findViewById(R.id.tvFollowerName);
        }
    }
}

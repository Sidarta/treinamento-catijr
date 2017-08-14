package com.example.sidarta.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sidarta.myapplication.R;
import com.example.sidarta.myapplication.domain.model.User;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

/**
 * Created by sidarta on 04/08/17.
 */

public class FollowersListAdapter extends RecyclerView.Adapter<FollowersListAdapter.ViewHolder> {

    private List<User> mDataSet;
    private FollowerClickInterface onclick;

    public FollowersListAdapter(List<User> dataSet, FollowerClickInterface onclick){
        mDataSet = dataSet;
        this.onclick = onclick;
    }

    public interface FollowerClickInterface{
        void followerClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follower_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { //pegadinha - nao usar final no parametro
        final int finalPosition = position;

        //name
        holder.mFollowerName.setText(mDataSet.get(position).getLogin());

        //image - using picasso
        Context context = holder.mAvatarPhoto.getContext();
        Picasso.with(context)
                .load(Uri.parse(mDataSet.get(position).getAvatar_url()))
                .into(holder.mAvatarPhoto);

        holder.mFollowerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.followerClick(view, finalPosition);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mFollowerName;
        AppCompatImageView mAvatarPhoto;

        ViewHolder(View v) {
            super(v);
            mFollowerName = v.findViewById(R.id.tvFollowerName);
            mAvatarPhoto = v.findViewById(R.id.ivAvatarPhoto);
        }
    }
}

package com.example.sidarta.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sidarta.myapplication.adapter.FollowersListAdapter;
import com.example.sidarta.myapplication.domain.GitHubAPI;
import com.example.sidarta.myapplication.domain.model.User;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersListActivity extends AppCompatActivity {

    private static final String TAG = FollowersListActivity.class.getSimpleName();

    @BindView(R.id.rvFollowersList) RecyclerView mFollowersList;

    private RecyclerView.Adapter mAdapter;

    private GitHubAPI mGitHubAPI;
    private SharedPreferences mSharedPrefs;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);

        ButterKnife.bind(this);

        mGitHubAPI = GitHubAPI.RETROFIT.create(GitHubAPI.class);
        mContext = this;

        mFollowersList.setLayoutManager(new LinearLayoutManager(this));
        mFollowersList.setHasFixedSize(true);

        //get shared prefs to get auth header
        mSharedPrefs = getSharedPreferences(getString(R.string.app_shared_prefs), MODE_PRIVATE);
        String authHeader = mSharedPrefs.getString(getString(R.string.app_shared_prefs_auth), "default");

        //get the dataset before adding the adapter
        mGitHubAPI.listFollowers(authHeader).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, final Response<List<User>> response) {
                if(response.isSuccessful()){
                    //adapter here since i have the dataset ready
                    mAdapter = new FollowersListAdapter(response.body(), new FollowersListAdapter.FollowerClickInterface() {
                        @Override
                        public void followerClick(View view, int position) {
                            //start new activity here
                            Log.d(TAG, response.body().get(position).getLogin());
                        }
                    });
                    mFollowersList.setAdapter(mAdapter);
                } else {
                    try {
                        String error = response.errorBody().string();
                        Snackbar.make(mFollowersList, error, Snackbar.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Snackbar.make(mFollowersList, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }


}

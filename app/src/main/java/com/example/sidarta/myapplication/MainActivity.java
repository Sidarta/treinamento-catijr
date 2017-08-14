package com.example.sidarta.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sidarta.myapplication.domain.GitHubAPI;
import com.example.sidarta.myapplication.domain.model.User;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tilUsername) TextInputLayout mTilUsername;
    @BindView(R.id.tilPassword) TextInputLayout mTilPassword;
    @BindView(R.id.btnLogin) Button mBtnLogin;

    private GitHubAPI mGitHubAPI;
    private SharedPreferences mSharedPrefs;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mGitHubAPI = GitHubAPI.RETROFIT.create(GitHubAPI.class);
        mSharedPrefs = getSharedPreferences(getString(R.string.app_shared_prefs), MODE_PRIVATE);
        mContext = this;
    }

    @OnClick(R.id.btnLogin)
    public void login(final View view){
        //on click action
        final String authHeader = Credentials.basic(mTilUsername.getEditText().getText().toString(), mTilPassword.getEditText().getText().toString());

        Call call = mGitHubAPI.login(authHeader);
        //Log.d(TAG, call.toString());

        mGitHubAPI.login(authHeader).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){

                    //armazenar no shared prefs
                    mSharedPrefs.edit().putString(getString(R.string.app_shared_prefs_auth), authHeader).apply();

                    //new intent
                    Intent intent = new Intent(mContext, FollowersListActivity.class);
                    startActivity(intent);

                } else {
                    try {
                        String error = response.errorBody().string();
                        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }

                Log.d(TAG, call.request().toString());
                Log.d(TAG, response.raw().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}

package com.example.sidarta.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sidarta.myapplication.domain.GitHubAPI;
import com.example.sidarta.myapplication.domain.GitHubOAuthAPI;
import com.example.sidarta.myapplication.domain.model.AccessToken;
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
    @BindView(R.id.btnOAuth) Button mBtnOAuth;

    private GitHubAPI mGitHubAPI;
    private GitHubOAuthAPI mGitHubOAuthAPI;
    private SharedPreferences mSharedPrefs;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mGitHubAPI = GitHubAPI.RETROFIT.create(GitHubAPI.class);
        mGitHubOAuthAPI = GitHubOAuthAPI.RETROFIT.create(GitHubOAuthAPI.class);
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

    @OnClick(R.id.btnOAuth)
    public void oauthLogin(View view){
        final String baseUrl = GitHubOAuthAPI.BASE_URL + "authorize";
        final String clientId = getString(R.string.oauth_client_id);
        final String redirectUri = this.getOAuthRedirectUri();

        /*
        https://github.com/login/oauth/authorize?client_id=XXXX&redirect_uri=http://sidarnet.curso.io
         */
        final Uri uri = Uri.parse(baseUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private String getOAuthRedirectUri() {
        /*
        http://sidarnet.curso.io
         */
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }

    @Override
    protected void onResume() {
        super.onResume();
        processOAuthRedirect(); //chama quando o app volta pro foco
    }

    private void processOAuthRedirect() {
        final Uri uri = getIntent().getData(); //pego o dado do intent que enviei
        if (uri != null && uri.toString().startsWith(this.getOAuthRedirectUri())) { //verifica se veio do link que passamos como redirect URI
            String code = uri.getQueryParameter("code");
            if (code != null) {
                //here we have secret, key and code
                String clientId = getString(R.string.oauth_client_id);
                String clientSecret = getString(R.string.oauth_client_secret);

                mGitHubOAuthAPI.accessToken(clientId, clientSecret, code).enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if(response.isSuccessful()){
                            //armazenar no shared prefs
                            AccessToken token = response.body();
                            mSharedPrefs.edit().putString(getString(R.string.app_shared_prefs_auth), token.getAuthCredential()).apply();

                            //new intent
                            Intent intent = new Intent(mContext, FollowersListActivity.class);
                            startActivity(intent);

                        } else {
                            try {
                                String error = response.errorBody().string();
                                Snackbar.make(mBtnOAuth, error, Snackbar.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Snackbar.make(mBtnOAuth, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

            } else if (uri.getQueryParameter("error") != null) {
                //TODO tratar erro no recebimento do code
            }

            // Limpar os dados para evitar chamadas múltiplas
            //ou seja, depois que processou, seta a data para null
            getIntent().setData(null);
        }
    }
}

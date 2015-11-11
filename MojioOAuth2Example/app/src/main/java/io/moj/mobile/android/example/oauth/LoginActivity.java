package io.moj.mobile.android.example.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.moj.mobile.android.sdk.auth.Environment;
import io.moj.mobile.android.sdk.auth.OAuthActivity;
import io.moj.mobile.android.sdk.auth.OAuthFragment;

public class LoginActivity extends AppCompatActivity {

    private static final int CONTENT_RES_ID = R.id.container_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String clientId = getString(R.string.client_id);
        String scope = getString(R.string.scope);

        String redirectUriScheme = getString(R.string.redirect_uri_scheme);
        String redirectUriHost = getString(R.string.redirect_uri_host);
        String redirectUri = getString(R.string.redirect_uri, redirectUriScheme, redirectUriHost);

        OAuthFragment f = OAuthFragment.newInstance(Environment.TRIAL, clientId, scope, redirectUri);
        getSupportFragmentManager().beginTransaction()
                .replace(CONTENT_RES_ID, f)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String accessToken = intent.getStringExtra(OAuthActivity.EXTRA_ACCESS_TOKEN);
        long expiresIn = intent.getLongExtra(OAuthActivity.EXTRA_EXPIRES_IN, 0);

        AuthenticatedFragment f = AuthenticatedFragment.newInstance(accessToken, expiresIn);
        getSupportFragmentManager().beginTransaction()
                .replace(CONTENT_RES_ID, f)
                .commit();
    }
}

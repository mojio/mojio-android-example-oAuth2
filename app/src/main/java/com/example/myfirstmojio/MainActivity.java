package com.example.myfirstmojio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import io.moj.mobile.android.sdk.MojioClient;
import io.moj.mobile.android.sdk.models.User;

public class MainActivity extends ActionBarActivity {

    //===========================================================================
    // Mojio App Setup
    // These values will match the keys given to you for your Mojio application in the
    // Mojio Developer panel.
    //===========================================================================
    private final static String MOJIO_APP_ID = "<YOUR_APP_ID>";
    private final static String REDIRECT_URL = "<YOUR_APP_REDIRECT>://"; // Example "myfirstmojio://"

    //===========================================================================
    // Activity properties
    //===========================================================================
    // Activity request ID to allow us to listen for the OAuth2 response
    private static int OAUTH_REQUEST = 0;

    // The main mojio client object; allows login and data retrieval to occur.
    private MojioClient mMojio;

    private User mCurrentUser;

    private Button mLoginButton;
    private TextView mUserName, mUserEmail;
    private ListView mVehicleList;

    //===========================================================================
    // Activity implementation
    //===========================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup mojio client with app keys.
        mMojio = new MojioClient(this, MOJIO_APP_ID, null, REDIRECT_URL);

        mUserName = (TextView)findViewById(R.id.user_name);
        mUserEmail = (TextView)findViewById(R.id.user_email);
        mVehicleList = (ListView)findViewById(R.id.vehicle_list);

        mLoginButton = (Button)findViewById(R.id.oauth2_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOauth2Login();
            }
        });
    }

    // IMPORTANT: This uses the com.mojio.mojiosdk.networking.OAuthLoginActivity class.
    // For this to work correctly, we must declare it as an Activity in our app's AndroidManifest.xml file.
    private void doOauth2Login() {
        // Launch the OAuth request; this will launch a web view Activity for the user enter their login.
        // When the Activity finishes, we listen for it in the onActivityResult method
        mMojio.launchLoginActivity(this, OAUTH_REQUEST);

    }

    // IMPORTANT: Must be overridden so that we can listen for the OAuth2 result and know if we were
    // logged in successfully. We do not have to bother with storing the auth tokens, the SDK codes that
    // for us.
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == OAUTH_REQUEST) {
            // We now have a stored access token
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                getCurrentUser(); // Now attempt to get user info
            }
            else {
                Toast.makeText(MainActivity.this, "Problem logging in", Toast.LENGTH_LONG).show();
            }
        }
    }

    // We have our access token stored now with the client, but we now need to grab our user ID.
    private void getCurrentUser() {
        String entityPath = "Users";
        HashMap<String, String> queryParams = new HashMap<>();

        mMojio.get(User[].class, entityPath, queryParams, new MojioClient.ResponseListener<User[]>() {
            @Override
            public void onSuccess(User[] result) {
                // Should have one result
                try {
                    mCurrentUser = result[0]; // Save user info so we can use ID later

                    // Show user data
                    mUserName.setText("Hello " + mCurrentUser.FirstName + " " + mCurrentUser.LastName);
                    mUserEmail.setText(mCurrentUser.Email);
                    mLoginButton.setVisibility(View.GONE);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Problem getting users", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MainActivity.this, "Problem getting users", Toast.LENGTH_LONG).show();
            }
        });
    }
}

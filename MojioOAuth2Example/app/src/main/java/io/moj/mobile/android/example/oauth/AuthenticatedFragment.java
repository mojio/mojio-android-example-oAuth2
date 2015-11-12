package io.moj.mobile.android.example.oauth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A sample fragment that displays auth token information.
 * Created by skidson on 15-11-10.
 */
public class AuthenticatedFragment extends Fragment {

    private static final String ARG_ACCESS_TOKEN = "ARG_ACCESS_TOKEN";
    private static final String ARG_EXPIRES_IN = "ARG_EXPIRES_IN";

    public static AuthenticatedFragment newInstance(String accessToken, long expiresIn) {
        Bundle args = new Bundle();
        args.putString(ARG_ACCESS_TOKEN, accessToken);
        args.putLong(ARG_EXPIRES_IN, expiresIn);

        AuthenticatedFragment f = new AuthenticatedFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_authenticated, container, false);
        ((TextView) root.findViewById(R.id.txt_access_token)).setText(getArguments().getString(ARG_ACCESS_TOKEN));
        ((TextView) root.findViewById(R.id.txt_expires_in)).setText(String.valueOf(getArguments().getLong(ARG_EXPIRES_IN, 0)));
        return root;
    }
}

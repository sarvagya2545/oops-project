package com.example.triggertracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.Arrays;
import java.util.List;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int AUTH_UI_REQUEST_CODE = 10001;
    private static final String TAG = "LOG_TAG";

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart:
                handleLoginRegister();
                break;
        }
    }

    private void handleLoginRegister() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAlwaysShowSignInMethodScreen(true)
                .setLogo(R.drawable.ic_google)
                .build();

        startActivityForResult(intent, AUTH_UI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DATA", "requestCode: " + requestCode + " resultCode: " + resultCode);
        if(requestCode == AUTH_UI_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                // Signed in user or new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.getEmail());

                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    Toast.makeText(this, "Welcome new user", Toast.LENGTH_SHORT).show();
                    String provider = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider();
//                    Toast.makeText(this, "Provider: " + provider, Toast.LENGTH_SHORT).show();

                    if(provider.equals("password")) {
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Email sent.");
                                        }
                                    }
                                });
                        Log.d(TAG, "Auth: password strategy");
                    }

                    if(user.isEmailVerified()) {
                        Intent profileIntent = new Intent(this, ProfileCreateActivity.class);
                        startActivity(profileIntent);
                    } else {
                        Intent emailVerifyIntent = new Intent(this, EmailVerifyActivity.class);
                        startActivity(emailVerifyIntent);
                    }
                } else {
                    Toast.makeText(this, "Welcome back again", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, UserActivity.class);
                    startActivity(intent);
                }
                this.finish();

            } else {
                // Sign in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if(response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request");
                } else {
                    Log.e(TAG, "onActivityResult: ", response.getError());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            Intent intent;
            if(user.isEmailVerified()) {
                intent = new Intent(this, UserActivity.class);
            } else {
                intent = new Intent(this, EmailVerifyActivity.class);
            }
            startActivity(intent);
            finish();
        }

        findViewById(R.id.btnStart).setOnClickListener(this);
    }
}
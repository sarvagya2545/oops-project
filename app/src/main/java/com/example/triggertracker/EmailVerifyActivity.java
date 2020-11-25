package com.example.triggertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class EmailVerifyActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        } else {
            user.reload();
            checkIfEmailVerified(user);

            findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.reload();
                    checkIfEmailVerified(user);
                    Toast.makeText(EmailVerifyActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                this.finish();
        }
        return true;
    }

    /**
     * Checks if the user is verified or not, and then returns him/her to a screen if the user is verified.
     * @param user
     */
    public void checkIfEmailVerified(FirebaseUser user) {
        if(user.isEmailVerified()) {
            Intent intent  = new Intent(this, UserActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "The user's email is still not verified", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {

                    }
                });
    }
}
package com.example.triggertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ExtraFeatureActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mWebsiteText, mLocationText, mMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_feature);

        mWebsiteText = findViewById(R.id.website_edit);
        mLocationText = findViewById(R.id.location_edit);
        mMessageText = findViewById(R.id.message_edit);

        findViewById(R.id.btnWeb).setOnClickListener(this);
        findViewById(R.id.btnLocation).setOnClickListener(this);
        findViewById(R.id.btnMessage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWeb:
                openWebsite();
                break;
            case R.id.btnLocation:
                openLocation();
                break;
            case R.id.btnMessage:
                openMessage();
                break;
            default:
                break;
        }
    }

    private void openMessage() {
        String msg = mMessageText.getText().toString();
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share this text with: ")
                .setText(msg)
                .startChooser();
    }

    private void openLocation() {
        String loc = mLocationText.getText().toString();
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }

    private void openWebsite() {
        String url = mWebsiteText.getText().toString();
        Uri website = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, website);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("Implicit intents", "Can't handle the intent!");
        }
    }
}
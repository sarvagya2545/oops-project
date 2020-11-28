package com.example.triggertracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

public class AddShoppingItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editShopItemName;
    private NumberPicker quantityPicker;
    private String[] pickerVals = new String[] {"0","1","2","3","4","5","6","7","8","9","10"};
    private int qtyValue = 0;
    private Button btnAddItem;
    private ImageView uploadImage;
    private Bitmap imageBitmap;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private String TAG = "TAG";
    private int TAKE_IMAGE_CODE = 10101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_item);

        quantityPicker = findViewById(R.id.qtyPicker);
        btnAddItem = findViewById(R.id.btnAddNote);
        btnAddItem.setOnClickListener(this);
        findViewById(R.id.btnUpload).setOnClickListener(this);
        uploadImage = findViewById(R.id.uploadImage);

        quantityPicker.setMinValue(0);
        quantityPicker.setMaxValue(10);
        quantityPicker.setDisplayedValues(pickerVals);

        quantityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                qtyValue = quantityPicker.getValue();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnAddNote:
                addItem();
                break;
            case R.id.btnUpload:
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        if(imageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(imageIntent, "Select file to upload"), TAKE_IMAGE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_IMAGE_CODE) {
            if(resultCode == RESULT_OK) {
                Uri imageData = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    uploadImage.setImageURI(imageData);
                    imageBitmap = handleUpload(bitmap);
                    uploadImage.setImageURI(imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap handleUpload(Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return bitmap;
    }

    private void addItem() {
        Log.d(TAG, "addItem: Reached here");
        editShopItemName = findViewById(R.id.editNoteTitle);
        String name = editShopItemName.getText().toString();
        Timestamp created = new Timestamp(new Date());
        int qty = qtyValue;
        ShoppingItem newItem = new ShoppingItem(name, created, qty, FirebaseAuth.getInstance().getCurrentUser().getUid());

        if(Pattern.matches("",name)) {
            editShopItemName.setError("Please enter something");
        } else {
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("ShopListItems")
                    .add(newItem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Added the item to firebase");
                            String docId = documentReference.getId();

                            StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                                    .child("images")
                                    .child("shoppingItemImages")
                                    .child(uid + "..." + docId + ".jpeg");

                            imageRef.putBytes(baos.toByteArray())
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Log.d(TAG, "onSuccess: Image uploaded");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: ", e.getCause());
                                        }
                                    });

                            Toast.makeText(AddShoppingItemActivity.this, "Added the item to database", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ", e);
                        }
                    });
        }
    }
}
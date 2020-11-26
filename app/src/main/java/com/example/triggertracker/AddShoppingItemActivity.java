package com.example.triggertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class AddShoppingItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editShopItemName;
    private NumberPicker quantityPicker;
    private String[] pickerVals = new String[] {"0","1","2","3","4","5","6","7","8","9","10"};
    private int qtyValue = 0;
    private Button btnAddItem;

    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_item);

        quantityPicker = findViewById(R.id.qtyPicker);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(this);

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
            case R.id.btnAddItem:
                addItem();
                break;
        }
    }

    private void addItem() {
        Log.d(TAG, "addItem: Reached here");
        editShopItemName = findViewById(R.id.editShopItemName);
        String name = editShopItemName.getText().toString();
        Timestamp created = new Timestamp(new Date());
        int qty = qtyValue;
        // FirebaseAuth.getInstance().getCurrentUser().getUid()
        ShoppingItem newItem = new ShoppingItem(name, created, qty, "123");

        FirebaseFirestore.getInstance()
                .collection("ShopListItems")
                .add(newItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Added the item to firebase");
                        Toast.makeText(AddShoppingItemActivity.this, "Added the item to database", Toast.LENGTH_SHORT).show();
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
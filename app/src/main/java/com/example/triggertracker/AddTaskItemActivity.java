package com.example.triggertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

public class AddTaskItemActivity extends AppCompatActivity implements View.OnClickListener {

    TextView dateTextView, timeTextView;
    EditText editShopItemName;
    Button btnAddDate, btnAddTime, btnAddItem;
    Calendar calendar, calendar1 = Calendar.getInstance();
    CheckBox checkBox;

    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_item);

        btnAddDate = findViewById(R.id.btnAddDate);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddTime = findViewById(R.id.btnAddTime);

        btnAddDate.setOnClickListener(this);
        btnAddTime.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);

        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        checkBox = findViewById(R.id.hasReminder);

        dateTextView.setVisibility(View.INVISIBLE);
        timeTextView.setVisibility(View.INVISIBLE);
        btnAddTime.setVisibility(View.INVISIBLE);
        btnAddDate.setVisibility(View.INVISIBLE);

        checkBox.setChecked(false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dateTextView.setVisibility(View.VISIBLE);
                    timeTextView.setVisibility(View.VISIBLE);
                    btnAddDate.setVisibility(View.VISIBLE);
                    btnAddTime.setVisibility(View.VISIBLE);
                } else {
                    dateTextView.setVisibility(View.INVISIBLE);
                    timeTextView.setVisibility(View.INVISIBLE);
                    btnAddTime.setVisibility(View.INVISIBLE);
                    btnAddDate.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnAddDate:
                addDate();
                break;
            case R.id.btnAddTime:
                addTime();
                break;
            case R.id.btnAddItem:
                addItem();
                break;
        }
    }

    private void addTime() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar1.set(Calendar.HOUR, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);
                calendar1.set(Calendar.SECOND, 0);

                CharSequence dateCharSeq = DateFormat.format("hh:mm a", calendar1);
                timeTextView.setText(dateCharSeq);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }

    private void addItem() {
        Log.d(TAG, "addItem: Reached here");

        editShopItemName = findViewById(R.id.editShopItemName);

        String name = editShopItemName.getText().toString();
        Timestamp created = new Timestamp(calendar.getTime());

        Timestamp reminderTime = new Timestamp(calendar1.getTime());

        boolean hasReminder = checkBox.isChecked();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Task created
        Task newTask = new Task(name, created, reminderTime, hasReminder, userId);

        // Firestore: push task to database
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .add(newTask)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Added the item to firebase");
                        Toast.makeText(AddTaskItemActivity.this, "Added the item to database", Toast.LENGTH_SHORT).show();
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

    private void addDate() {
        calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);

                CharSequence dateCharSeq = DateFormat.format("MMM dd, yyyy", calendar1);
                dateTextView.setText(dateCharSeq);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }
}
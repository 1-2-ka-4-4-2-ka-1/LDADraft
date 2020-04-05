package com.bankbros.ldaadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailsActivity extends AppCompatActivity {


    private TextView displayName;
    private TextView emailId;
    private TextView registerDate;
    private EditText noticeLimit;
    private TextView donationsAmount;
    private TextView enabledDisabled;

    private Switch switchEnable;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();



        displayName = findViewById(R.id.tv_display_name);
        emailId= findViewById(R.id.tv_email);
        registerDate= findViewById(R.id.tv_date);
        noticeLimit= findViewById(R.id.ed_notice_limit);
        donationsAmount= findViewById(R.id.tv_donations_amount);
        enabledDisabled= findViewById(R.id.tv_enabled);
        switchEnable= findViewById(R.id.swch_enable);


        Intent intent = getIntent();
        final RegisteredUsersModel usersModel = (RegisteredUsersModel) intent.getSerializableExtra("obj");

        displayName.setText(usersModel.getUserName());
        emailId.setText(usersModel.getUserEmail());
        registerDate.setText(usersModel.getDateRegistered());
        noticeLimit.setText(usersModel.getNoticeLimit());
        donationsAmount.setText(usersModel.getDonatios());

        if(usersModel.isUserEnabled())
        {
            enabledDisabled.setText("Enabled");
            enabledDisabled.setTextColor(Color.GREEN);
            switchEnable.setChecked(true);

        }else {
            enabledDisabled.setText("Dissabled");
            enabledDisabled.setTextColor(Color.RED);
            switchEnable.setChecked(false);
        }

        noticeLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                databaseReference.child("Admin").child("usersdata").child(usersModel.getUserId()).child("noticeLimit").setValue(s);
            }
        });


        switchEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    enabledDisabled.setText("Enabled");
                    enabledDisabled.setTextColor(Color.GREEN);

                    databaseReference.child("Admin").child("disabledUsers").child(usersModel.getUserId()).setValue(null);
                    databaseReference.child("Admin").child("usersdata").child(usersModel.getUserId()).child("userEnabled").setValue(true);
                }
                else {
                    enabledDisabled.setText("Dissabled");
                    enabledDisabled.setTextColor(Color.RED);

                    databaseReference.child("Admin").child("disabledUsers").child(usersModel.getUserId()).setValue(true);
                    databaseReference.child("Admin").child("usersdata").child(usersModel.getUserId()).child("userEnabled").setValue(false);
                }
            }
        });

    }


}

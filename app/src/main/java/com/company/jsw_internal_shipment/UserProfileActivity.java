package com.company.jsw_internal_shipment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {
    String username,name,mobile_no,yard_id,yard_name,email;
    TextView usernames,names,mobiles,yard_names,yardid,emails;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        username=getIntent().getStringExtra("username");
        name=getIntent().getStringExtra("name");
        mobile_no=getIntent().getStringExtra("mobile_number");
        yard_id=getIntent().getStringExtra("yard_id");
        yard_name=getIntent().getStringExtra("yard_name");
        email=getIntent().getStringExtra("email");
        usernames=findViewById(R.id.usernames);
        names=findViewById(R.id.name);
        mobiles=findViewById(R.id.mobile_no);
        yardid=findViewById(R.id.yard_id);
        yard_names=findViewById(R.id.yard_name);
        emails=findViewById(R.id.emails);
        try{
            usernames.setText(username);
            names.setText(name);
            emails.setText(email);
            mobiles.setText(mobile_no);
            yardid.setText(yard_id);
            yard_names.setText(yard_name);
            System.out.println("userdetails: "+username+name+email+mobile_no+yard_id+yard_name);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
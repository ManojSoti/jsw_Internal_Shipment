package com.company.jsw_internal_shipment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username, password;
    private Button mLogin;
    private RequestQueue mRequestQueue;
    private SharedPreferences mSharedPreferences;
    public static String baseurl;
    static String mainUrl;
    private ProgressDialog mProgressDialog;
    StringRequest stringRequest;
    String user;
    String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        if (!mSharedPreferences.getBoolean("LoginAppUser", false)) {
            openLoginPage();
        } else {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("name", mSharedPreferences.getString("name", "name"));
            i.putExtra("yard_id", mSharedPreferences.getString("yard_id", "yard_id"));
            i.putExtra("username", mSharedPreferences.getString("username", "username"));
            i.putExtra("password", mSharedPreferences.getString("password", "password"));
            i.putExtra("yard_name", mSharedPreferences.getString("yard_name", "yard_name"));
            startActivity(i);
        }



    }

    private void openLoginPage() {
        setContentView(R.layout.activity_login);
        initializeViews();
        mLogin.setOnClickListener(this);

    }

    private void initializeViews() {
        username = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        mLogin = findViewById(R.id.login_button);
        mRequestQueue = Volley.newRequestQueue(this);
        mProgressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                if (!username.getText().toString().trim().isEmpty()) {
                    username.setError(null);
                    if (!password.getText().toString().trim().isEmpty()) {
                        password.setError(null);
                        //  mainUrl ="http://"+mServerName.getText().toString().trim()+":1280/api/v1/login" ;
                        Log.d("username", username.getText().toString());
                        Log.d("password", password.getText().toString());
                        //  Log.d("loginurl",mainUrl);
                        callLoginApi(ApiLinks.loginUrl);

                    } else {
                        password.setError("You need to enter password");
                    }
                } else {
                    username.setError("You need to enter an Username");
                }
                break;

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }



    private void callLoginApi(String url) {
        mProgressDialog.setMessage("Verifying login details...");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);


        JSONObject jsonObject = new JSONObject();

        try {
            user= String.valueOf(EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G",username.getText().toString().trim()));
             pass= String.valueOf(EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G",password.getText().toString().trim()));
            System.out.println("Encrypted data user:"+user);
            System.out.println("Encrypted data pss:"+ pass);

           // String username=EncryptionDecryption.decrypt(user);
           // String password=EncryptionDecryption.decrypt(pass);
          //  System.out.println("decrypted data username: "+username);
          //  System.out.println("decrypted data password:"+ password);
            jsonObject.put("username", user);
            jsonObject.put("password",pass);
            System.out.println(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }*/

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    mProgressDialog.dismiss();
                    try {
                        Log.d("jsonobject", jsonObject.toString());
                        System.out.println("response: " + response);

                        if (response.getString("status").equals("success")) {
                            System.out.println("sucesss");
                            JSONObject profileData = response.getJSONObject("profile_data");

                            if (profileData != null) {
                                SharedPreferences.Editor editor = mSharedPreferences.edit();

                                String username=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",profileData.getString("username")));
                                System.out.println("usrname:"+username);
                                String mobile_no=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",profileData.getString("mobile_no")));
                               String name=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",profileData.getString("name")));
                              String email=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",profileData.getString("email_id")));
                               String yard_id=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",profileData.getString("yard_id")));
                               String yard_name=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",profileData.getString("yard_name")));
                                editor.putBoolean("LoginAppUser", true)
                                        .putString("username",username )
                                        .putString("mobile_no",mobile_no )
                                        .putString("name",name)
                                        .putString("email_id",email)
                                        .putString("yard_id",yard_id )
                                        .putString("yard_name", yard_name)
                                        .putString("user_name", user)
                                        .putString("password", pass)
                                        .commit();

                                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                               intent.putExtra("username",username);
                               intent.putExtra("mobile_number",mobile_no);
                               intent.putExtra("name",name);
                               intent.putExtra("email_id",email);
                               intent.putExtra("yard_id",yard_id);
                               intent.putExtra("yard_name",yard_name);
                               System.out.println("mobile:"+mobile_no);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Enter Correct Username and password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    mProgressDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            JSONObject obj = new JSONObject(res);
                            // Handle the error response
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(loginRequest);
    }
}
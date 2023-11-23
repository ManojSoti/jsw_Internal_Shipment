package com.company.jsw_internal_shipment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class BatchDetailsActivity extends AppCompatActivity {


    String username,name,mobile_no,yardid,yard_name,email,batchId;
    Toolbar toolbar;
    Button next;
    TextView batchDetails,toolbartitle;
    private RequestQueue mRequestQueue;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    BatchDetailsBean  batchDetailsBean=new BatchDetailsBean();
   static List<BatchDetailsBean> batchDetailsBeanList=new LinkedList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.batch_details_activity);
        mRequestQueue = Volley.newRequestQueue(this);
        mProgressDialog = new ProgressDialog(this);
        batchId=getIntent().getStringExtra("batchId");
        username=getIntent().getStringExtra("username");
        name=getIntent().getStringExtra("name");
        mobile_no=getIntent().getStringExtra("mobile_number");
        System.out.println("mobile:"+mobile_no);
        yardid=getIntent().getStringExtra("yard_id");
        yard_name=getIntent().getStringExtra("yard_name");
        email=getIntent().getStringExtra("email_id");
        toolbartitle=findViewById(R.id.toolbar_title);
        toolbartitle.setText("Welcome "+username);
        toolbar=findViewById(R.id.toolbar);
        next=findViewById(R.id.next_button);
        batchDetails=findViewById(R.id.batch_details);


        getBatchDetails(ApiLinks.batchDetailsUrl,batchId,username);



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
    public void getBatchDetails(String url,String batchId,String username){
        mProgressDialog.setMessage("Verifying Batch details...");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        JSONObject jsonObject=new JSONObject();
        try{
            String batchIds=String.valueOf(EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G",batchId));
            String load_unload=String.valueOf(EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G","1"));
            String usr=String.valueOf(EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G",username));
            jsonObject.put("batch_id",batchIds);
            jsonObject.put("load_unload",load_unload);
            jsonObject.put("supervisor",usr);
            System.out.println("getbtachdetails: "+jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest batchdetailsrequest=new JsonObjectRequest(Request.Method.POST,url,jsonObject,response -> {
            mProgressDialog.dismiss();
            try {
                Log.d("jsonobject", jsonObject.toString());
                System.out.println("response: " + response);

                if (response.getString("status").equals("success")) {
                    System.out.println("sucesss");
                    JSONObject batch_details = response.getJSONObject("batch_details");
                    System.out.println("batch details: " + batch_details);

                        String batch_id=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("batch_id")));
                        String batch_weight=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("batch_weight")));
                        String yard_id=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("load_yard_id")));
                        String load_yard=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("load_yard")));
                        String unload_yard=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("unload_yard")));
                        String unload_yard_id=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("unload_yard_id")));
                        String unloading_point=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("unloading_point")));
                      //  String unloading_yard=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("unloading_point")));
                        String customer=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("customer")));
                       String trip_id=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",batch_details.getString("trip_id")));
                       System.out.println("trip id: "+trip_id);
                        batchDetails.setText("Batch ID: "+batch_id
                        +"\n"
                                +"\n"
                        +"Batch Weight: "+batch_weight
                        +"\n"
                                +"\n"
                                +"Load_yard_ID: "+yard_id
                                +"\n"
                                +"\n"
                        +"Load Yard: "+load_yard
                        +"\n"
                                +"\n"
                        +"Unloading Yard: "+unload_yard
                        +"\n"
                                +"\n"
                                +"Unloading_yard_ID: "+unload_yard_id
                                +"\n"
                                +"\n"
                        +"Unloading Point: "+unloading_point
                        +"\n"
                                +"\n"
                        +"Customer: "+customer
                        +"\n"
                        +"\n"
                        +"Trip_id: "+trip_id);


                        batchDetailsBean.setBatchId(batchId);
                        batchDetailsBean.setBatchWeight(batch_weight);
                        batchDetailsBean.setLoadYardId(yard_id);
                        batchDetailsBean.setLoadYard(load_yard);
                        batchDetailsBean.setUnloadYard(unload_yard);
                        batchDetailsBean.setUnloadYardId(unload_yard_id);
                        batchDetailsBean.setUnloadingPoint(unloading_point);
                        batchDetailsBean.setCustomer(customer);
                        batchDetailsBean.setTripId(trip_id);
                        batchDetailsBeanList.add(batchDetailsBean);

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        Intent next =new Intent(BatchDetailsActivity.this,CreateTripActivity.class);
                            next.putExtra("username",username);
                            next.putExtra("mobile_number",mobile_no);
                            next.putExtra("name",name);
                            next.putExtra("yard_id",yardid);
                            next.putExtra("email_id",email);
                            next.putExtra("yard_name",yard_name);
                        startActivity(next);
                        }
                    });

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BatchDetailsActivity.this);


                    builder.setMessage(response.getString("message"));

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so dismiss dialog
                         onBackPressed();
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialogs = builder.create();
                    dialogs.show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> {
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
        batchdetailsrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       mRequestQueue.add(batchdetailsrequest);
    }


}

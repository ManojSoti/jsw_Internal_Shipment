package com.company.jsw_internal_shipment;

import static com.company.jsw_internal_shipment.BatchDetailsActivity.batchDetailsBeanList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BatchDetailsForwardActivity extends AppCompatActivity {
    String username,name,mobile_no,yardid,yard_name,email,batchId;
    Toolbar toolbar;
    Button next;
    List<UnitNameBean> yard_details = new LinkedList();
    UnitNameBean unitNameBean;
    String SelectedUnitNAme;
    TextView batchDetails,toolbartitle;
    private RequestQueue mRequestQueue;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    BatchDetailsBean  batchDetailsBean=new BatchDetailsBean();
    static List<BatchDetailsBean> batchDetailsBeanList=new LinkedList<>();;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_details_forward);
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
       // toolbartitle.setText("Welcome "+username);
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

        JsonObjectRequest batchdetailsrequest=new JsonObjectRequest(Request.Method.POST,url,jsonObject, response -> {
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
                            getYardNameType(ApiLinks.unloadingYardUrl,username);
                           /* Intent next =new Intent(BatchDetailsForwardActivity.this,CreateTripActivity.class);
                            next.putExtra("username",username);
                            next.putExtra("mobile_number",mobile_no);
                            next.putExtra("name",name);
                            next.putExtra("yard_id",yardid);
                            next.putExtra("email_id",email);
                            next.putExtra("yard_name",yard_name);
                            startActivity(next);*/
                        }
                    });

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BatchDetailsForwardActivity.this);


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

    public void getYardNameType(String url,String username){
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        yard_details.clear();
        JSONObject jsonObject=new JSONObject();
        try{
            String usr=String.valueOf(EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G",username));
            jsonObject.put("supervisor",usr);
            System.out.println("getyardnames: "+jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest locationJsonrequest=new JsonObjectRequest(Request.Method.POST,url,jsonObject,
                response -> {
                    try {
                        mProgressDialog.dismiss();
                        String status = response.getString("status");
                        System.out.println(status);
                        if (status.equals("success")) {
                            /*JSONObject details=response.getJSONObject("details");
                            String label=details.getString("label");*/
                            JSONArray arraylist=response.getJSONArray("yard_details");

                            for (int i=0;i<arraylist.length();i++){
                                JSONObject yardNameobject=arraylist.getJSONObject(i);
                                String unitNames=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",yardNameobject.getString("yard_name")));
                                // String location=locationobject.getString("location");
                                System.out.println(unitNames);
                                unitNameBean =new UnitNameBean();
                                //locationBean.setId(id);
                                unitNameBean.setYard_name(unitNames);

                                yard_details.add(unitNameBean);
                                System.out.println("unloading names inside method: "+yard_details);
                            }

                            showYardNameTypeSpinner();
                        }else {
                            // Handle error or display appropriate message
                            Toast.makeText(this,  response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },error -> {
            NetworkResponse networkResponse = error.networkResponse;
            if (error instanceof ServerError && networkResponse != null) {
                try {
                    String res = new String(networkResponse.data,
                            HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                    JSONObject obj = new JSONObject(res);
                    // Handle the error response
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer YourAuthTokenHere");
                return headers;
            }
        };

        locationJsonrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(locationJsonrequest);;
    }
    private void showYardNameTypeSpinner() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) BatchDetailsForwardActivity.this.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        final Dialog dialog = new Dialog(BatchDetailsForwardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box_spinner);
        dialog.getWindow().setLayout(((displayMetrics.widthPixels / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        ListView listView = dialog.findViewById(R.id.dialog_box_new_tag_reason_listView);
        TextView mealService = dialog.findViewById(R.id.mealSerViceDia);

        mealService.setText("Unloading Yards");
        System.out.println("yard...."+yard_details);
       UnloadingYardSpinnerAdapter tagReasonAdapter = new UnloadingYardSpinnerAdapter(BatchDetailsForwardActivity.this, yard_details);
        listView.setAdapter(tagReasonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // SelectedPositionIndexloc=position;
                SelectedUnitNAme=yard_details.get(position).getYard_name();

                /*final int which_item=position;
                new AlertDialog.Builder(BatchDetailsForwardActivity.this)

                        .setMessage("Are You Sure you want to create the Trip?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                (ApiLinks.createTripUrl,batchDetailsBeanList,"remarks","1",SelectedUnitNAme,username);

                            }
                        })
                        .setNegativeButton("No", null).show();*/


                dialog.dismiss();

            }
        });
        dialog.show();


    }
}
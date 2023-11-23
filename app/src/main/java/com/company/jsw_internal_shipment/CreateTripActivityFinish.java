package com.company.jsw_internal_shipment;

import static com.company.jsw_internal_shipment.BatchDetailsActivity.batchDetailsBeanList;
import static com.company.jsw_internal_shipment.CreateTripSourceBatchDetails.batchDetailsBeanLists;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

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
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateTripActivityFinish extends AppCompatActivity {
    ListView listViewDetails;
    BatchDetailsAdapter adapter;
    Button scanbarcode,createTrip;
    String username;
    String SelectedUnitNAme;
    private RequestQueue mRequestQueue;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    List<UnitNameBean> unitname=new LinkedList<>();
    UnitNameBean unitNameBean;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(this);
        mProgressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_create_trip_finish);
        username=getIntent().getStringExtra("username");
        //List<BatchDetailsBean> batchDetailsLists = (List<BatchDetailsBean>) getIntent().getSerializableExtra("batchDetailsList");

        listViewDetails=findViewById(R.id.listcreatetrips);
        scanbarcode=findViewById(R.id.scanbacode);
        createTrip=findViewById(R.id.createTrip);
        System.out.println("batch details: "+batchDetailsBeanLists);
        adapter=new BatchDetailsAdapter(this,batchDetailsBeanLists);
        listViewDetails.setAdapter(adapter);

        scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUnitNumber(ApiLinks.getUnitNameUrl);
            }
        });
        listViewDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item = position;

                new AlertDialog.Builder(CreateTripActivityFinish.this)
                        .setMessage("Are You sure you want to delete this Batch Id  "+batchDetailsBeanLists.get(position).getBatchId()+" ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove the item from the list

                                batchDetailsBeanLists.remove(which_item);
                                adapter.notifyDataSetChanged();
                                // Check if the list is empty
                            /*    if (batchDetailsBeanLists.size()==1) {
                                    // If the list is empty, navigate to FirstActivity
                                    batchDetailsBeanLists.remove(which_item);
                                    adapter.notifyDataSetChanged();
                                   //onBackPressed(); // Optional: Close the current activity if needed
                                }else{

                                }*/
                            }
                        })
                        .setNegativeButton("No", null).show();
              //  return true;
            }
        });



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
    public void onBackPressed() {
        Intent intent = new Intent(this, CreateTripActivitySource.class);
        intent.putExtra("username",username);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // This finishes the current activity (ActivityC)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            Intent i=new Intent(CreateTripActivityFinish.this,CreateTripActivitySource.class);
            i.putExtra("username",username);
            i.putExtra("code",result.getContents());
            startActivity(i);
        }
    });
    public void getUnitNumber(String url){
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        unitname.clear();
        JsonObjectRequest locationJsonrequest=new JsonObjectRequest(Request.Method.GET,url,null,
                response -> {
                    try {
                        mProgressDialog.dismiss();
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            /*JSONObject details=response.getJSONObject("details");
                            String label=details.getString("label");*/
                            JSONArray arraylist=response.getJSONArray("vehicles");

                            for (int i=0;i<arraylist.length();i++){
                                JSONObject locationobject=arraylist.getJSONObject(i);
                                String unitNames=String.valueOf(EncryptionDecryption.decrypt("SAVITHRU-x!A%D*G",locationobject.getString("unitname")));
                                // String location=locationobject.getString("location");

                                unitNameBean =new UnitNameBean();
                                //locationBean.setId(id);
                                unitNameBean.setUnitNAme(unitNames);

                                unitname.add(unitNameBean);
                                System.out.println("unit names inside method: "+unitname);
                            }

                            showUnitNameTypeSpinner();
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
    private void showUnitNameTypeSpinner() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) CreateTripActivityFinish.this.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        final Dialog dialog = new Dialog(CreateTripActivityFinish.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_box_spinner);
        dialog.getWindow().setLayout(((displayMetrics.widthPixels / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        ListView listView = dialog.findViewById(R.id.dialog_box_new_tag_reason_listView);
        TextView mealService = dialog.findViewById(R.id.mealSerViceDia);

        mealService.setText("Unit Names");
        UnitNameSpinnerAdapter tagReasonAdapter = new UnitNameSpinnerAdapter(CreateTripActivityFinish.this, unitname);
        listView.setAdapter(tagReasonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // SelectedPositionIndexloc=position;
                SelectedUnitNAme=unitname.get(position).getUnitNAme();

                final int which_item=position;
                new AlertDialog.Builder(CreateTripActivityFinish.this)

                        .setMessage("Your Trip has Multiple unloading yards,\n do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createTrip(ApiLinks.createTripUrl,batchDetailsBeanLists,"remarks","0",SelectedUnitNAme,username);

                            }
                        })
                        .setNegativeButton("No", null).show();


                dialog.dismiss();

            }
        });
        dialog.show();


    }
    public void createTrip(String url,List<BatchDetailsBean> batchdetails,String remarks,String load_unload,String vehicle,String supervisor){
        mProgressDialog.setMessage("Creating Trip...");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        JSONObject jsonObject=new JSONObject();
        try{
            JSONArray batchesArray = new JSONArray();
            for (BatchDetailsBean batch : batchdetails) {
                JSONObject batchObject = new JSONObject();
                batchObject.put("batch_id", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", batch.getBatchId()));
                batchObject.put("weight", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", batch.getBatchWeight()));
                batchObject.put("unloading_yard_id", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", batch.getUnloadYardId()));
                batchObject.put("loading_yard_id", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", batch.getLoadYardId()));
                batchesArray.put(batchObject);
            }
            jsonObject.put("batches", batchesArray);

            // Add other details to the main JSONObject
            jsonObject.put("remarks", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", remarks));
            jsonObject.put("load_unload", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", load_unload));
            jsonObject.put("vehicle_no", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", vehicle));
            jsonObject.put("supervisor", EncryptionDecryption.encrypt("SAVITHRU-x!A%D*G", supervisor));

            System.out.println("getbtachdetails: " + jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest createTriprequest=new JsonObjectRequest(Request.Method.POST,url,jsonObject,response -> {
            mProgressDialog.dismiss();
            try {
                Log.d("jsonobject", jsonObject.toString());
                System.out.println("response: " + response);

                if (response.getString("status").equals("success")) {
                    System.out.println("sucesss");
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateTripActivityFinish.this);


                    builder.setMessage(response.getString("message"));

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so dismiss dialog
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialogs = builder.create();
                    dialogs.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateTripActivityFinish.this);


                    builder.setMessage(response.getString("message"));

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so dismiss dialog
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
        createTriprequest.setRetryPolicy(new DefaultRetryPolicy(
                50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(createTriprequest);
    }
}
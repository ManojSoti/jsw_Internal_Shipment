package com.company.jsw_internal_shipment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class CreateTripActivitySource extends AppCompatActivity {
    Button btn_scan;
    ImageView imageView;
    EditText batchId;
    String username;
    SharedPreferences prefs;
    Toolbar toolbar;
    TextView toolbartile;
    String batchIDFromcompleteTrip;
    //  private DrawerLayout drawerLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip_source);


        // drawerLayout = findViewById(R.id.drawer_layout);
        //   NavigationView navigationView = findViewById(R.id.navigation_view);
        //   View headerView = navigationView.getHeaderView(0);
        username=getIntent().getStringExtra("username");
        btn_scan =findViewById(R.id.scanBarcode);
        imageView=findViewById(R.id.play_button);
        batchId=findViewById(R.id.batchId);
        toolbar=findViewById(R.id.toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        toolbar.setTitle("");
        toolbartile=findViewById(R.id.toolbar_title);
        //toolbartile.setText("Welcome "+username);
        //  this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        try{
            batchIDFromcompleteTrip=getIntent().getStringExtra("code");
            System.out.println("code: "+batchIDFromcompleteTrip);
            batchId.setText(batchIDFromcompleteTrip);
        }catch (Exception e){
            e.printStackTrace();
        }
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(batchId.getText().toString().trim().isEmpty()){
                    batchId.setError("Batch Id cannot be empty");
                }else{
                    Intent i=new Intent(CreateTripActivitySource.this,CreateTripSourceBatchDetails.class);
                    i.putExtra("batchId",batchId.getText().toString().trim());
                    i.putExtra("username",username);
                    startActivity(i);
                }

            }
        });
        // setupToolbarAndNavigationDrawer();
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
            batchId.setText(result.getContents());
        }
    });
}
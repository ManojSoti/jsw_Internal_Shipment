package com.company.jsw_internal_shipment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity
{
    Button btn_scan;
    ImageView imageView;
    EditText batchId;
    String username,name,mobile_no,yard_id,yard_name,email;
    SharedPreferences prefs;
  MaterialToolbar toolbar;
    TextView toolbartile;
    String batchIDFromcreateTrip;
    private DrawerLayout drawerLayout;
    UserBeanClass userBeanClass=new UserBeanClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        username=getIntent().getStringExtra("username");
        name=getIntent().getStringExtra("name");
        mobile_no=getIntent().getStringExtra("mobile_number");
        System.out.println("mobile:"+mobile_no);
        yard_id=getIntent().getStringExtra("yard_id");
        yard_name=getIntent().getStringExtra("yard_name");
        email=getIntent().getStringExtra("email_id");
        btn_scan =findViewById(R.id.scanBarcode);
        imageView=findViewById(R.id.play_button);
        batchId=findViewById(R.id.batchId);
        toolbar=findViewById(R.id.toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        toolbar.setTitle("");
        toolbartile=findViewById(R.id.toolbar_title);
        toolbartile.setText("Welcome "+username);
      //  this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        try{
            batchIDFromcreateTrip=getIntent().getStringExtra("code");
            System.out.println("code: "+batchIDFromcreateTrip);
            batchId.setText(batchIDFromcreateTrip);
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
                    Intent i=new Intent(MainActivity.this,BatchDetailsActivity.class);
                    i.putExtra("batchId",batchId.getText().toString().trim());
                    i.putExtra("username",username);
                    i.putExtra("mobile_number",mobile_no);
                    i.putExtra("name",name);
                    i.putExtra("yard_id",yard_id);
                    i.putExtra("email_id",email);
                    i.putExtra("yard_name",yard_name);
                    startActivity(i);
                }

            }
        });
        setupToolbarAndNavigationDrawer();


    }



    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setupToolbarAndNavigationDrawer() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        // Set up navigation drawer icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Set up navigation item click listener
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            buildDialog(R.style.DialogTheme, "Are you sure you want to logout");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildDialog(int animationSource, String type) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#096389'>Logout</font>"));
        builder.setMessage(type);
        builder.setPositiveButton(Html.fromHtml("<font color='#619115'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        prefs = PreferenceManager
                                .getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("LoginAppUser", false);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });

        builder.setNegativeButton(Html.fromHtml("<font color='#af4658'>No</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = animationSource;
        dialog.show();

    }
    private void handleNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.userprofile) {
            Intent i1 = new Intent(MainActivity.this, UserProfileActivity.class);
            i1.putExtra("username",username);
            i1.putExtra("mobile_number",mobile_no);
            i1.putExtra("name",name);
            i1.putExtra("yard_id",yard_id);
            i1.putExtra("email_id",email);
            i1.putExtra("yard_name",yard_name);
            startActivity(i1);
        } else if (id == R.id.createTrip) {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            i.putExtra("username",username);
            startActivity(i);
        }
             else if (id == R.id.nav_Inprogress) {
                Intent i3 = new Intent(MainActivity.this, InProgressActivity.class);
                // i3.putExtra("PROFILE_IMAGE_URI", imageUriString);
                startActivity(i3);

        } else if (id == R.id.nav_complete) {
            Intent i3 = new Intent(MainActivity.this, CompleteTripActivityEnterId.class);
            i3.putExtra("username", username);
            startActivity(i3);
        }else if(id==R.id.nav_batchforward){
            Intent i4 = new Intent(MainActivity.this,BatchForwardActivity.class);
            i4.putExtra("username", username);
            startActivity(i4);
        }else if(id==R.id.nav_createTipSource){
            Intent i4 = new Intent(MainActivity.this, CreateTripActivitySource.class);
            i4.putExtra("username", username);
            startActivity(i4);

        } else if (id == R.id.nav_completeTripDestination) {
            Intent i2 = new Intent(MainActivity.this, CompleteTripDestination.class);
            i2.putExtra("username", username);
            startActivity(i2);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
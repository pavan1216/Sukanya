package f.com.livessavers.Activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import f.com.livessavers.Adapter.BloodGroupAdapter;
import f.com.livessavers.LocationTrack;
import f.com.livessavers.MainActivity;
import f.com.livessavers.Pojo.BloodGroupSpinner;
import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;
import f.com.livessavers.Services.Masters;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //    RecyclerView recyclerView;
    List<BloodGroupSpinner> bloodGroupSpinners = new ArrayList<BloodGroupSpinner>();
    //    BloodGroupAdapter bloodGroupAdapter;
    SharedPreferences prefs, prefUsername;
    String user_id;
    TextView tvUsername, tvUserEmail;
    Button btnEmergency, btnSendMyLocation;
    ProgressDialog progressDialog;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    View headerView;
    ActionBarDrawerToggle toggle;
    LocationTrack locationTrack;
    private String[] listOfPermissions = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            android.Manifest.permission.INTERNET};
    private static final int PERMISSION_REQUEST_CODE = 1212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        initalizeValues();

        setupValues();

/*
        recyclerView = findViewById(R.id.bloodgrouprecycle);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

*/
    }

    private void setupValues() {

        prefs = getSharedPreferences("USERDETAILS", MODE_PRIVATE);
        user_id = prefs.getString("USERID", "No name defined");
        prefUsername = getSharedPreferences("USERDETAILS", MODE_PRIVATE);


        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {

            checkAndRequestPermission();

        }

        performActions();

        //get the menu from the navigation view
        Menu menu = navigationView.getMenu();

        //get switch view
        SwitchCompat switchcompat = MenuItemCompat.getActionView(menu.findItem(R.id.nav_donor_switch)).findViewById(R.id.drawer_switch);

        //add listener
        switchcompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //your action
                if (isChecked) {
                    Log.d("NavigationActivity:", "Donor Available");
                } else {
                    Log.d("NavigationActivity:", "Donor Not Available");
                }
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (!user_id.equals("0")) {
            new userdeatils().execute();
        }

    }

    public void performActions() {

        locationTrack = new LocationTrack(NavigationActivity.this);

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationActivity.this, EmergencyActivity.class);
                startActivity(i);
            }
        });

        btnSendMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (locationTrack.canGetLocation()) {
                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();
                    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                } else {
                    locationTrack.showSettingsAlert();
                }
            }
        });
    }

    private boolean checkAndRequestPermission() {

        List<String> listOfPermissionNeeded = new ArrayList<>();
        for (String permission : listOfPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listOfPermissionNeeded.add(permission);
            }
        }

        // Ask for non granted permissions
        if (!listOfPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listOfPermissionNeeded.toArray(new String[listOfPermissionNeeded.size()]),
                    PERMISSION_REQUEST_CODE
            );
            return false;
        }

        // App has all permissions proceed to go ahead
        return true;
    }

    private void initalizeValues() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnEmergency = findViewById(R.id.fab);
        btnSendMyLocation = findViewById(R.id.btn_sendlocation);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvUsername = headerView.findViewById(R.id.showusername);
        tvUserEmail = headerView.findViewById(R.id.showuseremail);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    @Override
    public void onBackPressed() {

        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            System.exit(0);
                            finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResultMap = new HashMap<>();
            int deniedCount = 0;

            for (int i = 0; i < grantResults.length; i++) {
                // Add only which are permissions denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResultMap.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            // Check if all permission are granted
            if (deniedCount == 0) {

                performActions();

            }
            // At least one or all permissions are denied
            else {

                for (Map.Entry<String, Integer> entry : permissionResultMap.entrySet()) {
                    String permission = entry.getKey();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                        // Show dialog with explanation, why permission required
                        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(NavigationActivity.this);
                        builder.setMessage(R.string.dialog_alert_permission)
                                .setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Agreed to grant permission
                                        dialog.dismiss();
                                        checkAndRequestPermission();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    } else {

                        // When denied permission, show settings page to allow permissions
                        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(NavigationActivity.this);
                        builder.setMessage(R.string.dialog_navigate_setting)
                                .setPositiveButton(R.string.dialog_navigate_setting, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Agreed to grant permission
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(R.string.exit_app, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();

                    }
                }
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_page) {

            Intent i = new Intent(NavigationActivity.this, NavigationActivity.class);
            startActivity(i);

            // Handle the camera action
        } else if (id == R.id.nav_home) {

            Intent i = new Intent(NavigationActivity.this, ViewProfileActivity.class);
            startActivity(i);

            // Handle the camera action
        } else if (id == R.id.nav_donor_switch) {

            return false;

        } /*else if (id == R.id.nav_gallery) {

            Intent i = new Intent(NavigationActivity.this, UpdateProfileActivity.class);
            startActivity(i);

        } */ else if (id == R.id.nav_slideshow) {

            Intent i = new Intent(NavigationActivity.this, ChangePasswordActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_tools) {

            Intent i = new Intent(NavigationActivity.this, BloodRequestActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_location) {

            Intent i = new Intent(NavigationActivity.this, AddressActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            prefUsername.edit().clear().commit();

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            System.exit(0);
                            finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


   /* private class fillbloodbroup extends AsyncTask<String, String, String> {

        SoapObject result = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = ProgressDialog.show(NavigationActivity.this,
                    "", "Loading. Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {


            SoapObject request = new SoapObject(Masters.NAMESPACE, Masters.fillBloodGroup);

            //Parameters


            //Version Soap
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Masters.URL);

                //Call the webservice
                androidHttpTransport.call(Masters.NAMESPACE + "/" + Masters.fillBloodGroup, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result != null)
                return result.toString();
            else
                return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (result != null) {


                String responce = result.getProperty(0).toString();


                try {

                    JSONObject jsonObject = new JSONObject(responce);

                    JSONObject object = jsonObject.getJSONObject("BloodGroupResult");

                    JSONArray jsonArray = object.getJSONArray("Table");

                    jsonArray.remove(0);
                    String blooddata = jsonArray.toString();
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    bloodGroupSpinners = Arrays.asList(mGson.fromJson(blooddata, BloodGroupSpinner[].class));
                    bloodGroupAdapter = new BloodGroupAdapter(NavigationActivity.this, bloodGroupSpinners);
                    recyclerView.setAdapter(bloodGroupAdapter);

                    progressDialog.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(NavigationActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }
*/

    private class userdeatils extends AsyncTask<String, String, String> {

        SoapObject result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {


            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.GETUSERDETAILS);
            request.addProperty("UserId", user_id);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE + "/" + CommonApi.GETUSERDETAILS, envelope);


                // Get the result
                result = (SoapObject) envelope.bodyIn;


            } catch (Exception e) {
                e.printStackTrace();
            }


            if (result != null)
                return result.toString();
            else
                return null;
        }


        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (result != null) {

                String responce = result.getProperty(0).toString();

                try {

                    JSONObject jsonObject = new JSONObject(responce);
                    JSONObject object = jsonObject.getJSONObject("UserResult");
                    JSONArray jsonArray = object.getJSONArray("Table");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        tvUsername.setText(jsonObject1.optString("DonorName"));
                        tvUserEmail.setText(jsonObject1.optString("Email"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }
}

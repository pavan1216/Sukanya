package f.com.livessavers.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

import f.com.livessavers.Pojo.CountrySpinner;
import f.com.livessavers.Pojo.StateSpinner;
import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;
import f.com.livessavers.Services.Masters;

public class UpdateProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView txtDate;
    private int mYear, mMonth, mDay;
    Spinner spgender,spcountry,spstate,spdistrict,spcity,spbloodgroup;
    EditText nametxt,mobiletxt,emailtxt,addresstxt,usernametxt,passwordtxt,conformtxt;

    Button submit;

    String[] genderarray = {"Gender" ,"Male", "Female"};


    String get_countrt,country_id,get_state,stateid,districtid,cityid,get_city,get_blood,blood_id;



    List<CountrySpinner> countrySpinners=new ArrayList<CountrySpinner>();

    List<StateSpinner>stateSpinners=new ArrayList<StateSpinner>();




    ArrayAdapter<CountrySpinner> countrySpinnerArrayAdapter;
    ArrayAdapter<StateSpinner> spinnerArrayAdapter;




    ArrayList<String> bGroupsname=new ArrayList<String>();
    ArrayList<String> bloodid=new ArrayList<String>();

    ArrayList<String> districtname=new ArrayList<String>();
    ArrayList<String> district_id=new ArrayList<String>();

    ArrayList<String> cityname=new ArrayList<String>();
    ArrayList<String> city_id=new ArrayList<String>();


    SharedPreferences prefs;
    String user_id;


    String name,mobile,email,address,username,password,conformpassword,dateofbirth,bloodgroup_id,gender_type;

    String tvcountry,tvstate,tvdistrict,tvcity,bloodgroup,gender;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setTitle("Update Profile");


        prefs = getSharedPreferences("USERDETAILS", MODE_PRIVATE);

        user_id = prefs.getString("USERID", "No name defined");


        nametxt=(EditText)findViewById(R.id.updateregnametxt);
        mobiletxt=(EditText)findViewById(R.id.updateregmobiletxt);
        emailtxt=(EditText)findViewById(R.id.updateregemailtxt);
        addresstxt=(EditText)findViewById(R.id.updateregaddresstxt);







        txtDate=(TextView) findViewById(R.id.updatein_date);

        spgender = (Spinner) findViewById(R.id.updategender);
        spcountry=(Spinner)findViewById(R.id.updatecountry);
        spstate=(Spinner)findViewById(R.id.updatestate);
        spdistrict=(Spinner)findViewById(R.id.updatedistrict);
        spcity=(Spinner)findViewById(R.id.updatecity);
        spbloodgroup=(Spinner)findViewById(R.id.updatebooldgroupspinner);

        spgender.setOnItemSelectedListener(this);

        submit=(Button)findViewById(R.id.updateregisterbtn);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ArrayAdapter aa = new ArrayAdapter(UpdateProfileActivity.this,R.layout.textviewlayoyt,genderarray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spgender.setAdapter(aa);






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name =nametxt.getText().toString().trim();
                mobile=mobiletxt.getText().toString().trim();
                email=emailtxt.getText().toString().trim();
                address=addresstxt.getText().toString().trim();
                dateofbirth=txtDate.getText().toString().trim();




                if(TextUtils.isEmpty(name)) {
                    nametxt.setError("Not Empty");
                    return;
                }
                else if(spbloodgroup.getSelectedItem().toString().trim().equals("BloodGroup"))
                {
                    Toast.makeText(UpdateProfileActivity.this,"Select Blood Group",Toast.LENGTH_LONG).show();
                }
                else if (spgender.getSelectedItem().toString().trim().equals("Gender"))
                {
                    Toast.makeText(UpdateProfileActivity.this,"Select Gender",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(dateofbirth)) {
                    txtDate.setError("Not Empty");
                    return;
                }
                else if(TextUtils.isEmpty(mobile)) {
                    mobiletxt.setError("Not Empty");
                    return;
                }
                else if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    emailtxt.setError("Not Valid Email");

                }
                else if (spcountry.getSelectedItem().toString().trim().equals("Country"))
                {
                    Toast.makeText(UpdateProfileActivity.this,"Select Country",Toast.LENGTH_LONG).show();
                }
                else if (spstate.getSelectedItem().toString().trim().equals("State"))
                {
                    Toast.makeText(UpdateProfileActivity.this,"Select State",Toast.LENGTH_LONG).show();
                }
                else if (spdistrict.getSelectedItem().toString().trim().equals("District"))
                {
                    Toast.makeText(UpdateProfileActivity.this,"Select District",Toast.LENGTH_LONG).show();
                }
                else if (spcity.getSelectedItem().toString().trim().equals("City"))
                {
                    Toast.makeText(UpdateProfileActivity.this,"Select City",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(address)) {
                    addresstxt.setError("Not Empty");
                    return;
                }

                else
                {

                    new updateProfile().execute();

                }
            }
        });

        new userdeatils().execute();



    }


    private class updateProfile extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {






            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.UPDATEUSERPROFILE);
            request.addProperty("UserId", user_id);
            request.addProperty("DonorName",name);
            request.addProperty("BloodGroupId",bloodgroup_id);
            request.addProperty("Gender", gender_type);
            request.addProperty("DateofBirth",dateofbirth);
            request.addProperty("ContactNumber",mobile);
            request.addProperty("Email", email);
            request.addProperty("CountryId",country_id);
            request.addProperty("StateId",stateid);
            request.addProperty("DistrictId",districtid);
            request.addProperty("CityId",cityid);
            request.addProperty("Address",address);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.UPDATEUSERPROFILE, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;


            }
            catch (Exception e)
            {
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


                String responce=result.getProperty(0).toString();

                try {

                    JSONObject jsonObject=new JSONObject(responce);
                    JSONObject object=jsonObject.getJSONObject("UserProfileResult");

                    String result = object.getString("Result");

                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

                    }
                catch (Exception e)
                {
                    e.printStackTrace();
                }





            } else {
                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        }



    }






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

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.GETUSERDETAILS, envelope);



                // Get the result
                result = (SoapObject) envelope.bodyIn;


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



            if (result != null)
                return result.toString();
            else
                return null;
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (result != null) {


                String responce=result.getProperty(0).toString();
                try {

                    JSONObject jsonObject=new JSONObject(responce);
                    JSONObject object=jsonObject.getJSONObject("UserResult");

                    JSONArray jsonArray=object.getJSONArray("Table");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                        nametxt.setText(jsonObject1.optString("DonorName"));
                        bloodgroup=jsonObject1.optString("BloodGroupName");

                        gender=jsonObject1.optString("Gender");


                        String dateoftime =jsonObject1.optString("DateofBirth").substring(0,10);


                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        date = df.parse(dateoftime);
                        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

                        txtDate.setText(df1.format(date));

                        mobiletxt.setText(jsonObject1.optString("ContactNumber"));
                        emailtxt.setText(jsonObject1.optString("Email"));
                        addresstxt.setText(jsonObject1.optString("Address"));
                        tvcountry=jsonObject1.optString("CountryId");
                        tvstate=jsonObject1.optString("StateId");
                        tvdistrict=jsonObject1.optString("DistrictId");
                        tvcity=jsonObject1.optString("CityId");






                        new fillbloodbroup().execute();
                        new countries().execute();







                    }




                    for(int j=0;j<genderarray.length;j++)
                    {

                        if(gender.equals(genderarray[j]))
                        {
                            spgender.setSelection(j);
                        }

                    }







                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }





            } else {
                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        }



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        gender_type=genderarray[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




    private class fillbloodbroup extends AsyncTask<String, String, String> {

        SoapObject result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                androidHttpTransport.call(Masters.NAMESPACE +"/"+ Masters.fillBloodGroup, envelope);

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

                    JSONObject object=jsonObject.getJSONObject("BloodGroupResult");

                    JSONArray jsonArray=object.getJSONArray("Table");




                    bloodid.clear();
                    bGroupsname.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                        bloodid.add(jsonObject1.optString("Id"));

                        bGroupsname.add(jsonObject1.optString("BloodGroupName"));

                    }


                    spbloodgroup.setAdapter(new ArrayAdapter<String>(UpdateProfileActivity.this, R.layout.textviewlayoyt, bGroupsname));



                    for(int i =0;i<bGroupsname.size();i++)
                    {

                        if(bloodgroup.equals(bGroupsname.get(i)))
                        {

                            spbloodgroup.setSelection(i);
                        }
                    }

                    spbloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            bloodgroup_id = bloodid.get(position);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });







                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(UpdateProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }




    private class countries extends AsyncTask<String, String, String> {

        SoapObject result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            SoapObject request = new SoapObject(Masters.NAMESPACE, Masters.fill_country);

            //Parameters


            //Version Soap
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Masters.URL);

                //Call the webservice
                androidHttpTransport.call(Masters.NAMESPACE +"/"+ Masters.fill_country, envelope);

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

                    JSONObject object=jsonObject.getJSONObject("CountryResult");

                    JSONArray jsonArray=object.getJSONArray("Table");



                    String data=jsonArray.toString();



                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();




                    countrySpinners= Arrays.asList(mGson.fromJson(data, CountrySpinner[].class));
                    countrySpinnerArrayAdapter = new ArrayAdapter<CountrySpinner>(UpdateProfileActivity.this, R.layout.textviewlayoyt, countrySpinners);
                    spcountry.setAdapter(countrySpinnerArrayAdapter);





                    for(int i = 0; i < countrySpinnerArrayAdapter.getCount(); i++)
                    {
                        if (countrySpinners.get(i).getId().equals(tvcountry) )
                        {
                            spcountry.setSelection(i); //(false is optional)
                            break;
                        }

                    }

                    spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CountrySpinner spinnercountry_id = (CountrySpinner) parent.getSelectedItem();
                            country_id=spinnercountry_id.getId();
                            new state().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(UpdateProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }



    private class state extends AsyncTask<String, String, String>{


        SoapObject result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            SoapObject request = new SoapObject(Masters.NAMESPACE, Masters.fill_state);


            request.addProperty("CountryId",country_id);

            //Version Soap
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Masters.URL);

                //Call the webservice
                androidHttpTransport.call(Masters.NAMESPACE +"/"+ Masters.fill_state, envelope);

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

                    JSONObject object=jsonObject.getJSONObject("StateResult");
                    JSONArray jsonArray=object.getJSONArray("Table");
                    String data=jsonArray.toString();

                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();

                    stateSpinners= Arrays.asList(mGson.fromJson(data, StateSpinner[].class));

                    spinnerArrayAdapter = new ArrayAdapter<StateSpinner>(UpdateProfileActivity.this, R.layout.textviewlayoyt, stateSpinners);
                    spstate.setAdapter(spinnerArrayAdapter);



                    for(int i = 0; i < spinnerArrayAdapter.getCount(); i++)
                    {
                        if (stateSpinners.get(i).getId().equals(tvstate) )
                        {
                            spstate.setSelection(i); //(false is optional)
                            break;
                        }

                    }


                    spstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            StateSpinner stateSpinner_id = (StateSpinner) parent.getSelectedItem();

                            stateid=stateSpinner_id.getId();

                            new districtwise().execute();


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(UpdateProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }



    private class districtwise extends AsyncTask<String, String, String> {

        SoapObject result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            SoapObject request = new SoapObject(Masters.NAMESPACE, Masters.fill_district);



            request.addProperty("StateId",stateid);

            //Parameters


            //Version Soap
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Masters.URL);

                //Call the webservice
                androidHttpTransport.call(Masters.NAMESPACE+"/"+ Masters.fill_district, envelope);

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
                    JSONObject object=jsonObject.getJSONObject("DistrictResult");
                    JSONArray jsonArray=object.getJSONArray("Table");
                    district_id.clear();
                    districtname.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        district_id.add(jsonObject1.optString("Id"));
                        districtname.add(jsonObject1.optString("DistrictName"));

                    }

                    spdistrict.setAdapter(new ArrayAdapter<String>(UpdateProfileActivity.this, R.layout.textviewlayoyt, districtname));
                    for(int j =0;j<district_id.size();j++)
                    {

                        if(tvdistrict.equals(district_id.get(j)))
                        {

                            spdistrict.setSelection(j);
                        }
                    }


                    spdistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            districtid= String.valueOf(district_id.get(position));
                            new citywise().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(UpdateProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }




    private class citywise extends AsyncTask<String, String, String> {

        SoapObject result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            SoapObject request = new SoapObject(Masters.NAMESPACE, Masters.fill_city);



            request.addProperty("DistrictId",districtid);

            //Parameters


            //Version Soap
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Masters.URL);

                //Call the webservice
                androidHttpTransport.call(Masters.NAMESPACE+"/"+ Masters.fill_city, envelope);

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




                    JSONObject object=jsonObject.getJSONObject("CityResult");



                    JSONArray jsonArray=object.getJSONArray("Table");

                    city_id.clear();
                    cityname.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                        city_id.add(jsonObject1.optString("Id"));

                        cityname.add(jsonObject1.optString("CityName"));

                    }

                    spcity.setAdapter(new ArrayAdapter<String>(UpdateProfileActivity.this, R.layout.textviewlayoyt, cityname));

                    for(int  j=0;j<city_id.size();j++)
                    {

                        if(tvcity.equals(city_id.get(j)))
                        {

                            spcity.setSelection(j);
                        }
                    }

                    spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cityid= String.valueOf(city_id.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(UpdateProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }



}

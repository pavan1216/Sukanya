package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.List;

import f.com.livessavers.Pojo.CountrySpinner;
import f.com.livessavers.Pojo.StateSpinner;
import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;
import f.com.livessavers.Services.Masters;

public class ViewProfileActivity extends AppCompatActivity {

    TextView username,gender,mobile,email,age,dateofbirth,address,country,state,district,city,bloodgroup;
    String tvusername,tvgender,tvmobile,tvemail,tvage,tvdateofbirth,tvcountry,tvstate,tvdistrict,tvcity;
    SharedPreferences prefs;
    String user_id;
    List<CountrySpinner> countrySpinners=new ArrayList<CountrySpinner>();
    List<StateSpinner>stateSpinners=new ArrayList<StateSpinner>();

    ArrayList<String> districtname=new ArrayList<String>();
    ArrayList<String> district_id=new ArrayList<String>();
    ArrayList<String> cityname=new ArrayList<String>();
    ArrayList<String> city_id=new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().setTitle("View Profile");

        prefs = getSharedPreferences("USERDETAILS", MODE_PRIVATE);

        user_id = prefs.getString("USERID", "No name defined");

        bloodgroup =(TextView)findViewById(R.id.tvbloodgroup);
        username =(TextView)findViewById(R.id.tvusername);
        gender =(TextView)findViewById(R.id.tvgender);
        age =(TextView)findViewById(R.id.tvage);
        dateofbirth =(TextView)findViewById(R.id.tvdob);
        mobile =(TextView)findViewById(R.id.tvcontactnumber);
        email =(TextView)findViewById(R.id.tvemail);
        address =(TextView)findViewById(R.id.tvaddress);
        country =(TextView)findViewById(R.id.tvcountry);
        state =(TextView)findViewById(R.id.tvstate);
        district =(TextView)findViewById(R.id.tvdistrict);
        city =(TextView)findViewById(R.id.tvcity);

        new userdeatils().execute();


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


                        username.setText(jsonObject1.optString("DonorName"));
                        bloodgroup.setText(jsonObject1.optString("BloodGroupName"));

                        gender.setText(jsonObject1.optString("Gender"));
                        dateofbirth.setText(jsonObject1.optString("DateofBirth"));
                        age.setText(jsonObject1.optString("Age"));
                        mobile.setText(jsonObject1.optString("ContactNumber"));
                        email.setText(jsonObject1.optString("Email"));
                        address.setText(jsonObject1.optString("Address"));
                        tvcountry=jsonObject1.optString("CountryId");
                        tvstate=jsonObject1.optString("StateId");
                        tvdistrict=jsonObject1.optString("DistrictId");
                        tvcity=jsonObject1.optString("CityId");

                        new countries().execute();
                        new state().execute();
                        new districtwise().execute();
                        new citywise().execute();


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

                   for(int i =0;i<countrySpinners.size();i++)
                   {

                       if(tvcountry.equals(countrySpinners.get(i).getId()))
                       {

                           country.setText(""+countrySpinners.get(i).getCountryName());
                       }
                   }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(ViewProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
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


            request.addProperty("CountryId",tvcountry);

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

                    for(int i=0;i<stateSpinners.size();i++)
                    {
                        if(tvstate.equals(stateSpinners.get(i).getId()))
                        {
                            state.setText(stateSpinners.get(i).getStateName());
                        }
                    }





                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(ViewProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
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



            request.addProperty("StateId",tvstate);

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

                    for(int j=0;j<district_id.size();j++)
                    {

                        if(tvdistrict.equals(district_id.get(j)))
                        {

                            district.setText(districtname.get(j));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(ViewProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
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



            request.addProperty("DistrictId",tvdistrict);

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







                    for(int j=0;j<city_id.size();j++)
                    {

                        if(tvcity.equals(city_id.get(j)))
                        {

                            city.setText(cityname.get(j));
                        }
                    }









                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(ViewProfileActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }
}

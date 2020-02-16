package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import f.com.livessavers.Pojo.BloodGroupSpinner;
import f.com.livessavers.Pojo.CountrySpinner;
import f.com.livessavers.Pojo.DonorsListPojo;
import f.com.livessavers.Pojo.StateSpinner;
import f.com.livessavers.R;
import f.com.livessavers.Services.Masters;

public class BloodSearchAcitivity extends AppCompatActivity {


    private Gson gson;
    GsonBuilder builder;
    Spinner spblood,spcountry,spstate,spdistrict,spcity;
    Button searchbtn;
    String get_countrt,country_id,state_id,get_state,stateid,districtid,cityid,getblood_id;

    List<CountrySpinner> countrySpinners=new ArrayList<CountrySpinner>();
    ArrayAdapter<CountrySpinner> countrySpinnerArrayAdapter;

    List<StateSpinner>stateSpinners=new ArrayList<StateSpinner>();
    ArrayAdapter<StateSpinner> spinnerArrayAdapter;

    ArrayList<String> districtname=new ArrayList<String>();
    ArrayList<String> district_id=new ArrayList<String>();

    ArrayList<String> cityname=new ArrayList<String>();
    ArrayList<String> city_id=new ArrayList<String>();

    BloodGroupSpinner bloodGroupSpinner;

    List<DonorsListPojo> donorsListPojo=new ArrayList<DonorsListPojo>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_search_acitivity);


        spcountry=(Spinner)findViewById(R.id.searchcountry);
        spstate=(Spinner)findViewById(R.id.searchstate);
        spdistrict=(Spinner)findViewById(R.id.searchdistrict);
        spcity=(Spinner)findViewById(R.id.searchcity);

        searchbtn=(Button)findViewById(R.id.searchbtn);


        builder = new GsonBuilder();
        gson = builder.create();
        String productInStringFormat = getIntent().getExtras().getString("bloodgroup");
        bloodGroupSpinner=gson.fromJson(productInStringFormat, BloodGroupSpinner.class);

        if(bloodGroupSpinner!=null)
        {
            getblood_id=String.valueOf(bloodGroupSpinner.getId());
            getSupportActionBar().setTitle("BLOOD GROUP : "+bloodGroupSpinner.getBloodGroupName());


            new countries().execute();

            searchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getBaseContext(), DonorsListActivity.class);
                    intent.putExtra("BLOOD_ID", getblood_id);
                    intent.putExtra("COUNTRY_ID", country_id);
                    intent.putExtra("STATE_ID", stateid);
                    intent.putExtra("DISTRICT_ID", districtid);
                    intent.putExtra("CITY_ID", cityid);
                    startActivity(intent);

                }
            });

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
                    countrySpinnerArrayAdapter = new ArrayAdapter<CountrySpinner>(BloodSearchAcitivity.this, R.layout.textviewlayoyt, countrySpinners);
                    spcountry.setAdapter(countrySpinnerArrayAdapter);

                    for(int i = 0; i < countrySpinnerArrayAdapter.getCount(); i++)
                    {
                        if (countrySpinners.get(i).getId().equals(get_countrt) )
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
                Toast.makeText(BloodSearchAcitivity.this, "No Response", Toast.LENGTH_LONG).show();
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

                    spinnerArrayAdapter = new ArrayAdapter<StateSpinner>(BloodSearchAcitivity.this, R.layout.textviewlayoyt, stateSpinners);
                    spstate.setAdapter(spinnerArrayAdapter);
                    for(int i = 0; i < spinnerArrayAdapter.getCount(); i++)
                    {
                        if (stateSpinners.get(i).getId().equals(get_state) )
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
                Toast.makeText(BloodSearchAcitivity.this, "No Response", Toast.LENGTH_LONG).show();
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

                    spdistrict.setAdapter(new ArrayAdapter<String>(BloodSearchAcitivity.this, R.layout.textviewlayoyt, districtname));


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
                Toast.makeText(BloodSearchAcitivity.this, "No Response", Toast.LENGTH_LONG).show();
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



                    spcity.setAdapter(new ArrayAdapter<String>(BloodSearchAcitivity.this, R.layout.textviewlayoyt, cityname));
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
                Toast.makeText(BloodSearchAcitivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }
}

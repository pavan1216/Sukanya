package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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


import f.com.livessavers.Adapter.DonorsListAdapter;
import f.com.livessavers.Pojo.DonorsListPojo;
import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

public class DonorsListActivity extends AppCompatActivity {



    RecyclerView recyclerView;
    DonorsListAdapter donorsListAdapter;


    List<DonorsListPojo> donorsList=new ArrayList<DonorsListPojo>();

    String blood_id,country_id,state_id,district_id,city_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_list);


        getSupportActionBar().setTitle("DONORS LIST");


        recyclerView =(RecyclerView)findViewById(R.id.donorlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        blood_id=getIntent().getStringExtra("BLOOD_ID");
        country_id=getIntent().getStringExtra("COUNTRY_ID");
        state_id=getIntent().getStringExtra("STATE_ID");
        district_id=getIntent().getStringExtra("DISTRICT_ID");
        city_id=getIntent().getStringExtra("CITY_ID");


        if(blood_id.equals("0"))
        {
            blood_id="";
        }
        if(country_id.equals("0"))
        {
            country_id="";
        }
        if(state_id.equals("0"))
        {
            state_id="";
        }
        if(district_id.equals("0"))
        {
            district_id="";
        }
        if(city_id.equals("0"))
        {
            city_id="";
        }



        new getdonorslist().execute();

    }


    private class getdonorslist extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {



            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.GETDONORSLIST);
            request.addProperty("BloodGroupId",blood_id);
            request.addProperty("CountryId",country_id);
            request.addProperty("StateId",state_id);
            request.addProperty("DistrictId",district_id);
            request.addProperty("CityId",city_id);




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.GETDONORSLIST, envelope);



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

                    if(object.length()==1)
                    {
                        JSONArray jsonArray=object.getJSONArray("Table");
                        String blooddata=jsonArray.toString();
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        donorsList= Arrays.asList(mGson.fromJson(blooddata, DonorsListPojo[].class));
                        donorsListAdapter= new DonorsListAdapter(DonorsListActivity.this,donorsList);
                        recyclerView.setAdapter(donorsListAdapter);
                    }
                    else
                    {
                        String result = object.getString("Result");
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
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


}

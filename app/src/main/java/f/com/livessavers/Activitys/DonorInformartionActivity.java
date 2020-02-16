package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import cz.msebera.android.httpclient.Header;
import f.com.livessavers.Pojo.BloodGroupSpinner;
import f.com.livessavers.Pojo.DonorsListPojo;
import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

public class DonorInformartionActivity extends AppCompatActivity {

    private Gson gson;
    GsonBuilder builder;
    DonorsListPojo donorsListPojo;

    TextView donorname, donorgroup, donorgender, donorcontact, donoremail;

    Button btnsms;
    SharedPreferences prefs;
    String user_id, username, mobile, blood_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_informartion);


        getSupportActionBar().setTitle("DONOR INFORMATION");
        builder = new GsonBuilder();
        gson = builder.create();
        String productInStringFormat = getIntent().getExtras().getString("DonorInformation");
        donorsListPojo = gson.fromJson(productInStringFormat, DonorsListPojo.class);

        donorname = (TextView) findViewById(R.id.donorinforname);
        donorgroup = (TextView) findViewById(R.id.donorinforgroup);
        donorgender = (TextView) findViewById(R.id.donorinforgender);
        donorcontact = (TextView) findViewById(R.id.donorinfornumber);
        donoremail = (TextView) findViewById(R.id.donorinforemail);


        btnsms = (Button) findViewById(R.id.sendsms);

        prefs = getSharedPreferences("USERDETAILS", MODE_PRIVATE);

        user_id = prefs.getString("USERID", "No name defined");

        new userdeatils().execute();


        if (donorsListPojo != null) {

            donorname.setText(donorsListPojo.getDonorName());
            donorgroup.setText(donorsListPojo.getBloodGroupName());
            donorgender.setText(donorsListPojo.getGender());
            donorcontact.setText(donorsListPojo.getContactNumber().replaceAll("\\w(?=\\w{4})", "*"));

            donoremail.setText(donorsListPojo.getEmail().replaceAll("(\\w{1})(\\w+)(@)", "$1****"));


            btnsms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(donorsListPojo.getContactNumber().length()==10)
                    {
                        RequestParams requestParams=new RequestParams();
                        requestParams.put("user",CommonApi.username);
                        requestParams.put("password",CommonApi.password);
                        requestParams.put("mobilenumber",donorsListPojo.getContactNumber());
                        requestParams.put("message","To help us serve you better, we request you to donate blood group :" +blood_name +","+" to " +username+" , "+"please contact no "+mobile+"  From www.Lifessaver.com");
                        requestParams.put("sid",CommonApi.sid);
                        requestParams.put("mtype",CommonApi.mtype);
                        verficationservice(requestParams);
                    }



                }
            });
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


                        username = jsonObject1.optString("DonorName");
                        blood_name = jsonObject1.optString("BloodGroupName");
                        mobile = jsonObject1.optString("ContactNumber");




                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        }


    }



    private void verficationservice(RequestParams req) {


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);
        RequestParams params = req;


        client.get(CommonApi.smsgateway,params, new AsyncHttpResponseHandler()
        {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }



        });
    }
}

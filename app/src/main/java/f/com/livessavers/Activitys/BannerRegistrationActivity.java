package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

public class BannerRegistrationActivity extends AppCompatActivity {

    Button navregistartion;
    SharedPreferences prefs;
    String user_id,payment_id,name,mobile,email,order_id,amount,recepit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_registration);

        prefs = getSharedPreferences("USERDETAILS", MODE_PRIVATE);

        user_id = prefs.getString("USERID", "No name defined");


        navregistartion=(Button)findViewById(R.id.navregistration);

        navregistartion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new insertPayment().execute();
            }
                });
    }



    private class insertPayment extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.InsertPayment);
            request.addProperty("UserId", user_id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.InsertPayment, envelope);

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
                    JSONObject object=jsonObject.getJSONObject("InsertPaymentResult");
                    String result=object.getString("Result");

                    int status=object.getInt("Status");

                     payment_id = object.getString("PaymentId");

                    if(!payment_id.equals("0"))
                    {

                        new fillpayment().execute();
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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


    private class fillpayment extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {



            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.FillPayment);
            request.addProperty("UserId", user_id);
            request.addProperty("PaymentId",payment_id);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.FillPayment, envelope);



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
                    JSONObject object=jsonObject.getJSONObject("FillPaymentDetails");

                    JSONObject object1 = object.getJSONObject("DonorDetails");
                    JSONObject object2 = object.getJSONObject("PaymentDetails");

                    JSONArray jsonArray=object1.getJSONArray("Table");
                    JSONArray jsonArray1=object2.getJSONArray("Table");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        name=jsonObject1.optString("DonorName");
                        mobile=jsonObject1.optString("ContactNumber");
                        email=jsonObject1.optString("Email");
                    }

                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                        order_id=jsonObject2.optString("OrderId");
                        amount=jsonObject2.optString("Amount");
                        recepit=jsonObject2.optString("Receipt");
                    }


                    if(order_id!=null)
                    {
                        Intent i = new Intent(BannerRegistrationActivity.this, MerchantActivity.class);
                        i.putExtra("donorname",name);
                        i.putExtra("donoremail",email);
                        i.putExtra("donormobile",mobile);
                        i.putExtra("donoramount",amount);
                        i.putExtra("donorrderid",order_id);
                        i.putExtra("donorrecepit",recepit);
                        startActivity(i);
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

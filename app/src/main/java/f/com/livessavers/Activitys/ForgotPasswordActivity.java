package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText user_txt;
    Button subbtn;
    String user_ip,username,password,mobilenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Forgot Password");

        user_txt=findViewById(R.id.formobiletxt);

        subbtn=findViewById(R.id.forbtnsubmit);

        subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_ip=user_txt.getText().toString();

                if(TextUtils.isEmpty(user_ip))
                {
                    user_txt.setError("Not Empty");
                }
                else
                {
                    new forgotUserDetailsAsync().execute();
                }
            }
        });

    }



    private class forgotUserDetailsAsync extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.UserForgotPassword);
            request.addProperty("Username", user_ip);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.UserForgotPassword, envelope);

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

                String response=result.getProperty(0).toString();

                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject object=jsonObject.getJSONObject("UserForgotPassword");

                    if(object.length()==2)
                    {
                        String result = object.getString("Result");
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        JSONArray jsonArray=object.getJSONArray("Table");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            username=jsonObject1.getString("UserName");
                            password=jsonObject1.getString("Password");
                            mobilenumber=jsonObject1.getString("ContactNumber");

                            if(mobilenumber.length()==10)
                            {

                                RequestParams requestParams=new RequestParams();
                                requestParams.put("user",CommonApi.username);
                                requestParams.put("password",CommonApi.password);
                                requestParams.put("mobilenumber",mobilenumber);
                                requestParams.put("message","www.lifessaver.com " + " Username :" +username + " Password : " +password);
                                requestParams.put("sid",CommonApi.sid);
                                requestParams.put("mtype",CommonApi.mtype);
                                verficationService(requestParams);

                            }
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


    private void verficationService(RequestParams req) {

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

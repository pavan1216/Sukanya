package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

public class ChangePasswordActivity extends AppCompatActivity {

    SharedPreferences prefs;
    String user_id;

    EditText oldtxt,newtxt,confirmtxt;
    String oldpassword,newpassword,confirmpassword;

    Button submitpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Password Change");

        prefs = getSharedPreferences("USERDETAILS", MODE_PRIVATE);

        user_id = prefs.getString("USERID", "No name defined");

        oldtxt=(EditText)findViewById(R.id.oldpass);
        newtxt=(EditText)findViewById(R.id.newpass);
        confirmtxt=(EditText)findViewById(R.id.confnewpass);
        submitpassword=(Button)findViewById(R.id.changepass);


        submitpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldpassword=oldtxt.getText().toString();
                newpassword=newtxt.getText().toString();
                confirmpassword=confirmtxt.getText().toString();



                if(TextUtils.isEmpty(oldpassword)) {
                    oldtxt.setError("Not Empty");
                    return;
                }
                else if(TextUtils.isEmpty(newpassword)) {
                    newtxt.setError("Not Empty");
                    return;
                }
                else if(TextUtils.isEmpty(confirmpassword)) {
                    confirmtxt.setError("Not Empty");
                    return;
                }
                else if(!newpassword.equals(confirmpassword))
                {
                    Toast.makeText(ChangePasswordActivity.this,"Please Enter Correct Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    new userchangepassword().execute();
                }
            }
        });




    }


    private class userchangepassword extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {



            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.USERCHANGEPASSWORD);
            request.addProperty("UserId", user_id);
            request.addProperty("Password", oldpassword);
            request.addProperty("NewPassword", newpassword);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.USERCHANGEPASSWORD, envelope);



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
                    String output=jsonObject.getString("UserChangePassword");
                    Toast.makeText(getApplicationContext(),output, Toast.LENGTH_LONG).show();


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

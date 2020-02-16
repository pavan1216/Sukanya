package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Calendar;

import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;
import f.com.livessavers.Services.Masters;

public class BloodRequestActivity extends AppCompatActivity {


    Button submit;
    Spinner spbloodgroup;
    ArrayList<String> bGroupsname=new ArrayList<String>();
    ArrayList<String> bloodid=new ArrayList<String>();
    String bloodgroup_id,dateofbirth,patientname,conatctnumber,email,message;

    EditText patientnametxt,contacttxt,emailtxt,messagetxt;
    TextView txtDate;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);

        getSupportActionBar().setTitle("Blood Request");


        spbloodgroup=(Spinner)findViewById(R.id.reqbloodspinner);
        txtDate=(TextView) findViewById(R.id.reqin_date);
        patientnametxt=(EditText)findViewById(R.id.reqpatientname);
        contacttxt=(EditText)findViewById(R.id.reqcontactnumber);
        emailtxt=(EditText)findViewById(R.id.reqemail);
        messagetxt=(EditText)findViewById(R.id.reqmessage);
        submit=(Button)findViewById(R.id.reqbtn);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                patientname=patientnametxt.getText().toString();
                conatctnumber=contacttxt.getText().toString();
                email=emailtxt.getText().toString();
                message=messagetxt.getText().toString();
                dateofbirth=txtDate.getText().toString();

                if(TextUtils.isEmpty(patientname)) {
                    patientnametxt.setError("Not Empty");
                    return;
                }
                else if(spbloodgroup.getSelectedItem().toString().trim().equals("BloodGroup"))
                {
                    Toast.makeText(BloodRequestActivity.this,"Select Blood Group",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(dateofbirth))
                {
                    txtDate.setError("Not Empty");
                    return;
                }
                else if(TextUtils.isEmpty(conatctnumber))
                {
                    contacttxt.setError("Not Empty");
                    return;
                }
                else if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    emailtxt.setError("Not Valid Email");

                }
                else
                {
                    new bloodrequest().execute();
                }


            }
        });



        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BloodRequestActivity.this,
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

        new fillbloodbroup().execute();
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


                    spbloodgroup.setAdapter(new ArrayAdapter<String>(BloodRequestActivity.this, R.layout.textviewlayoyt, bGroupsname));





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
                Toast.makeText(BloodRequestActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class bloodrequest extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {



            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.POSTBLOODREQUEST);
            request.addProperty("PatientName", patientname);
            request.addProperty("BloodGroupId", bloodgroup_id);
            request.addProperty("RequestDate", dateofbirth);
            request.addProperty("ContactNumber", conatctnumber);
            request.addProperty("Email", email);
            request.addProperty("Message", message);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.POSTBLOODREQUEST, envelope);



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

                    JSONObject object=jsonObject.getJSONObject("BloodRequest");

                    String result=object.getString("Result");
                    String status = object.getString("Status");

                    if(status.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(BloodRequestActivity.this, NavigationActivity.class);
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

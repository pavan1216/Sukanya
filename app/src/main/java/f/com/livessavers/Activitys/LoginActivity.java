package f.com.livessavers.Activitys;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

import static f.com.livessavers.utils.Constants.PREF_KEY_RESULT;
import static f.com.livessavers.utils.Constants.PREF_KEY_SUCCESS;
import static f.com.livessavers.utils.Constants.PREF_KEY_USERID;
import static f.com.livessavers.utils.Constants.PREF_LIFE_SAVERS;
import static f.com.livessavers.utils.Constants.RESULT_SUCCESS;
import static f.com.livessavers.utils.Constants.STATUS_SUCCESS;

public class LoginActivity extends AppCompatActivity {

    TextView tvForgotPassword, tvSignup;
    Button btnLogin;
    EditText etUserName, etPaassword;
    String enteredUserName, enteredPassword;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        initializeValues();

        setUpValues();

    }

    private void setUpValues() {

        editor = getSharedPreferences(PREF_LIFE_SAVERS, MODE_PRIVATE).edit();

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               navigateToForgotPasswordScreen();
            }
        });


        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               navigateToRegisterationScreen();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    new donorlogin(enteredUserName, enteredPassword).execute();
                }
            }
        });


    }

    private boolean isValid() {

        enteredUserName = etUserName.getText().toString().trim();
        enteredPassword = etPaassword.getText().toString().trim();

        if (TextUtils.isEmpty(enteredUserName)) {
            etUserName.setError("Not Empty");
            return false;
        } else if (TextUtils.isEmpty(enteredPassword)) {
            etPaassword.setError("Not Empty");
            return false;
        } else{
            return true;
        }
    }

    private void initializeValues() {

        tvForgotPassword = findViewById(R.id.forgottv);
        tvSignup =  findViewById(R.id.signuptv);
        etUserName = findViewById(R.id.usertxt);
        etPaassword = findViewById(R.id.passtxt);
        btnLogin = findViewById(R.id.btnlogin);

    }


    private class donorlogin extends AsyncTask<String, String, String> {

        SoapObject result = null;
        String userName;
        String password;

        public donorlogin(String userName, String password){
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this,
                    "", "Loading. Please wait...", true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {


            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.DONNORLOGIN);
            request.addProperty("Username", userName);
            request.addProperty("Password", password);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE + "/" + CommonApi.DONNORLOGIN, envelope);

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
                    JSONObject object = jsonObject.getJSONObject("UserLogin");

                    String result = object.getString("Result");
                    int status = object.getInt("Status");
                    String user_id = object.getString("UserId");
                    String paymentStatus = object.getString("PaymentStatus");

                    if (status == RESULT_SUCCESS) {

                        if (paymentStatus.equals(STATUS_SUCCESS)) {

                            editor.putString(PREF_KEY_USERID, user_id);
                            editor.putString(PREF_KEY_SUCCESS, paymentStatus);
                            editor.apply();

                            navigateToNavigationDashboard();

                        } else {

                            editor.putString(PREF_KEY_USERID, user_id);
                            editor.apply();

                            navigateToHomeScreen();
                        }

                        Toast.makeText(getApplicationContext(), object.getString(PREF_KEY_RESULT), Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_invalid_user), Toast.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Not able to login now...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void navigateToHomeScreen() {
        Intent i = new Intent(LoginActivity.this, BannerRegistrationActivity.class);
        startActivity(i);
    }

    private void navigateToNavigationDashboard() {
        Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
        startActivity(i);
    }

    private void navigateToRegisterationScreen() {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void navigateToForgotPasswordScreen() {
        Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
    }
}

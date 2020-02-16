package f.com.livessavers.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

import static android.Manifest.permission.CALL_PHONE;

public class EmergencyActivity extends AppCompatActivity {

    TextView call, call1;
    EditText username, mobile, mobile1, sms;
    Button btnsend;

    String name, phone, phone1, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        call = (TextView) findViewById(R.id.emergencypho);
        call1 = (TextView) findViewById(R.id.emergencypho1);

        username = (EditText) findViewById(R.id.emergencytxtname);
        mobile = (EditText) findViewById(R.id.emergencytxtmobile);
        mobile1 = (EditText) findViewById(R.id.emergencytxtmobile1);
        sms = (EditText) findViewById(R.id.emergencytxtsms);

        btnsend = (Button) findViewById(R.id.emergencybtnsms);

        call.setText("8790160741");
        call1.setText("6300912687");


        call.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:8790160741"));

                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(i);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });

        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:6300912687"));

                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(i);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                }
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = username.getText().toString();
                phone = mobile.getText().toString();
                phone1 = mobile1.getText().toString();
                message = sms.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    username.setError("Not Empty");

                } else if (TextUtils.isEmpty(phone) && phone1.length() == 10) {
                    mobile.setError("Not Empty");

                } else if (TextUtils.isEmpty(phone1) && phone1.length() == 10) {
                    mobile1.setError("Not Empty");

                } else if (TextUtils.isEmpty(message)) {
                    sms.setError("Not Empty");

                } else {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user", CommonApi.username);
                    requestParams.put("password", CommonApi.password);
                    requestParams.put("mobilenumber", "6300912687");
                    requestParams.put("message", "www.livessaver.com " + name + " ph : " + phone + "  ph1 : " + phone1 + " " + message);
                    requestParams.put("sid", CommonApi.sid);
                    requestParams.put("mtype", CommonApi.mtype);
                    verficationService(requestParams);
                }
            }
        });
    }


    private void verficationService(RequestParams req) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);
        RequestParams params = req;

        client.get(CommonApi.smsgateway, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

               /* Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();*/

               showSuccessDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Successfully sent message")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

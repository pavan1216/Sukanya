package f.com.livessavers.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import f.com.livessavers.R;
import f.com.livessavers.Services.CommonApi;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MerchantActivity extends Activity implements PaymentResultListener {

    String username,useremail,usermobile,order_id,order_recepit,amount,payment_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_payment);


        username= getIntent().getStringExtra("donorname");
        useremail= getIntent().getStringExtra("donoremail");
        usermobile= getIntent().getStringExtra("donormobile");
        amount= getIntent().getStringExtra("donoramount");
        order_id= getIntent().getStringExtra("donorrderid");
        order_recepit= getIntent().getStringExtra("donorrecepit");






        Checkout.preload(getApplicationContext());

        if(order_id!=null)
        {
            startPayment();
        }



    }
    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setImage(R.drawable.blood);

        try {
            JSONObject options = new JSONObject();
            options.put("name", username);
            options.put("description", order_recepit);
             options.put("currency", "INR");
             options.put("amount", 100);
             options.put("order_id",order_id);

            JSONObject preFill = new JSONObject();
            preFill.put("email", useremail);
            preFill.put("contact", usermobile);


            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {


            payment_id= razorpayPaymentID;

            if(payment_id!=null)
            {
                new updatepayment().execute();
                Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private class updatepayment extends AsyncTask<String, String, String> {

        SoapObject result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(CommonApi.NAMESPACE, CommonApi.UpdatePayment);
            request.addProperty("razorpay_order_id", order_id);
            request.addProperty("razorpay_payment_id", payment_id);
            request.addProperty("PaymentStatus","Success");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try
            {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonApi.URL);

                //Call the webservice
                androidHttpTransport.call(CommonApi.NAMESPACE+"/"+CommonApi.UpdatePayment, envelope);

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
                    JSONObject object=jsonObject.getJSONObject("UpdatePaymentResult");

                    String result=object.getString("Result");

                    int status=object.getInt("Status");

                    payment_id = object.getString("PaymentId");

                    if(status!=0)
                    {

                        Intent i = new Intent(MerchantActivity.this, LoginActivity.class);
                        startActivity(i);
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

}

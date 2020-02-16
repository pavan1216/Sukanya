package f.com.livessavers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;



import androidx.appcompat.app.AppCompatActivity;

import f.com.livessavers.Activitys.BannerRegistrationActivity;
import f.com.livessavers.Activitys.LoginActivity;
import f.com.livessavers.Activitys.NavigationActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    SharedPreferences prefs;
    String user_id,payment_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Welcome To Life Saver ");

        prefs = getSharedPreferences("USERDETAILS", MODE_PRIVATE);

        user_id = prefs.getString("USERID", "0");
        payment_status = prefs.getString("Success", "0");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(user_id.equals("0") && payment_status.equals("0"))
                {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else if(!user_id.equals("0") && payment_status.equals("0"))
                {
                    Intent i = new Intent(MainActivity.this, BannerRegistrationActivity.class);
                    startActivity(i);
                }
                else if(!user_id.equals("0") && !payment_status.equals("0"))
                {
                    Intent i = new Intent(MainActivity.this, NavigationActivity.class);
                    startActivity(i);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

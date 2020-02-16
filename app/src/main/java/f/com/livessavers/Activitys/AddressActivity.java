package f.com.livessavers.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import f.com.livessavers.R;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        getSupportActionBar().setTitle("Address");

    }
}

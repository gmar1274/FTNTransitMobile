package app.ftntransit.acbasoftware.com.ftntransit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this.getApplicationContext(),"Hello "+LoginActivity.DRIVER.getName(),Toast.LENGTH_LONG).show();
    }
}

package app.ftntransit.acbasoftware.com.ftntransit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import app.ftntransit.acbasoftware.com.ftntransit.Objects.Order;
import app.ftntransit.acbasoftware.com.ftntransit.WebServices.PendingOrdersWebTask;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Order> arrayList_pendingOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        arrayList_pendingOrders=new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PendingOrdersWebTask po = new PendingOrdersWebTask(this);
        po.execute();
        Toast.makeText(this.getApplicationContext(),"Hello "+LoginActivity.DRIVER.getName(),Toast.LENGTH_LONG).show();
    }
}

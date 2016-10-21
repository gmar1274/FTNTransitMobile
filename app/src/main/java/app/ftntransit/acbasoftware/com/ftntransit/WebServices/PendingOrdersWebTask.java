package app.ftntransit.acbasoftware.com.ftntransit.WebServices;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import app.ftntransit.acbasoftware.com.ftntransit.LoginActivity;
import app.ftntransit.acbasoftware.com.ftntransit.MainActivity;
import app.ftntransit.acbasoftware.com.ftntransit.Objects.Driver;
import app.ftntransit.acbasoftware.com.ftntransit.Objects.Encryption;
import app.ftntransit.acbasoftware.com.ftntransit.Objects.Order;
import app.ftntransit.acbasoftware.com.ftntransit.R;

/**
 * Created by user on 2016-10-20.
 */
public class PendingOrdersWebTask extends AsyncTask<Void, Void, String> {
        private  MainActivity ma;
      public  PendingOrdersWebTask(MainActivity ma) {

          this.ma=ma;

        }
        protected String doInBackground(Void... arg0) {
            try {


                String link = "http://acbasoftware.com/ftntransport/mobile/driverOrders.php";
                String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(LoginActivity.DRIVER.getUserID()+"", "UTF-8");
                data += "&" + URLEncoder.encode("driver_id", "UTF-8") + "=" + URLEncoder.encode(LoginActivity.DRIVER.getDriverID()+"", "UTF-8");
                data += "&" + URLEncoder.encode("order", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbaorderacba"), "UTF-8");
                //data += "&" + URLEncoder.encode("company_name", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbaloginacba"), "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                 return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                //return false;/// return "";
            }
            return null;
        }



        protected void onPostExecute(String result) {
            if(result==null){return;}//something went wrong. net error
            try {
               // Log.e("HERRRREEEEEE:::::::",result.toString()+" DRIVER:: "+LoginActivity.DRIVER);
                JSONObject jObject = new JSONObject(result);
                JSONArray jArray = jObject.getJSONArray("pending");
                for (int i = 0; i < jArray.length(); ++i) {
                    try {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        // Pulling items from the array
                        long on = oneObject.getLong("order_number");
                        long sd = oneObject.getLong("start_destination_id");
                        long ed = oneObject.getLong("end_destination_id");
                        BigDecimal comm = new BigDecimal(oneObject.getDouble("driver_commission"));
                        long t = oneObject.getLong("truck_id");
                        long c = oneObject.getLong("customer_id");
                        String s = oneObject.getString("size");
                        String cont = oneObject.getString("container");
                        String sl = oneObject.getString("shipping_line");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date pkdate=null;
                        try {
                            pkdate = format.parse(oneObject.getString("pickup_datetime"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String pksku=oneObject.getString("pickup_sku");
                        String delsku=oneObject.getString("delivery_sku");
                        Date lfd=null;
                        try {
                            lfd = format.parse(oneObject.getString("lfd"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        long term =oneObject.getLong("terminal");
                        this.ma.arrayList_pendingOrders.add(new Order(on,sd,ed,comm,t,c,s,cont,term,sl,pkdate,pksku,delsku,lfd));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            ArrayAdapter<Order> aa = new ArrayAdapter<Order>(this.ma.getApplicationContext(), R.layout.text_view_layout,this.ma.arrayList_pendingOrders);
            ListView listView = (ListView) this.ma.findViewById(R.id.listview_pendingOrders);
            listView.setAdapter(aa);


        }



    }

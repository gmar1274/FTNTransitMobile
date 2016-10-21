package app.ftntransit.acbasoftware.com.ftntransit.WebServices;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import app.ftntransit.acbasoftware.com.ftntransit.LoginActivity;
import app.ftntransit.acbasoftware.com.ftntransit.Objects.Encryption;

/**
 * Created by user on 2016-10-20.
 */
public class DriverOrders extends AsyncTask<Void, Void, Boolean> {
        private  String user_id;
        private String driver_id;

        DriverOrders(String user_id, String driver_id) {
            this.user_id = user_id;
            this.driver_id= driver_id;
        }
        protected Boolean doInBackground(Void... arg0) {
            try {


                String link = "http://acbasoftware.com/ftntransport/mobile/driverOrders.php";
                String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(this.user_id, "UTF-8");
                data += "&" + URLEncoder.encode("driver_id", "UTF-8") + "=" + URLEncoder.encode(this.driver_id, "UTF-8");
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


                //return true;///  return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                //return false;/// return "";
            }
            return false;
        }



        protected void onPostExecute() {

        }



    }

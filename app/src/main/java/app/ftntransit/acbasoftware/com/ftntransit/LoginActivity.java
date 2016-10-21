package app.ftntransit.acbasoftware.com.ftntransit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import app.ftntransit.acbasoftware.com.ftntransit.Objects.Driver;
import app.ftntransit.acbasoftware.com.ftntransit.Objects.Encryption;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static Driver DRIVER;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
  //  private UserLoginTask mAuthTask = null;
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public static final String PREF_USERNAME = "username";
    public static final String PREF_PASSWORD = "password";
    private UserLoginTask mAuthTask = null;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ///////////////
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        //pref.edit().putString(PREF_USERNAME,null).putString(PREF_PASSWORD,null).commit();//debug
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
       //Log.e("Preffffffffff: ",username+" "+password );
        if (username != null && password != null) {
            mAuthTask  = new UserLoginTask(username,password,this);
            mAuthTask.execute();
            return;
        }
        /////////////

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.editText_username);
        mPasswordView = (EditText) findViewById(R.id.editText_password);
        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

       }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
       // if (mAuthTask != null) {
         //   return;
       // }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (username.length()!=10) {
            mUsernameView.setError("Enter a valid phone number");
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password,this);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, String, String> {
        private  String username;
        private  String mPassword;
        private LoginActivity la;
        UserLoginTask(String user, String password, LoginActivity la) {
            username = user;
            mPassword = password;
            this.la=la;
        }
        protected String doInBackground(Void... arg0) {
        try {
            String username = this.username;
            String password =Encryption.encryptPassword(this.mPassword);

            String link = "http://acbasoftware.com/ftntransport/mobile/driverLogin.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbaloginacba"), "UTF-8");
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
                //Log.d("IN RESPONSE:::::",sb.toString());
                break;
            }
            return  sb.toString();
          //return true;///  return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
           return "";
        }
    }

        @NonNull
        private Boolean isLong(String s) {
            try{
                return Long.parseLong(s)>=0;
            }catch (Exception ee){
ee.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(String result) {
        showProgress(false);
            try {
               // Log.d("HEREEEEEE:::::",result);
                JSONObject jObject = new JSONObject(result);
                JSONArray jArray = jObject.getJSONArray("driver");
                for (int i = 0; i < jArray.length(); ++i) {
                    try {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        // Pulling items from the array
                        long user_id = oneObject.getLong("user_id");
                        long driver_id = oneObject.getLong("driver_id");
                        String f = oneObject.getString("fname");
                        String m = oneObject.getString("mname");
                        String l = oneObject.getString("lname");
                        DRIVER = new Driver(user_id,driver_id,f,m,l);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }




            ///////////////////////
        if(DRIVER!=null){
            if (pref != null) {
                pref.edit().putString(LoginActivity.PREF_USERNAME,username).putString(LoginActivity.PREF_PASSWORD, mPassword).commit();
            }//getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent intent = new Intent(this.la, MainActivity.class);
            this.la.startActivity(intent);
            return;
        }else{
           // if(mUsernameView==null || mPasswordView==null)return;
            mUsernameView.setError("Username may be incorrect");
            mPasswordView.setError("Password may be incorrect");

        }
    }


        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


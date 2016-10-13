package app.ftntransit.acbasoftware.com.ftntransit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import app.ftntransit.acbasoftware.com.ftntransit.Objects.Encryption;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static long USER_ID=-1;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
  //  private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mUsernameView;
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

        pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        //pref.edit().putString(PREF_USERNAME,null).putString(PREF_PASSWORD,null).commit();//debug
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        if (username != null && password != null) {
            mAuthTask  = new UserLoginTask(username,password);
            mAuthTask.execute();
            return;
        }
        /////////////

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.textview_phone);


        mPasswordView = (EditText) findViewById(R.id.textview_password);


        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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
            mAuthTask = new UserLoginTask(username, password);
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
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String username;
        private final String mPassword;

        UserLoginTask(String user, String password) {
            username = user;
            mPassword = password;
        }
        protected Boolean doInBackground(Void... arg0) {
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
                break;
            }

            if (isNumeric(sb.toString())){
                LoginActivity.USER_ID = Long.parseLong(sb.toString());
                return true;
            }
          //return true;///  return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
           //return false;/// return "";
        }
        return false;
    }

        private Boolean isNumeric(String s) {
            try{
                return Long.parseLong(s)>=0;
            }catch (Exception ee){
ee.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute() {
        showProgress(false);
        if(LoginActivity.USER_ID>=0){
            if (pref != null) {
                pref.edit().putString(LoginActivity.PREF_USERNAME,username).putString(LoginActivity.PREF_PASSWORD, mPassword).commit();
            }//getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           // Intent intent = new Intent(this.a, MainActivity.class);
           // this.a.startActivity(intent);
        }else{
            mUsernameView.setError("Username may be incorrect");
            mPasswordView.setError("Password may be incorrect");
            return;
        }
    }


        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


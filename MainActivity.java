package teamsanguine.sanguine;

import android.app.Activity;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    GPSManager gps;
    UserLocalStore userLocalStore;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getRegId();
        //setSupportActionBar(toolbar);
       // int timeout = 4000; // make the activity visible for 4 seconds

       /* Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                finish();
                Intent homepage = new Intent(MainActivity.this, loginscreen.class);
                startActivity(homepage);
            }
        }, timeout);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* gps = new GPSManager(MainActivity.this);
                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings

                    Toast.makeText(getApplicationContext(), "Cant Access GPS" , Toast.LENGTH_LONG).show();
                    gps.showSettingsAlert();
                }*/



            }


        });

        if(authenticate() == true){
            //go to the profile page directly.. using register page temporarily
            startActivity(new Intent(MainActivity.this, DonationHistory.class));
            Log.d("tag", "verifiedchecked");
            finish();
        }else {
            //go to login page
            startActivity(new Intent(MainActivity.this, loginscreen.class));
            Log.d("tag", "verifiednotchecked");
            finish();
        }

        //Intent homepage = new Intent(MainActivity.this, loginscreen.class);
        //startActivity(homepage);
        //finish();
        //gps.stopUsingGPS();

    }

    private boolean authenticate(){
        userLocalStore = new UserLocalStore(this);
       // userLocalStore.clearUserData();
        return userLocalStore.getUserLoggedIn();
    }

    private void getRegId() {
        GoogleCloudMessaging gcmObj;
        // Check if Google Play Service is installed in Device
        // Play services is needed to handle GCM stuffs
        //if (checkPlayServices()) {
            // Register Device in GCM Server
            getTokenInBackground();
       // }
    }

    /**
     * Method to verify google play services on the device
     * */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    //Get an authorization Token for GCM
    private void getTokenInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token;
                try {

                    InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                    token = instanceID.getToken("7994807303", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                } catch (final IOException e) {
                    token = "Error :" + e.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String msg) {
                // Add token to App Server
                addToken(msg);
                // Toast.makeText(applicationContext,"Registered with GCM Server successfully.nn"+ msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    //This will add the token to App server in the background
    private void addToken(final String token) {
        new AsyncTask<Void, Void, String>() {

            private Exception exception;
            protected String doInBackground(Void... urls) {
                try {
                    StringBuilder urlString = new StringBuilder();
                    String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
                    //http://localhost/Sanguine-Web/regGCMToken.php?DeviceId=A123324&GCMRegToken=23343
                    urlString.append("http://192.168.0.102/Sanguine-Web/regGCMToken.php?");
                    urlString.append("GCMRegToken=").append(token);
                    urlString.append("&DeviceId=").append( android_id );

                    URL url = new URL(urlString.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
            }

            protected void onPostExecute(String response) {
                if (response == null) {
                    response = "THERE WAS AN ERROR";
                }
                else
                {
                    //Toast.makeText(applicationContext,"Regn Token Updated to database", Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();
    }
}

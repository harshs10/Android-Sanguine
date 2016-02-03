package teamsanguine.sanguine;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DonationHistory extends AppCompatActivity {

    UserLocalStore userLocalStore;
    User user;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    donationdetail[] donations = new donationdetail[10];

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);


        return true;//return true so that the menu pop up is opened

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                //your code here
                userLocalStore = new UserLocalStore(this);
                userLocalStore.clearUserData();
                startActivity(new Intent(DonationHistory.this, loginscreen.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();
        Log.d("tag1", user.username);

        grabHistroy(user.username);
        for(double i = 0; i < 99999999; i++) {
        }
        Log.d("taged1", donations[0].getDate());


        setContentView(R.layout.activity_donation_history);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public void grabHistroy(String username) {

        DonationHistoryRequest1 donationHistoryRequest = new DonationHistoryRequest1(this);
        donationHistoryRequest.fetchUserDataInBackground(username, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

            }

            @Override
            public void done1(donationdetail[] donations1) {
                //donations = donations1;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        int k = 0;

        //Parsing Json
        //JSONArray jObject = new JSONArray(finalJson);
        //Log.d("tag", jObject.getJSONObject(0).getString("Date"));

        // Adding header data
        for(int i = 0; i < donations.length; i++){
            if(donations[i] != null)
                k++;
        }
        for (int i = 0; i < k; i++) {
            List<String> medical =  new ArrayList<>();
            listDataHeader.add("On " + donations[i].getDate() + " at " + donations[i].getLocation());

            medical.add("BloodPressure: " + String.valueOf(donations[i].getBloodpressure()));
            medical.add("Cholestrol: " + String.valueOf(donations[i].getCholestrol()));
            medical.add("Temperature: " + String.valueOf(donations[i].getTemperature()));

            listDataChild.put(listDataHeader.get(i), medical); // Header, Child data
        }

        // Adding child data
       /* List<String> medical =  new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            medical.add("BloodPressure: " + String.valueOf(donations[i].getBloodpressure()));
            medical.add("Cholestrol: " + String.valueOf(donations[i].getCholestrol()));
            medical.add("Temperature: " + String.valueOf(donations[i].getTemperature()));
        }

        for (int i = 0; i < 3; i++) {
            listDataChild.put(listDataHeader.get(i), medical); // Header, Child data
        }*/
    }

    public class DonationHistoryRequest1 {
        ProgressDialog progressDialog;
        public static final int CONNECTION_TIMEOUT = 1000 * 15;
        public static final String SERVER_ADDRESS = "http://deepan.netai.net/";

        public DonationHistoryRequest1(Context context){
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please wait...");
        }

        public void fetchUserDataInBackground(String username, GetUserCallback callBack){
            progressDialog.show();
            new FetchUserDataAsyncTask(username, callBack).execute();
        }

        public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
            String username;
            GetUserCallback userCallback;
            String finalJson;
            JSONArray jObject;

            public FetchUserDataAsyncTask(String username, GetUserCallback userCallback) {
                this.username = username;
                this.userCallback = userCallback;
            }

            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;

                try{
                    URL url = new URL("http://10.0.2.2/Sanguine-Web/donation_Mobile.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    StringBuilder sb = new StringBuilder();
                    sb.append(URLEncoder.encode("Username", "UTF-8"));
                    sb.append("=");
                    sb.append(URLEncoder.encode(username, "UTF-8"));

                    writer.write(sb.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    InputStream is = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder buffer = new StringBuilder();

                    String inputStr = "";
                    while ((inputStr = reader.readLine()) != null)
                        buffer.append(inputStr);

                    finalJson = buffer.toString();
                    Log.d("JsonString", finalJson);

                    jObject = new JSONArray(finalJson);
                    Log.d("JsonObject", jObject.getJSONObject(0).getString("Date"));

                    for(int i = 0; i < jObject.length(); i++){
                        JSONObject jsonObject = jObject.getJSONObject(i);
                        String date = jsonObject.getString("Date");
                        String location = jsonObject.getString("LocationUsername");
                        int cholestrol = jsonObject.getInt("Cholestrol");
                        int temperature = jsonObject.getInt("Temperature");
                        int bloodpressure = jsonObject.getInt("BloodPressure");

                        donations[i] = new donationdetail(date, location, cholestrol, temperature, bloodpressure);
                    }

                    reader.close();
                    is.close();
                    urlConnection.disconnect();

                } catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("taged", donations[0].getLocation());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                progressDialog.dismiss();
                userCallback.done(null);
                super.onPostExecute(aVoid);
            }
        }
    }
}
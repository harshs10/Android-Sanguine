package teamsanguine.sanguine;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by deepa on 11/29/2015.
 */
public class DonationHistoryRequest {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://deepan.netai.net/";

    public DonationHistoryRequest(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void fetchUserDataInBackground(String username, GetUserCallback callBack){
        progressDialog.show();
        new FetchUserDataAsyncTask(username, callBack).execute();
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, donationdetail[]> {
        String username;
        GetUserCallback userCallback;
        String finalJson;
        JSONArray jObject;

        public FetchUserDataAsyncTask(String username, GetUserCallback userCallback) {
            this.username = username;
            this.userCallback = userCallback;
        }

        @Override
        protected donationdetail[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            donationdetail[] donations = new donationdetail[10];

            try{
                URL url = new URL("http://10.0.2.2:8080/donation.php");
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
            Log.d("taged", donations[0].getDate());

            return donations;
        }

        @Override
        protected void onPostExecute(donationdetail[] donations) {

            progressDialog.dismiss();
            userCallback.done1(donations);
            super.onPostExecute(donations);
        }
    }
}

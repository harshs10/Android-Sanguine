package teamsanguine.sanguine;

/**
 * Created by Deepan on 10/15/2015.
 */
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
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

/**
 * Created by Deepan on 10/8/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://deepan.netai.net/";

    public ServerRequests(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callBack){
        progressDialog.show();
        new FetchUserDataAsyncTask(user, callBack).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try{
                URL url = new URL("http://10.0.2.2/Sanguine-Web/postdetails.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                StringBuilder sb = new StringBuilder();
                sb.append(URLEncoder.encode("Name", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(user.name, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("Username", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(user.username, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("Email", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(user.email, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("Password", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(user.password, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("PhoneNumber", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(String.valueOf(user.phone), "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("rdoAccountType", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(String.valueOf(user.type), "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("Address1", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(user.address1, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("Address2", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(user.address2, "UTF-8"));

                writer.write(sb.toString());
                writer.flush();
                writer.close();
                os.close();
                urlConnection.disconnect();
                Log.d("fls","ksdf");

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallback;

        public FetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            User returnedUser = null;

            try{
                URL url = new URL("http://10.0.2.2/Sanguine-Web/fetchdetails.php");
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
                sb.append(URLEncoder.encode(user.username, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("Password", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(user.password, "UTF-8"));

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

                String finalJson = buffer.toString();
                Log.d("tag", finalJson);
                Log.d("jksdj","sjdf");
                String json = finalJson.substring(finalJson.indexOf("{"), finalJson.lastIndexOf("}") + 1);

                JSONObject jObject = new JSONObject(json);

                if(jObject.length() == 0){
                    returnedUser = null;
                } else{
                    String name = jObject.getString("Name");
                    String email = jObject.getString("Email");
                    long phone = jObject.getLong("Phone");
                    String address1 = jObject.getString("Address1");
                    String address2 = jObject.getString("Address2");

                    returnedUser = new User(name, user.username, user.password, email, phone, address1, address2);
                }

                reader.close();
                is.close();
                urlConnection.disconnect();

            } catch (Exception e){
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {

            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}

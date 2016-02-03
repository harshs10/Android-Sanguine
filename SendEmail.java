package teamsanguine.sanguine;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Deepan on 11/3/2015.
 */
public class SendEmail {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://deepan.netai.net/";

    public SendEmail(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(String username, String email, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(username, email, userCallback).execute();

    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        String email;
        String username;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(String username, String email, GetUserCallback userCallback) {
            this.username = username;
            this.email = email;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try{
                URL url = new URL("http://10.0.2.2:8080/forgotpassword.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                StringBuilder sb = new StringBuilder();
                sb.append(URLEncoder.encode("username", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(username, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("email", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(email, "UTF-8"));

                writer.write(sb.toString());
                writer.flush();
                writer.close();
                os.close();
                urlConnection.disconnect();

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
}

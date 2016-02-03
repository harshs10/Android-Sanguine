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
public class ResetPasswordServer {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://deepan.netai.net/";

    public ResetPasswordServer(Context context){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(String onetimepass, String newpass, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(onetimepass, newpass, userCallback).execute();

    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        String onetimepass;
        String newpass;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(String onetimepass, String newpass, GetUserCallback userCallback) {
            this.onetimepass = onetimepass;
            this.newpass = newpass;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try{
                URL url = new URL("http://10.0.2.2:8080/resetpassword.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                StringBuilder sb = new StringBuilder();
                sb.append(URLEncoder.encode("onetimepassword", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(onetimepass, "UTF-8"));
                sb.append("&");
                sb.append(URLEncoder.encode("newpassword", "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(newpass, "UTF-8"));

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

package teamsanguine.sanguine;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

public class loginscreen extends AppCompatActivity implements View.OnClickListener {

    Button hello, login;
    TextView forgetpassword, signup;
    EditText etUsername, etPassword;
    UserLocalStore userLocalStore;
    CheckBox checkBox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        //hello = (Button) findViewById(R.id.userCreate);
        //hello.setOnClickListener(this);

        forgetpassword = (TextView) findViewById(R.id.forgotpassword);
        forgetpassword.setOnClickListener(this);
        signup = (TextView) findViewById(R.id.registerlink);
        signup.setOnClickListener(this);
        //Toast.makeText(getApplicationContext(), "Login Screen", Toast.LENGTH_LONG).show();

        etUsername = (EditText) findViewById(R.id.TFusername);
        etPassword = (EditText) findViewById(R.id.TFpassword);
        login = (Button) findViewById(R.id.homeScreen);
        login.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerlink:
                startActivity(new Intent(this, Register.class));
                finish();
                break;

            case R.id.forgotpassword:
                startActivity(new Intent(this, ForgotPassword.class));
                finish();
                break;

            case R.id.homeScreen:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                User user = new User(username, password);

                authenticate(user);

                break;
        }
    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null){
                    showErrorMessage();
                } else{
                    logUserIn(returnedUser);
                }
            }

            @Override
            public void done1(donationdetail[] donations1) {

            }

        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(loginscreen.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        boolean checked = checkBox.isChecked();

        if(checked) {
            userLocalStore.setUserLoggedIn(true);
            Log.d("tag1", "checked");
        }
        //should go to the profile page but going ot the register page temporarily
        startActivity(new Intent(this, DonationHistory.class));
        finish();
    }
}
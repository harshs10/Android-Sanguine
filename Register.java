package teamsanguine.sanguine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button signup;
    EditText etrepeatpass, etpassword, etusername, etphone, etemail, etname, etaddress1, etaddress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname = (EditText) findViewById(R.id.etname);
        etemail = (EditText) findViewById(R.id.etemail);
        etphone = (EditText) findViewById(R.id.etphone);
        etusername = (EditText) findViewById(R.id.etusername);
        etname = (EditText) findViewById(R.id.etname);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etrepeatpass = (EditText) findViewById(R.id.etrepeatpass);
        etaddress1 = (EditText) findViewById(R.id.etaddress1);
        etaddress2 = (EditText) findViewById(R.id.etaddress2);
        signup = (Button) findViewById(R.id.signup);

        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                Log.d("fls", "1");

                String name = etname.getText().toString();
                String username = etusername.getText().toString();
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                String address1 = etaddress1.getText().toString();
                String address2 = etaddress2.getText().toString();

                // starting validations
                Validation validation = new Validation();

                // password match check
                String repeatpassword = etrepeatpass.getText().toString();
                boolean passcheck = validation.passwordMatch(password, repeatpassword);

                //email id format check
                boolean emailcheck = validation.isValidEmail(email);

                // required fields check
                boolean emptypass = validation.isNotEmpty(etpassword);
                boolean emptyemail = validation.isNotEmpty(etemail);
                boolean emptyname = validation.isNotEmpty(etname);
                boolean emptyusername = validation.isNotEmpty(etusername);
                boolean emptyaddress = validation.isNotEmpty(etaddress1) & validation.isNotEmpty(etaddress2);
                boolean emptyphone = validation.isNotEmpty(etphone);

                // password length match
                boolean passlength = validation.passwordLength(password);

                int flag = 0;
                // doing the acutal validations
                if(passcheck == false)
                {etrepeatpass.setError("Password and Repeat Password fields don't match"); flag = 1;}

                if(passlength == false)
                {etpassword.setError("Password length should be atleast 6"); flag = 1;}

                if(emailcheck == false)
                {etemail.setError("Email ID is not valid"); flag = 1;}

                if(emptypass == false)
                {etpassword.setError("Please enter a Password"); flag = 1;}
                if(emptyemail == false)
                {etemail.setError("Please enter an Email ID"); flag = 1;}
                if(emptyname == false)
                {etname.setError("Please enter your name"); flag = 1;}
                if(emptyusername == false)
                {etusername.setError("Please enter a username"); flag = 1;}
                if(emptyaddress == false){
                    etaddress1.setError("Please enter your address");
                    etaddress2.setError("Please enter your address");
                    flag = 1;
                }
                if(emptyphone == false)
                {etphone.setError("Please enter a phone number"); flag = 1;}

                //end of validations
                if(flag == 0){
                    long phone = Long.parseLong(etphone.getText().toString());

                    User user = new User(name, username, password, email, phone, address1, address2);
                    registerUser(user);
                }
                break;
        }
    }

    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //Toast.makeText(getApplicationContext(), "Your Location", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Register.this, MainActivity.class));
                finish();
            }

            @Override
            public void done1(donationdetail[] donations1) {

            }

        });
    }
}

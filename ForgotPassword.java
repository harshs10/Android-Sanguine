package teamsanguine.sanguine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    Button send;
    EditText etusername1, etemailid2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        send = (Button) findViewById(R.id.send);
        etusername1 = (EditText) findViewById(R.id.etusername1);
        etemailid2 = (EditText) findViewById(R.id.etemailid2);

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String username = etusername1.getText().toString();
        String email = etemailid2.getText().toString();
        int flag = 0;

        Validation validation = new Validation();

        boolean emailcheck = validation.isValidEmail(email);

        boolean emptyemail = validation.isNotEmpty(etemailid2);
        boolean emptyusername = validation.isNotEmpty(etusername1);

        if (emailcheck == false) {
            etemailid2.setError("Email ID is not valid");
            flag = 1;
        }

        if (emptyemail == false) {
            etemailid2.setError("Please enter an Email ID");
            flag = 1;
        }
        if (emptyusername == false) {
            etusername1.setError("Please enter a username");
            flag = 1;
        }

        if (flag == 0)
            updatePassword(username, email);
    }

    private void updatePassword(String username, String email) {
        SendEmail sendemail = new SendEmail(this);
        sendemail.storeUserDataInBackground(username, email, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(ForgotPassword.this, ResetPassword.class));
                finish();
            }

            @Override
            public void done1(donationdetail[] donations1) {

            }
        });
    }
}

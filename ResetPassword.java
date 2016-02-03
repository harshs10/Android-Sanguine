package teamsanguine.sanguine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    Button resetpass;
    EditText etonetimepassword, etnewpassword, etrepeatpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetpass = (Button) findViewById(R.id.resetpass);
        etonetimepassword = (EditText) findViewById(R.id.etonetimepassword);
        etrepeatpassword = (EditText) findViewById(R.id.etrepeatpassword);
        etnewpassword = (EditText) findViewById(R.id.etnewpassword);

        resetpass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String onetimepass = etonetimepassword.getText().toString();
        String newpass = etnewpassword.getText().toString();
        String repeatpass = etrepeatpassword.getText().toString();

        int flag = 0;

        Validation validation = new Validation();

        boolean passcheck = validation.passwordMatch(newpass, repeatpass);
        boolean emptyonetimepass = validation.isNotEmpty(etonetimepassword);
        boolean emptynewpass = validation.isNotEmpty(etnewpassword);

        if(passcheck == false)
        {etnewpassword.setError("Password and Repeat Password fields don't match"); flag = 1;}
        if(emptynewpass == false)
        {etnewpassword.setError("Please enter a Password"); flag = 1;}
        if(emptyonetimepass == false)
        {etonetimepassword.setError("Please enter the One-time Password"); flag = 1;}

        if (flag == 0)
            resetPassword(onetimepass, newpass);
    }
    private void resetPassword(String onetimepass, String newpass) {
        ResetPasswordServer resetpasswordserver = new ResetPasswordServer(this);
        resetpasswordserver.storeUserDataInBackground(onetimepass, newpass, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(ResetPassword.this, loginscreen.class));
                finish();
            }

            @Override
            public void done1(donationdetail[] donations1) {

            }
        });
    }
}
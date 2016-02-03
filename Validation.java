package teamsanguine.sanguine;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

/**
 * Created by Deepan on 10/17/2015.
 */
public class Validation {

    // Checking password
    public boolean passwordMatch(String password, String repeatpassword){

        if(password.equals(repeatpassword))
            return true;
        else return false;
    }

    public boolean passwordLength(String password){
        if(password.trim().length() < 6)
            return false;
        else return true;
    }

    // checking email
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    // required fields
    public boolean isNotEmpty(EditText etText) {
        return (etText.getText().toString().trim().length() > 0);
    }
}

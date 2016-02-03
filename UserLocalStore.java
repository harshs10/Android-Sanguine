package teamsanguine.sanguine;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Deepan on 11/10/2015.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    //storing user details by collecting it from the database
    public void storeUserData(User user){
       SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("Name", user.name);
        spEditor.putString("Username", user.username);
        spEditor.putString("Password", user.password);
        spEditor.putString("Email", user.email);
        spEditor.putLong("Phone", user.phone);
        spEditor.putString("Address1", user.address1);
        spEditor.putString("Address2", user.address2);
        spEditor.commit();
    }

    //get the user details when needed
    public User getLoggedInUser(){
        String name = userLocalDatabase.getString("Name", "");
        String username = userLocalDatabase.getString("Username", "");
        String password = userLocalDatabase.getString("Password", "");
        String email = userLocalDatabase.getString("Email", "");
        Long phone = userLocalDatabase.getLong("Phone", -1);
        String address1 = userLocalDatabase.getString("Address1", "");
        String address2 = userLocalDatabase.getString("Address2", "");

        User storedUser = new User(name, username, password, email, phone, address1, address2);

        return storedUser;
    }

    // user logged in or not?
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.commit();
    }

    //check if user is logged in
    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("LoggedIn", false) == true){
            return true;
        }else{
            return false;
        }
    }
    //clear the local data when user logs out
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}

package teamsanguine.sanguine;

/**
 * Created by Deepan on 10/15/2015.
 */
public class User {
    String name, username, password, email, address1, address2;
    long phone;
    int type;

    public User(String name, String username, String password, String email, long phone, String address1, String address2){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this. phone = phone;
        this.type = 1;
        this.address1 = address1;
        this.address2 = address2;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}


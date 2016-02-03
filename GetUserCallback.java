package teamsanguine.sanguine;

import org.json.JSONArray;

/**
 * Created by Deepan on 10/15/2015.
 */
interface GetUserCallback {

    public abstract void done(User returnedUser);
    public abstract void done1(donationdetail[] donations1);
}

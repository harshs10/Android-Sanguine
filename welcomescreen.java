package teamsanguine.sanguine;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
/**
 * Created by Harsh on 10/8/2015.
 */
public class welcomescreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomescreen);
        String username = getIntent().getStringExtra("Username");
        TextView tv = (TextView) findViewById(R.id.TVusername);
        tv.setText(username);
    }
}
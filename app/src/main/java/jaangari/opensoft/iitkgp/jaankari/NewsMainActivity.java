package jaangari.opensoft.iitkgp.jaankari;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import jaangari.opensoft.iitkgp.jaangari.R;

public class NewsMainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        TextView tv=(TextView) findViewById(R.id.MainNewsTitle);
        tv.setText(b.getString("title"));
        tv = (TextView) findViewById(R.id.MainNewsLocation);
        tv.setText(b.getString("location"));
        tv = (TextView) findViewById(R.id.MainNewsBody);
        tv.setText(b.getString("text"));

        setContentView(R.layout.activity_news_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.Commodity;

public class CommodityActivity extends ActionBarActivity {
    DatabaseHandler db;
    List<Commodity> values;
    String[] names;
    String[] min_prices;
    String[] max_prices;

    private class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final String[] names;
        private final String[] min_prices;
        private final String[] max_prices;

        public CustomList(Activity context, String[] names, String[] min_prices, String[] max_prices) {
            super(context, R.layout.custom_list_view_commodity, names);
            this.context = context;
            this.min_prices = min_prices;
            this.names = names;
            this.max_prices = max_prices;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.custom_list_view_commodity, null, true);
            TextView nameView = (TextView) rowView.findViewById(R.id.commName);
            TextView maxPriceView = (TextView) rowView.findViewById(R.id.maxPrice);
            TextView minPriceView = (TextView) rowView.findViewById(R.id.minPrice);
            nameView.setText(names[position]);
            maxPriceView.setText(max_prices[position]);
            minPriceView.setText(min_prices[position]);
            return rowView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        db = new DatabaseHandler(this.getApplicationContext());
        ListView listview = (ListView) findViewById(R.id.commo_listView);
        Commodity c = new Commodity("M001","Rice","23.2","23.1");
        db.addCommodity(c);
        values = db.getCommodityPrices();
        Log.v("Commodity", ""+values.size());
        names = new String[values.size()];
        min_prices = new String[values.size()];
        max_prices = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            names[i] = values.get(i).getName();
            min_prices[i] = values.get(i).getMin();
            max_prices[i] = values.get(i).getMax();
        }
        CustomList adapter = new CustomList(this, names, min_prices, max_prices);
        listview.setAdapter(adapter);
        Log.e("Commodity-Activity", values.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_commodity, menu);
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

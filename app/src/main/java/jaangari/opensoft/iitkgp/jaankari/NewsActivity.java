package jaangari.opensoft.iitkgp.jaankari;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import se.emilsjolander.flipview.FlipView;

class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> newsitems) {
        super(context, 0, newsitems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        News nitem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.newsTitleUp);
        TextView tvHome = (TextView) convertView.findViewById(R.id.newsSummaryUp);

        TextView tv1Name = (TextView) convertView.findViewById(R.id.newsTitleDown);
        TextView tv1Home = (TextView) convertView.findViewById(R.id.newsSummaryDown);
        // Populate the data into the template view using the data object
        tvName.setText(nitem.getTitle());
        tvHome.setText(nitem.getSummary());

        //System.out.println(nitem.getTitle()+nitem.getSummary());

        tv1Name.setText(nitem.getTitle());
        tv1Home.setText(nitem.getSummary());

//        View up=(View) convertView.findViewById(R.id.NewsListUpperLayout);
//
//        up.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),NewsMainActivity.class);
//                startActivity(intent);
//            }
//        });

        // Return the completed view to render on screen
        return convertView;
    }
}


public class NewsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FlipView flipView = (FlipView) findViewById(R.id.flip_view);
        ArrayList<News> news_items_list = new ArrayList<News>();
        NewsAdapter adapter = new NewsAdapter(this, news_items_list);

        News nit = new News(1,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
        adapter.add(nit);
        nit = new News(2,"DEF","WEFASF0","sfewa dsad sad fasdf","we ewr ","We sadfasd");
        adapter.add(nit);
        nit = new News(3,"Aasdf we ","Swqre terSF0","SD sad fasdf asdfwe rsaf asdfasdf","asdf sdf","sd fweasd");
        adapter.add(nit);
        nit = new News(4,"E aefsde","Swe rwadsfa F0","asWe wea dfdf","asdf sdf","sd fweasd");
        adapter.add(nit);
        nit = new News(5,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
        adapter.add(nit);
        nit = new News(6,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
        adapter.add(nit);

        flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        flipView.setAdapter(adapter);
    }

    public void newsClickedUp(View view){
        FlipView flipView = (FlipView) findViewById(R.id.flip_view);
        int pos = flipView.getCurrentPage();
        ListAdapter ad = flipView.getAdapter();
        News n = (News)ad.getItem(pos);
        System.out.println(n.getTitle());
        Intent intent = new Intent(getApplicationContext(),NewsMainActivity.class);
        intent.putExtra("title",n.getTitle());
        intent.putExtra("text",n.getText());
        intent.putExtra("location",n.getPlace());
        startActivity(intent);
    }


    public void newsClickedDown(View view){
        Intent intent = new Intent(getApplicationContext(),NewsMainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
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

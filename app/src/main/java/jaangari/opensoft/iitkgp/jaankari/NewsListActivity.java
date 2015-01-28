package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.NewsPair;
import se.emilsjolander.flipview.FlipView;


class NewsAdapter extends ArrayAdapter<NewsPair> {
    public NewsAdapter(Context context, ArrayList<NewsPair> newsitems) {
        super(context, 0, newsitems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NewsPair npair;
        npair = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.newsTitleUp);
        TextView tvHome = (TextView) convertView.findViewById(R.id.newsSummaryUp);
        tvName.setText(npair.getFirst().getTitle());
        tvHome.setText(npair.getFirst().getSummary());
        if(npair.getSecond() != null) {
            TextView tv1Name = (TextView) convertView.findViewById(R.id.newsTitleDown);
            TextView tv1Home = (TextView) convertView.findViewById(R.id.newsSummaryDown);
            // Populate the data into the template view using the data object

            //System.out.println(nitem.getTitle()+nitem.getSummary());

            tv1Name.setText(npair.getSecond().getTitle());
            tv1Home.setText(npair.getSecond().getSummary());
        }
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


public class NewsListActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ListView mDrawerList;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mDrawerList = (ListView) findViewById(R.id.news_list_nav_menu);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, new String[]{
                    getString(R.string.news_section_1),
                    getString(R.string.news_section_2),
                    getString(R.string.news_section_3),
                    getString(R.string.news_section_4),
                    getString(R.string.news_section_5),
                    getString(R.string.news_section_6),
                    getString(R.string.news_section_7)
                    }));
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FlipView flipView = (FlipView) findViewById(R.id.flip_view);
        ArrayList<NewsPair> news_items_list = new ArrayList<NewsPair>();
        NewsAdapter adapter = new NewsAdapter(this, news_items_list);

        News nit1 = new News(1,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf",0);
        News nit2 = new News(2,"DEF","WEFASF0. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf","sfewa dsad sad fasdf","we ewr ",1);
        adapter.add(new NewsPair(nit1,nit2));
        nit1 = new News(3,"Aasdf we ","Swqre terSF0","SD sad fasdf asdfwe rsaf asdfasdf","asdf sdf",2);
        nit2 = new News(4,"E aefsde","Swe rwadsfa F0","asWe wea dfdf","asdf sdf",0);
        adapter.add(new NewsPair(nit1,nit2));
        nit1 = new News(5,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf",1);
        nit2 = new News(6,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf",1);
        adapter.add(new NewsPair(nit1,nit2));


        flipView.setAdapter(adapter);
    }

    public void newsClickedUp(View view){
        FlipView flipView = (FlipView) findViewById(R.id.flip_view);

        int pos = flipView.getCurrentPage();
        ListAdapter ad = flipView.getAdapter();
        NewsPair np = (NewsPair) ad.getItem(pos);
        News n = np.getFirst();
        System.out.println(n.getTitle());
        Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
        intent.putExtra("title", n.getTitle());
        intent.putExtra("text", n.getText());
        intent.putExtra("location", n.getPlace());
        startActivity(intent);
    }


    public void newsClickedDown(View view) {
        FlipView flipView = (FlipView) findViewById(R.id.flip_view);

        int pos = flipView.getCurrentPage();
        ListAdapter ad = flipView.getAdapter();
        NewsPair np = (NewsPair) ad.getItem(pos);
        News n = np.getSecond();
        if (n != null) {
            System.out.println(n.getTitle());
            Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
            intent.putExtra("title", n.getTitle());
            intent.putExtra("text", n.getText());
            intent.putExtra("location", n.getPlace());
            startActivity(intent);
        } else return;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.news_section_1);
                break;
            case 2:
                mTitle = getString(R.string.news_section_2);;
                break;
            case 3:
                mTitle = getString(R.string.news_section_3);
                break;
            case 4:
                mTitle = getString(R.string.news_section_4);
                break;
            case 5:
                mTitle = getString(R.string.news_section_5);
                break;
            case 6:
                mTitle = getString(R.string.news_section_6);
                break;
            case 7:
                mTitle = getString(R.string.news_section_7);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.news_list, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NewsListActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

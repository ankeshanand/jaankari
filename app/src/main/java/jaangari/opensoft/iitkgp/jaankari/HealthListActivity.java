package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
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
import android.widget.TextView;

import java.util.ArrayList;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.Health;
import jaangari.opensoft.iitkgp.jaankari.util.HealthPair;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.NewsPair;
import se.emilsjolander.flipview.FlipView;

class HealthAdapter extends ArrayAdapter<HealthPair> {
    public HealthAdapter(Context context, ArrayList<HealthPair> newsitems) {
        super(context, 0, newsitems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HealthPair hpair;
        hpair = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_health, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.healthTitleUp);
        TextView tvHome = (TextView) convertView.findViewById(R.id.healthSummaryUp);
        tvName.setText(hpair.getFirst().getTitle());
        tvHome.setText(hpair.getFirst().getText());
        if (hpair.getSecond() != null) {
            TextView tv1Name = (TextView) convertView.findViewById(R.id.healthTitleDown);
            TextView tv1Home = (TextView) convertView.findViewById(R.id.healthSummaryDown);
            // Populate the data into the template view using the data object

            //System.out.println(nitem.getTitle()+nitem.getSummary());

            tv1Name.setText(hpair.getSecond().getTitle());
            tv1Home.setText(hpair.getSecond().getText());
        }
        return convertView;
    }
}

public class HealthListActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_list);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FlipView flipView = (FlipView) findViewById(R.id.flip_view_health);
        ArrayList<HealthPair> health_items_list = new ArrayList<HealthPair>();
        HealthAdapter adapter = new HealthAdapter(this, health_items_list);

        Health hit1 = new Health(1,"ABC","sd sdf asd sad fasdf");
        Health hit2 = new Health(2,"DEF","WEFASF0. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf");
        adapter.add(new HealthPair(hit1,hit2));
        hit1 = new Health(3,"Aasdf we ","SD sad fasdf asdfwe rsaf asdfasdf");
        hit2 = new Health(4,"E aefsde","asWe wea dfdf");
        adapter.add(new HealthPair(hit1,hit2));
//        nit1 = new News(5,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
//        nit2 = new News(6,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
//        adapter.add(new NewsPair(nit1,nit2));


        flipView.setAdapter(adapter);


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
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
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
            getMenuInflater().inflate(R.menu.health_list, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_health_list, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HealthListActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

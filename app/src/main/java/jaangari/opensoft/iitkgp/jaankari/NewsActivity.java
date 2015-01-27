package jaangari.opensoft.iitkgp.jaankari;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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


public class NewsActivity extends ActionBarActivity {
    //implements NavigationDrawerFragment.NavigationDrawerCallbacks {

        /**
         * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
         */
//        private NavigationDrawerFragment mNavigationDrawerFragment;

        /**
         */
        private CharSequence mTitle;



//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_news);
//
////            mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//            mTitle = getTitle();

//            mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

//<<<<<<< HEAD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FlipView flipView = (FlipView) findViewById(R.id.flip_view);
        ArrayList<NewsPair> news_items_list = new ArrayList<NewsPair>();
        NewsAdapter adapter = new NewsAdapter(this, news_items_list);

        News nit1 = new News(1,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
        News nit2 = new News(2,"DEF","WEFASF0. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf","sfewa dsad sad fasdf","we ewr ","We sadfasd");
        adapter.add(new NewsPair(nit1,nit2));
        nit1 = new News(3,"Aasdf we ","Swqre terSF0","SD sad fasdf asdfwe rsaf asdfasdf","asdf sdf","sd fweasd");
        nit2 = new News(4,"E aefsde","Swe rwadsfa F0","asWe wea dfdf","asdf sdf","sd fweasd");
        adapter.add(new NewsPair(nit1,nit2));
        nit1 = new News(5,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
        nit2 = new News(6,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf","sd fweasd");
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


    public void newsClickedDown(View view){
        FlipView flipView = (FlipView) findViewById(R.id.flip_view);

        int pos = flipView.getCurrentPage();
            ListAdapter ad = flipView.getAdapter();
            NewsPair np = (NewsPair) ad.getItem(pos);
            News n = np.getSecond();
        if(n!=null){
            System.out.println(n.getTitle());
            Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
            intent.putExtra("title", n.getTitle());
            intent.putExtra("text", n.getText());
            intent.putExtra("location", n.getPlace());
            startActivity(intent);
        }
        else return;
//=======
//        }
//
//        @Override
//        public void onNavigationDrawerItemSelected(int position) {
//            Log.v(PRINT_SERVICE, "Fragment drawer");
//            FragmentNews fragment = null;
//            // update the main content by replacing fragments
//            switch (position){
//                case 0:
//                    fragment = new FragmentNews();
//                    break;
//                case 1:
//                    fragment = new FragmentNews();
//                    break;
//                case 2:
//                    fragment = new FragmentNews();
//                    break;
//            }
//
//            if(fragment!=null){
//                fragment.setPosition(position);
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment).commit();
//            }
//            else {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                        .commit();
//            }
//        }
//
//    public void onSectionAttached(int number) {
//        Log.v(PRINT_SERVICE,"onSectionAttached : " +number );
//        switch (number) {
//            case 1:
//                mTitle = getString(R.string.news_title_section1);
//                break;
//            case 2:
//                mTitle = getString(R.string.news_title_section2);
//                break;
//            case 3:
//                mTitle = getString(R.string.news_title_section3);
//                break;
//        }
//    }
//
//    public void restoreActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(mTitle);
//        Log.v(PRINT_SERVICE,"restoreActionBar" );
//>>>>>>> 97c69a16c6d788da857851c8c294dfb92102241d
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_news, menu);
//            restoreActionBar();
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.logout_video_screen){
            SharedPreferences sp=getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("sLogin",null);
            Ed.putString("emailId",null);
            Ed.putString("proPic",null);
            Ed.commit();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
//=======
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_news, container, false);
//            return rootView;
//        }
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            ((VideoActivity) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
//        }
//    }
//
//}
//>>>>>>> 97c69a16c6d788da857851c8c294dfb92102241d

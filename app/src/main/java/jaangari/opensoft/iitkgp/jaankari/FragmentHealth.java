package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.Health;
import jaangari.opensoft.iitkgp.jaankari.util.HealthPair;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.NewsPair;
import se.emilsjolander.flipview.FlipView;

/**
 * Created by menirus on 28/1/15.
 */
public class FragmentHealth extends Fragment {

    private int position;
    private float rating_user;
    private View parentLayout;
    DatabaseHandler db;
    private String TAG = "FragmentNews";

    public void setPosition(int position) {
        this.position = position;
    }

    private class HealthAdapter extends ArrayAdapter<HealthPair> {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentLayout = inflater.inflate(R.layout.fragment_health_list, container, false);
        db = new DatabaseHandler(this.getActivity().getApplicationContext());
        FlipView flipview = (FlipView) parentLayout.findViewById(R.id.flip_view_health);
        List<Health> nlist = db.getAllHealth();
//        Log.e(TAG, videos.toString());


        //Get health
        //List<Health> nlist = new ArrayList<Health>();
        Health second = null;
        ArrayList<HealthPair> news_pair_list = new ArrayList<HealthPair>();
        for(int i=0; i<nlist.size(); i+=2){
            second = null;
            if(i+1 != nlist.size()) second=nlist.get(i+1);
            news_pair_list.add(new HealthPair(nlist.get(i),second));

        }





        HealthAdapter adapter = new HealthAdapter(this.getActivity(), news_pair_list);

//        Health hit1 = new Health(1,"ABC","sd sdf asd sad fasdf");
//        Health hit2 = new Health(2,"DEF","WEFASF0. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf");
//        adapter.add(new HealthPair(hit1,hit2));
//        hit1 = new Health(3,"Aasdf we ","SD sad fasdf asdfwe rsaf asdfasdf");
//        hit2 = new Health(4,"E aefsde","asWe wea dfdf");
//        adapter.add(new HealthPair(hit1,hit2));

        flipview.setAdapter(adapter);
        db.closeDB();
        return parentLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((NewsActivity) activity).onSectionAttached(position + 1);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

}

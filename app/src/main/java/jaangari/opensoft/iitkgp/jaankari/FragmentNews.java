package jaangari.opensoft.iitkgp.jaankari;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.News;
import jaangari.opensoft.iitkgp.jaankari.util.NewsPair;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;
import se.emilsjolander.flipview.FlipView;

/**
 * Created by rahulanishetty on 1/27/15.
 */

public class FragmentNews extends Fragment {

    String[] values;
    Bitmap[] imageId;
    float[] rating;
    List<Videos> videos;

    private boolean video_set = false;
    private int video_id;
    private int video_history;
    private int position;
    private float rating_user;
    private View parentLayout;
    DatabaseHandler db;
    private String TAG = "FragmentNews";

    public void setPosition(int position) {
        this.position = position;
    }

    private class NewsAdapter extends ArrayAdapter<NewsPair> {
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
                tv1Name.setText(npair.getSecond().getTitle());
                tv1Home.setText(npair.getSecond().getSummary());
            }
            return convertView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentLayout = inflater.inflate(R.layout.fragment_news_list, container, false);
        db = new DatabaseHandler(this.getActivity().getApplicationContext());
        FlipView flipview = (FlipView) parentLayout.findViewById(R.id.flip_view);
        List<News> nlist = db.getAllNewsbyCategory(position);
//        Log.e(TAG, videos.toString());
        News second = null;
        ArrayList<NewsPair> news_pair_list = new ArrayList<NewsPair>();
        for(int i=0; i<nlist.size(); i+=2){
            second = null;
            if(i+1 != nlist.size()) second=nlist.get(i+1);
            news_pair_list.add(new NewsPair(nlist.get(i),second));

        }





        NewsAdapter adapter = new NewsAdapter(this.getActivity(), news_pair_list);

        News nit1 = new News(1,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf",1);
        News nit2 = new News(2,"DEF","WEFASF0. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf. sfewa dsad sad fasdf","sfewa dsad sad fasdf","we ewr ",1);
        adapter.add(new NewsPair(nit1,nit2));
        nit1 = new News(3,"Aasdf we ","Swqre terSF0","SD sad fasdf asdfwe rsaf asdfasdf","asdf sdf",1);
        nit2 = new News(4,"E aefsde","Swe rwadsfa F0","asWe wea dfdf","asdf sdf",0);
        adapter.add(new NewsPair(nit1,nit2));
        nit1 = new News(5,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf",2);
        nit2 = new News(6,"ABC","SEFDSF0","asd sdf asd sad fasdf","asdf sdf",1);
        adapter.add(new NewsPair(nit1,nit2));

        flipview.setAdapter(adapter);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                String path = videos.get(position).getPath();
//                File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/Videos/" + path.substring(path.lastIndexOf("/")));
//                Log.e(TAG, Environment.getExternalStorageDirectory() + getString(R.string.app_name) + "/Videos/" + path.substring(path.lastIndexOf("/")));
//                intent.setDataAndType(Uri.fromFile(file), "video/*");
//                if(videos.get(position).getIsRated()==0){
//                    video_set = true;
//                }
//                video_id = videos.get(position).getID();
//                video_history = videos.get(position).getHistory();
//                startActivity(intent);
//            }
//        });
        db.closeDB();
        return parentLayout;
    }

//    public void newsClickedUp(View view){
//        FlipView flipView = (FlipView) this.getActivity().findViewById(R.id.flip_view);
//
//        int pos = flipView.getCurrentPage();
//        ListAdapter ad = flipView.getAdapter();
//        NewsPair np = (NewsPair) ad.getItem(pos);
//        News n = np.getFirst();
//        System.out.println(n.getTitle());
//        Intent intent = new Intent(this.getActivity().getApplicationContext(), NewsMainActivity.class);
//        intent.putExtra("title", n.getTitle());
//        intent.putExtra("text", n.getText());
//        intent.putExtra("location", n.getPlace());
//        startActivity(intent);
//    }
//
//
//    public void newsClickedDown(View view) {
//        FlipView flipView = (FlipView) this.getActivity().findViewById(R.id.flip_view);
//
//        int pos = flipView.getCurrentPage();
//        ListAdapter ad = flipView.getAdapter();
//        NewsPair np = (NewsPair) ad.getItem(pos);
//        News n = np.getSecond();
//        if (n != null) {
//            System.out.println(n.getTitle());
//            Intent intent = new Intent(this.getActivity().getApplicationContext(), NewsMainActivity.class);
//            intent.putExtra("title", n.getTitle());
//            intent.putExtra("text", n.getText());
//            intent.putExtra("location", n.getPlace());
//            startActivity(intent);
//        } else return;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((NewsActivity) activity).onSectionAttached(position + 1);
    }

    @Override
    public void onResume() {

        Log.e("DEBUG", "onResume of VideoFragment");
        super.onResume();
    }
    public void updateDb(float rating) {
        DatabaseHandler db = new DatabaseHandler(this.getActivity().getApplicationContext());
        video_history++;
        //video_id
        if(rating!=-1){
            db.updateRated(video_id);
        }
        db.updateVideoHistory(video_id,video_history);
        db.closeDB();
    }
}

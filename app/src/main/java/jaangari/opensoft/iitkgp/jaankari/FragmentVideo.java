package jaangari.opensoft.iitkgp.jaankari;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.Videos;

/**
 * Created by rahulanishetty on 1/27/15.
 */

public class FragmentVideo extends Fragment {

    String[] values;
    Bitmap[] imageId;
    float[] rating;
    List<Videos> videos;
    private boolean video_set=false;
    private int video_id;
    private int position;
    private float rating_user;
    private View parentLayout;
    DatabaseHandler db;
    private String TAG = "FragmentVideo";
    public void setPosition(int position){
        this.position = position;
    }

    private class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final String[] web;
        private final Bitmap[] imageId;
        private final float[] rating;
        public CustomList(Activity context,String[] web, Bitmap[] imageId,float[] rating) {
            super(context, R.layout.custom_list_view, web);
            this.context = context;
            this.web = web;
            this.imageId = imageId;
            this.rating = rating;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.custom_list_view, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.list_text_view);
            RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rating_bar);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image_view);
            txtTitle.setText(web[position]);
            imageView.setImageBitmap(imageId[position]);
            ratingBar.setRating((float)(rating[position]));
            return rowView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        parentLayout = inflater.inflate(R.layout.fragment_video, container, false);
        db = new DatabaseHandler(this.getActivity().getApplicationContext());
        ListView listview = (ListView) parentLayout.findViewById(R.id.videos_list_view);
        videos = db.getAllVideosbyCategory(position);
        Log.e(TAG,videos.toString());
        values = new String[videos.size()];
        imageId = new Bitmap[videos.size()];
        rating = new float[videos.size()];
        for(int i=0;i<videos.size();i++) {
            values[i] = videos.get(i).getName();
            String path = videos.get(i).getPath();
            rating[i] = videos.get(i).getRating();
            imageId[i] = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name)+"/Videos/"
                    + path.substring(path.lastIndexOf("/")+1,path.lastIndexOf(".mp4"))+"_thumbnail.jpg");
        }
        CustomList adapter = new CustomList(this.getActivity(),values,imageId,rating);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                String path = videos.get(position).getPath();
                File file = new File(Environment.getExternalStorageDirectory()+"/"+getString(R.string.app_name)+"/Videos/"+path.substring(path.lastIndexOf("/")));
                Log.e(TAG,Environment.getExternalStorageDirectory()+getString(R.string.app_name)+"/Videos/"+path.substring(path.lastIndexOf("/")));
                intent.setDataAndType(Uri.fromFile(file), "video/*");
                video_set = true;
                video_id = videos.get(position).getID();
                startActivity(intent);
            }
        });
        db.closeDB();
        return parentLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((VideoActivity) activity).onSectionAttached(position+1);
    }

    @Override
    public void onResume(){
        if(video_set){
            video_set = false;
            LinearLayout llRating = new LinearLayout(this.getActivity());
//            llRating.setPadding(60,0,0,0);
            llRating.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            AlertDialog.Builder builder = new AlertDialog.Builder(parentLayout.getContext(),AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            RatingBar ratingBar = new RatingBar(this.getActivity().getApplicationContext());
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    rating_user = rating;
                    Log.e(TAG,"Rating : " + rating_user);
                }
            });
            ratingBar.setRating(0);
            ratingBar.setStepSize(0.1f);
            ratingBar.setNumStars(5);
            ratingBar.setLayoutParams(params);
            builder.setTitle("Rate Video");
            llRating.addView(ratingBar);
            builder.setView(llRating);
            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.e(TAG,"done" + which);
                }
            });
            builder.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rating_user = -1;
                }
            });
            builder.show();
        }

//        final CharSequence[] items = {"Take Photo", "Choose from Library","Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(parentLayout.getContext());
//        builder.setTitle("Add profile picture");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item){
//                if(items[item].equals("Take Photo")){
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent,100);
//                }
//                else if(items[item].equals("Choose from Library")){
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, 101);
//                }
//                else if(items[item].equals("Cancel")){
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
        Log.e("DEBUG", "onResume of VideoFragment");
        super.onResume();
    }

}

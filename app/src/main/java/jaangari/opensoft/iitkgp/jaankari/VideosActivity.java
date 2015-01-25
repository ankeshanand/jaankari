package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import jaangari.opensoft.iitkgp.jaangari.R;

public class VideosActivity extends Activity {
    String[] values,paths;
    Bitmap[] imageId;
    File[] files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        final ListView listview = (ListView) findViewById(R.id.videos_list_view);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/Videos");
        if (file.exists()) {
            files = file.listFiles();
            values = new String[files.length];
            paths = new String[files.length];
            imageId = new Bitmap[files.length];
            for (int i = 0; i < files.length; i++) {
                values[i] = files[i].getName().replace("_", " ");
                paths[i] = files[i].getAbsolutePath();
                imageId[i] = ThumbnailUtils.createVideoThumbnail(paths[i],MediaStore.Images.Thumbnails.MINI_KIND);
            }
            CustomList adapter = new CustomList(VideosActivity.this,values, imageId);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(VideosActivity.this, "You Clicked at " + values[+position], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class CustomList extends ArrayAdapter<String>{
        private final Activity context;
        private final String[] web;
        private final Bitmap[] imageId;
        public CustomList(Activity context,String[] web, Bitmap[] imageId) {
            super(context, R.layout.custom_list_view, web);
            this.context = context;
            this.web = web;
            this.imageId = imageId;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.custom_list_view, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.list_text_view);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image_view);
            txtTitle.setText(web[position]);
            imageView.setImageBitmap(imageId[position]);
//            videoView.setVideoPath(videoId[position]);
            return rowView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_videos, menu);
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

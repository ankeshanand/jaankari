package jaangari.opensoft.iitkgp.jaankari;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jaangari.opensoft.iitkgp.jaangari.R;

public class UpdateProfilePicActivity extends Activity {

    private static final int SELECT_PHOTO = 100;
    private static final int REQUEST_CAMERA = 101;

    public void imagePick(View view){
        final CharSequence[] items = {"Take Photo", "Choose from Library","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfilePicActivity.this);
        builder.setTitle("Add profile picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item){
                if(items[item].equals("Take Photo")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent,REQUEST_CAMERA);
                }
                else if(items[item].equals("Choose from Library")){
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }
                else if(items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void writeProfilePic(Bitmap yourSelectedImage){
        String path = android.os.Environment.getExternalStorageDirectory() + File.separator + R.string.app_name;
        OutputStream fOut = null;
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        path += File.separator + "proPics.jpg";
        file = new File(path);
        try {
            fOut = new FileOutputStream(file);
            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            SharedPreferences sp = getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("proPic", path);
            Ed.commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void setProfilePic(Bitmap yourSelectedImage , boolean set){
//        ImageView mImageView;
//        mImageView = (ImageView) findViewById(R.id.profile_pic);
////        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
////        params.height = (int) (yourSelectedImage.getHeight());
////        params.width = (int) (yourSelectedImage.getWidth());
////        Log.v(PRINT_SERVICE, "height :" + params.height + " , width : " + params.width);
////        mImageView.setLayoutParams(params);
//        mImageView.setImageBitmap(yourSelectedImage);
//        if(set) {
//            writeProfilePic(yourSelectedImage);
//        }
//    }

 //   @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch(requestCode) {
//            case REQUEST_CAMERA:
//                File f = new File(Environment.getExternalStorageDirectory().toString()+"/temp.jpg");
//                try {
//                    Uri selectedImage = Uri.fromFile(f);
//                    Bitmap yourSelectedImage = decodeUri(selectedImage);
//                    setProfilePic(yourSelectedImage,true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case SELECT_PHOTO:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    Bitmap yourSelectedImage = null;
//                    try {
//                        yourSelectedImage = decodeUri(selectedImage);
//                        setProfilePic(yourSelectedImage,true);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//        }
//    }


//    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
//
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
//
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 140;
//
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp / 2 < REQUIRED_SIZE
//                    || height_tmp / 2 < REQUIRED_SIZE) {
//                break;
//            }
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_pic);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_profile_pic, menu);
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

package jaangari.opensoft.iitkgp.jaankari;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jaangari.opensoft.iitkgp.jaangari.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private static final int SELECT_PHOTO = 100;
    private static final int REQUEST_CAMERA = 101;
    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWD_PATTERN= "(?=.*[0-9])(?=.*[a-z])" +
                                                "(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

    private UserLoginTask mAuthTask = null;
    private DownloadTask mDownloadTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private ImageView mImageView;
    private MainFragment mainFragment;

//    public void imagePick(View view){
//        final CharSequence[] items = {"Take Photo", "Choose from Library","Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//        builder.setTitle("Add profile picture");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item){
//                if(items[item].equals("Take Photo")){
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent,REQUEST_CAMERA);
//                }
//                else if(items[item].equals("Choose from Library")){
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//                }
//                else if(items[item].equals("Cancel")){
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }

//    private void writeProfilePic(Bitmap yourSelectedImage){
//        String path = android.os.Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name);
//        OutputStream fOut = null;
//        File file = new File(path);
//        if (!file.exists())
//            file.mkdirs();
//        path += File.separator + "proPics.jpg";
//        file = new File(path);
//        try {
//            fOut = new FileOutputStream(file);
//            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//            SharedPreferences sp = getSharedPreferences("Login", 0);
//            SharedPreferences.Editor Ed = sp.edit();
//            Ed.putString("proPic", path);
//            Ed.commit();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void setProfilePic(Bitmap yourSelectedImage , boolean set){
//        mImageView = (ImageView) findViewById(R.id.profile_pic);
//        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
//        params.height = 200;
//        params.width = 200;
//        mImageView.setLayoutParams(params);
//        mImageView.setImageBitmap(yourSelectedImage);
//        if(set) {
//            writeProfilePic(yourSelectedImage);
//        }
//    }

//    @Override
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
//        final int REQUIRED_SIZE = 300;
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

//        if(AppStatus.getInstance(this).isOnline()){
//            Intent intent_service = new Intent(getApplicationContext(),GlobalDatabaseImageService.class);
//            startService(intent_service);
//        }

        if(savedInstanceState==null){
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        }
        else{
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        String sLogin = sp1.getString("sLogin", null);
        String path = sp1.getString("proPic",null);

        // todo : Remove the following hardcoded true
        if (sLogin != null || true) {
            if (true || sLogin.equals("true")) {
                mDownloadTask = new DownloadTask();
                mDownloadTask.execute((Void) null);

                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(intent);
            }
        } else {
            setContentView(R.layout.activity_login);
            mDownloadTask = new DownloadTask();
            mDownloadTask.execute((Void) null);

//            if(path!=null){
//                try {
//                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(path);
//                    setProfilePic(yourSelectedImage,false);
//                }catch(Exception e){
//                    Log.e("Profile Pic path","Error Could not find file at " + path);
//                    e.printStackTrace();
//                }
//            }
            // Set up the login form.
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });
            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }
    }


    public void loginButton(View view){
        Log.v(PRINT_SERVICE, "Attempting Login");
        if (AppStatus.getInstance(this).isOnline()){
            attemptLogin();
        }
        else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Internet Connection not Available");
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        Log.v(PRINT_SERVICE,"In Login");
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        /*
        The password policy is:
                At least 8 chars
                Contains at least one digit
                Contains at least one lower alpha char and one upper alpha char
                Contains at least one char within a set of special chars (@#%$^ etc.)
                Does not contain space, tab, etc.
         */
        pattern = Pattern.compile(PASSWD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }



    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class DownloadTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            Intent intent_service = new Intent(getApplicationContext(),GlobalDatabaseImageService.class);
            startService(intent_service);
            return true;
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                HttpClient httpClient = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost("http://10.132.235.67:3000/userLogin");
                HttpPost httpPost = new HttpPost("http://"+getString(R.string.ip_address)+"login.php");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("email", mEmail));
                nameValuePair.add(new BasicNameValuePair("passwd",mPassword));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                if("true\n".equals(sb.toString())){
                    return true;
                }
                else{
                    return false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("sLogin","true");
                Ed.putString("emailId",mEmail);
                Ed.putString("password",mPassword);
                Ed.commit();
                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                Log.i("Login","Incorrect Password");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



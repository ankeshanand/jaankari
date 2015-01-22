package jaangari.opensoft.iitkgp.jaankari;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;

public class PasswordChangeActivity extends Activity{

    private EditText mEditTextCurrent;
    private EditText mEditTextNew;
    private EditText mEditTextRetype;
    private Button mButton;
    private UserLoginTask mAuthTask = null;

    public void changePassword(View view){
        SharedPreferences sp1 = this.getSharedPreferences("Login", 0);
        String old_password = sp1.getString("password", null);
        String email = sp1.getString("emailId",null);
        mEditTextCurrent = (EditText)findViewById(R.id.old_password_editText);
        mEditTextNew = (EditText)findViewById(R.id.new_password_editText);
        mEditTextRetype = (EditText)findViewById(R.id.retype_new_password_editText);

        View focusView = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(mEditTextCurrent.getText().toString())){
            mEditTextCurrent.setError("Cannot be empty!");
            focusView = mEditTextCurrent;
            cancel = true;
        }
        if(TextUtils.isEmpty(mEditTextNew.getText().toString())){
            mEditTextNew.setError("Cannot be empty!");
            focusView = mEditTextNew;
            cancel = true;
        }
        if(TextUtils.isEmpty(mEditTextRetype.getText().toString())){
            mEditTextRetype.setError("Cannot be empty!");
            focusView = mEditTextRetype;
            cancel = true;
        }

        if(!old_password.equals(mEditTextCurrent.getText().toString())){
            mEditTextCurrent.setError("Please Enter the correct password!");
            focusView = mEditTextCurrent;
            cancel = true;
        }
        if(!mEditTextRetype.getText().toString().equals(mEditTextNew.getText().toString())){
            mEditTextRetype.setError("Passwords do not match!");
            focusView = mEditTextRetype;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            mAuthTask = new UserLoginTask(email,mEditTextNew.getText().toString());
            mAuthTask.execute((Void) null);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_password_change, menu);
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


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mPassword;
        private final String mEmail;

        UserLoginTask(String email,String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.132.142.38/~rahulanishetty/OpenSoft/changePassword.php");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("email", mEmail));
                nameValuePair.add(new BasicNameValuePair("password",mPassword));
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
            if (success) {
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("password",mPassword);
                Ed.commit();
                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent);
                finishActivity(0);
            } else {
                finishActivity(1);
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//                Log.i("Login", "Incorrect Password");
            }
        }
    }
}

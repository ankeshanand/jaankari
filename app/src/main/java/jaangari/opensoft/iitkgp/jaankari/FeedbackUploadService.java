package jaangari.opensoft.iitkgp.jaankari;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaangari.R;
import jaangari.opensoft.iitkgp.jaankari.util.AppLog;

public class FeedbackUploadService extends Service {
    private ArrayList<String> list;
    public FeedbackUploadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        AppLog appLog = new AppLog();
        list = appLog.log(this.getApplicationContext());
        if(list!=null && list.size()>0){
            Upload upload = new Upload();
            upload.execute((Void) null);
            try {
                if (upload.get()) {

                } else {
                    for (int i=0;i<list.size();i++){
                        appLog.appendLog(this.getApplicationContext(),list.get(i));
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class Upload extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = "http://" + getString(R.string.ip_address) + "toDownload.php";
                JSONArray jsonArray = new JSONArray(list);
                int TIMEOUT_MILLISEC = 10000;
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = client.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                if(httpEntity!=null){
                    InputStream is = httpEntity.getContent();
                    String result = is.toString();
                    if(result.contains("true")){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

}

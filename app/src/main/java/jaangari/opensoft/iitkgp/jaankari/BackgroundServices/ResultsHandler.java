package jaangari.opensoft.iitkgp.jaankari.BackgroundServices;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import jaangari.opensoft.iitkgp.jaankari.hotspotUtils.CommDevice;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ResultsHandler extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "jaangari.opensoft.iitkgp.jaankari.BackgroundServices.action.FOO";
    private static final String ACTION_BAZ = "jaangari.opensoft.iitkgp.jaankari.BackgroundServices.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "jaangari.opensoft.iitkgp.jaankari.BackgroundServices.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "jaangari.opensoft.iitkgp.jaankari.BackgroundServices.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ResultsHandler.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ResultsHandler.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public ResultsHandler() {
        super("ResultsHandler");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Log.d("BroadcastRec", "Staring Service");
        try {
            listenForAvailableIDs();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean listenForAvailableIDs() throws IOException {
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            try {
                servsock = new ServerSocket(CommDevice.PORT_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock != null ? servsock.accept() : null;
                    System.out.println("Accepted connection : " + sock);

                    Log.d("CommDevice","Accepted Connection");
                    InputStream is = sock.getInputStream();

                    BufferedReader in = new BufferedReader(new InputStreamReader(is));

                    String msg = in.readLine();


//                    final byte[] buffer = new byte[1024];
                    try {
//                        int read = -1;
//                        while ((read = is.read(buffer)) > 0)
//                        {
//                            msg += buffer.toString();
//                        }

                        Log.d("CommDevice","Recieved Message : "+msg);

                        Log.d("BroadcastRec", "Started Service");
                        Intent ii = new Intent("NOTIFICATION");

                        ii.putExtra("JSON", msg);
                        sendBroadcast(ii);

                        is.close();
                        //CommDevice.IS_MSG_RECEIVED = true;
                        break;

                    }
                    catch (Exception e)
                    {
                        Log.d("CommDevice",e.toString());

                    }
                }

                finally {
                    if (sock != null) sock.close();
                }
            }
        }
        catch (Exception e)
        {

        }
//        finally {
//            if (servsock != null) servsock.close();
//        }

        return true;
    }



    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

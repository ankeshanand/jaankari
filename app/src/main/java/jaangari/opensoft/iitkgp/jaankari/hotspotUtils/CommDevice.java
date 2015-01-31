package jaangari.opensoft.iitkgp.jaankari.hotspotUtils;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import com.google.android.gms.internal.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Enumeration;

import jaangari.opensoft.iitkgp.jaankari.BackgroundServices.ResultsHandler;
import jaangari.opensoft.iitkgp.jaankari.DatabaseHandler;
import jaangari.opensoft.iitkgp.jaankari.SearchableActivity;
import jaangari.opensoft.iitkgp.jaankari.util.PairCategory;

/**
 * Created by shiwangi on 24/1/15.
 */
public class CommDevice {


    private static boolean IS_MSG_RECEIVED = false  ;
    DatabaseHandler dbHandler;

    private static final long LISTEN_TIMEOUT = 30000 ;

    private static final int MESSAGE_SIZE = 10;
    private static  boolean IS_RECEIVED = false;
    public static String BROADCAST_IP = "192.168.43.255";
    //public static String BROADCAST_IP = "10.0.3.0";
    public static final int PORT_DST = 6667;
    public static final int PORT_SRC = 2802;

    public static final int PORT_FILE = 4545;
    public static final int PORT_FILE_download = 5545;

    Context mContext ;


    public CommDevice(final Context context) throws IOException {
        mContext = context;
        dbHandler = new DatabaseHandler(context);
    }

    public void listenForQueries() throws IOException, JSONException {

        Log.d("CommDevice", "Listening for Broadcast Query");

        DatagramSocket s = new DatagramSocket(PORT_DST);
        while (true) {
            byte[] buf = new byte[100];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            s.receive(dp);
            String received = new String(dp.getData());
            String ans = "";
            for(int i=0;i<100;i++){
                if(received.charAt(i)!='\0'){
                    ans+=received.charAt(i);
                }
            }

            String myIp = getMyIp();
            String requestIp = dp.getAddress().getHostAddress();

            Log.d("CommDevice", "Got Query request from " + requestIp);

            if(myIp.equals(requestIp)) // In case of a query request to itself, ignore
                continue;

            String localResults =  dbHandler.fetchIndexList(ans);
            JSONObject finalJson = new JSONObject();

            finalJson.put("IP",myIp);
            finalJson.put("list",new JSONArray(localResults));
            sendMsg(finalJson.toString(), dp.getAddress());
            Log.d("CommDevice", ans);
            Log.d("CommDevice","Sent the Available indexes :"+ finalJson.toString());
        }

    }

    public String getMyIp()
    {
//        //  - Rohan - Implement This Function
//
//        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//        int ip = wifiInfo.getIpAddress();
//        String ipAddress = Formatter.formatIpAddress(ip);
//        if(ip==0)
//            return "192.168.43.1" ; //   Rohan - HardCoding ?

        return getWifiApIpAddress();
        //return ipAddress    ;
    }

    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            //Log.d("CommDevice", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("CommDevice", ex.toString());
        }
        return null;
    }

    public static String getBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements();) {
            NetworkInterface ni = niEnum.nextElement();

            if (ni.getName().contains("wlan")) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    try{
                        return interfaceAddress.getBroadcast().toString().replace("/","");
                    }
                    catch (Exception e)
                    {
                        continue;
                    }

                }
            }


        }
        return null;
    }

    public String intToIp(int i) {

        return ((i >> 24 ) & 0xFF ) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ( i & 0xFF) ;
    }

    public void sendMsg(String msg, InetAddress address) {
        Socket socket = null;
        try {
            socket = new Socket(address, PORT_FILE);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter out = new PrintWriter(outputStream);
            Log.d("CommDevice","Sending MEssage = " + msg);

            out.print(msg + "\n");

            out.close();

            // byte[] buffer = msg.getBytes();
            //outputStream.write(buffer);
            //outputStream.flush();

            outputStream.close();

            Log.d("CommDevice","Sent the message!!\n");
            socket.close();
//            bis.read(buffer,0,buffer.length);
//            outputStream.write(buffer,0,buffer.length);
//            outputStream.flush();

        } catch (Exception e) {
            Log.d("CommDevice","ERROR = " + e);
            e.printStackTrace();
        }
    }


    public ArrayList<Integer> fetchIndexOfQuery(String query) throws IOException {
        ArrayList<Integer> idList = new ArrayList<Integer>();
        InputStream indexFile = null;
        try {
            indexFile = new FileInputStream("index.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(indexFile));
        String line;
        ArrayList<Integer> Id = new ArrayList<Integer>();
        while ((line = br.readLine()) != null) {
            String token[] = line.split(",");
            int a = Integer.parseInt(token[0]);
            int l = token.length - 1;
            for(int i = 0 ; i < l ; i++){
                if(token[i + 1] == query){
                    Id.add(a);
                    break;
                }
            }
        }
        return Id;
    }




    public boolean broadcastQuery(String query) throws IOException {
        // DatagramSocket sock = new DatagramSocket(PORT_SRC);

        Log.d("CommDevice", "Broadcasting query..");

        DatagramSocket sock = new DatagramSocket(null);
        sock.setReuseAddress(true);
        sock.setBroadcast(true);
        sock.bind(new InetSocketAddress(PORT_SRC));

        InetSocketAddress dst = new InetSocketAddress(getBroadcast(), PORT_DST);

        String message =query;

        byte [] sendData = new byte[100];


        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dst);
        sendData = message.getBytes();
        sendPacket.setData(sendData);

        sock.send(sendPacket);

        IS_RECEIVED = false;

        Log.d("CommDevice", "Sent query..");

//        StoppableThread st = new StoppableThread();
//        st.start();
//
//        try {
//            Thread.sleep(LISTEN_TIMEOUT);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if(IS_RECEIVED == true)
//            st.exit(true);


        return true;

    }
    public boolean listenForAvailableIDs() throws IOException {
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            try {
                servsock = new ServerSocket(PORT_FILE);
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

                    String msg = "";
                    final byte[] buffer = new byte[1024];
                    try {
                        int read = -1;
                        while ((read = is.read(buffer)) > 0)
                        {
                            msg += buffer.toString();
                        }

                        Log.d("CommDevice","Recieved Message"+msg);

                        is.close();
                        IS_MSG_RECEIVED = true;
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
        } finally {
            if (servsock != null) servsock.close();
        }

        return true;
    }
    private boolean listenForReplies() throws IOException {
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            try {
                servsock = new ServerSocket(PORT_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock != null ? servsock.accept() : null;
                    System.out.println("Accepted connection : " + sock);

                    Log.d("CommDevice","Accepted Connection");
                    // send file
//                    BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//                    File newFile = new File("/storage/sdcard0/receivedFile");
                    byte[] mybytearray = new byte[10];
                    InputStream is = sock.getInputStream();

                    final File file2 = new File(dbHandler.getFilePath("", 1));

                    final OutputStream output = new FileOutputStream(file2);

                    final byte[] buffer = new byte[1024];
                    try {
                        int read = -1;
                        while ((read = is.read(buffer)) > 0)
                        {
                            output.write(buffer, 0 , read);
                        }

                        Log.d("CommDevice","Recieved File");


                        output.close();
                        is.close();
                        IS_RECEIVED = true;

                        break;

                    }


                    catch (Exception e)
                    {
                        Log.d("CommDevice",e.toString());

                    }


//
                }


                finally {
                    if (sock != null) sock.close();
                }
            }
        } finally {
            if (servsock != null) servsock.close();
        }

        return true;
    }



    public String getFilePathfromID(int query) throws IOException {
        InputStream indexFile = null;
        String filePath = "" ;
        try {
            indexFile = new FileInputStream("filesOnSystem.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(indexFile));
        String line;
        ArrayList<Integer> Id = new ArrayList<Integer>();
        while ((line = br.readLine()) != null) {
            String token[] = line.split(",");
            int a = Integer.parseInt(token[0]);
            if(a==query){
                filePath = token[1];
            }
        }
        return filePath;
    }

    private boolean checkLocalDatabase(int id, InetAddress the_ip) throws IOException{

        String filePath = getFilePathfromID(id);

        if(!filePath.equals(""))
        {
            sendFile(String.valueOf(the_ip).substring(1),filePath );
            return true;
        }

        return false;
    }


    public void sendFile(String IP,String filePath) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket(IP, PORT_FILE);
            OutputStream outputStream = socket.getOutputStream();
            File f = new File( filePath);
            byte [] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(f);
            Log.d("CommDevice","Read the file!!\n");

            BufferedInputStream bis = new BufferedInputStream(fis);

            int read = -1;
            while((read = bis.read(buffer)) > 0)
            {
                outputStream.write(buffer, 0 ,read);
            }
            //outputStream.flush();

            outputStream.close();

            Log.d("CommDevice","Sent the file!!\n");
            //socket.close();
//            bis.read(buffer,0,buffer.length);
//            outputStream.write(buffer,0,buffer.length);
//            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean listenForFileRequests() throws IOException {
        ServerSocket servsock = null;
        Socket sock = null;
        try {

            servsock = new ServerSocket(PORT_FILE_download);

            while (true) {
                //Log.d("CommDevice", "Listening for File Request");

                sock = servsock != null ? servsock.accept() : null;

                Log.d("CommDevice","Accepted Connection for File Request");

                String strRec = waitForStringSocket(sock);
                strRec = strRec.replaceAll("\n","");
                String[] tokens = strRec.split(";");

                String fPath = dbHandler.getFilePath(tokens[0],Integer.parseInt(tokens[1]));

                Log.d("CommDevice","****FILENAME = " + fPath);
                sendFileOverSocket(sock,fPath);
                //sendFileOverSocket(sock,"/storage/emulated/legacy/Jaankari/bak/inp.mp4");

                if (sock != null) sock.close();

            }
        }
        catch (Exception e)
        {
            Log.d("CommDevice","Error=" + e.toString());
            e.printStackTrace();
        }
        finally {
            if (servsock != null) servsock.close();
        }

        return true;

    }
    public void requestFile(String IP, String category,int id) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket(IP, PORT_FILE_download); // Todo : Rohan - Check SubString for IP
            String toSend = category + ";" + String.valueOf(id);
            sendStringoverSocket(socket,toSend);
            saveFileOverSocket(socket,category,id);
            //socket.close();
//            bis.read(buffer,0,buffer.length);
//            outputStream.write(buffer,0,buffer.length);
//            outputStream.flush();

        } catch (Exception e) {
            Log.d("CommDevice", e.toString());
            e.printStackTrace();
        }
    }

    public void sendStringoverSocket(Socket socket, String toSend) throws IOException {

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream);
        Log.d("CommDevice","Sending String = " + toSend);

        out.print(toSend + "\n");
        out.flush();

        //out.close();

        // byte[] buffer = msg.getBytes();
        //outputStream.write(buffer);
        //outputStream.flush();

        //outputStream.close();

        Log.d("CommDevice","Sent the message!!\n");

    }

    public String waitForStringSocket(Socket sock) throws IOException {

        InputStream is = sock.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        String theS = in.readLine();

        Log.d("CommDevice", "got waited String = " + theS);

        //in.close();
        //is.close();

        return theS ;
    }

    public void sendFileOverSocket(Socket sock,String filePath) throws IOException {
        Socket socket = null;
        try {
            socket = sock; //new Socket(IP, PORT_FILE);
            OutputStream outputStream = socket.getOutputStream();
            File f = new File( filePath);
            byte [] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(f);
            Log.d("CommDevice","Read the file!!\n");

            BufferedInputStream bis = new BufferedInputStream(fis);

            int read = -1;
            while((read = bis.read(buffer)) > 0)
            {
                outputStream.write(buffer, 0 ,read);
            }
            outputStream.flush();

            outputStream.close();
            sock.close();
            bis.close();

            Log.d("CommDevice","Sent the file!!\n");
            //socket.close();
//            bis.read(buffer,0,buffer.length);
//            outputStream.write(buffer,0,buffer.length);
//            outputStream.flush();

        } catch (Exception e) {
            Log.d("CommDevice","ERRe!" + e);
            e.printStackTrace();
        }
    }
    public void saveFileOverSocket(Socket sock, String cat, int id) throws IOException {
        try {

            InputStream is = sock.getInputStream();

            //final File file2 = new File(dbHandler.getFilePath("", 1));


            DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
            String fpath = databaseHandler.getFilePath(cat,id);
            //String fpath = "/storage/emulated/legacy/Jaankari/bak/out.mp4";
            final File file2 = new File(fpath);

            final OutputStream output = new FileOutputStream(file2);

            final byte[] buffer = new byte[1024];
            try {
                int read = -1;
                while ((read = is.read(buffer)) > 0)
                {
                    output.write(buffer, 0 , read);
                }

                Log.d("CommDevice","Recieved File");

                output.close();
                is.close();
                sock.close();
                IS_RECEIVED = true;

            }
            catch (Exception e)
            {
                Log.d("CommDevice",e.toString());

            }

            Log.d("CommDevice","Saved the file!!\n");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

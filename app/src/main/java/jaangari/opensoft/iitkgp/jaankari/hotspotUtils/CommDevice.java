package jaangari.opensoft.iitkgp.jaankari.hotspotUtils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by shiwangi on 24/1/15.
 */
public class CommDevice {



    private class StoppableThread extends Thread{
        boolean bExit = false;

        public void exit(boolean bExit){
            this.bExit = bExit;
        }

        @Override
        public void run(){
            while(!bExit){
                System.out.println("Thread is running");
                try {
                    listenForReplies();
                } catch (Exception ex) {
                    // Logger.getLogger(ThreadTester.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static final long LISTEN_TIMEOUT = 30000 ;

    public static String FILES_PATH = Environment.getExternalStorageDirectory().toString() ;
    public static String FILES_NAME = "";



    private static final int MESSAGE_SIZE = 10;
    private static  boolean IS_RECEIVED = false;
    public static String BROADCAST_IP = "192.168.43.0";
    //public static String BROADCAST_IP = "10.0.3.0";
    public static final int PORT_DST = 6667;
    public static final int PORT_SRC = 2802;

    public static final int PORT_FILE = 4545;

    Context mContext ;


    public CommDevice(final Context context) throws IOException {

    }

    public void listenForQueries() throws IOException {

        DatagramSocket s = new DatagramSocket(PORT_DST);
        while (true) {
            byte[] buf = new byte[100];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            s.receive(dp);
            String rcvd = "rcvd from " + dp.getAddress() + ", " + dp.getPort() + ": "
                    + new String(dp.getData());
            Log.d("CommDevice", rcvd);
            String received = new String(dp.getData());
            String ans = "";
            for(int i=0;i<100;i++){
                if(received.charAt(i)!='\0'){
                    ans+=received.charAt(i);
                }
            }
            System.out.println("here *** "+ ans);
            checkLocalDatabase(Integer.parseInt(ans), dp.getAddress());


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


    public String getFileName(int id)
    {

        return "xyz.mp4";
    }


    public boolean broadcastQuery(int id) throws IOException {
        // DatagramSocket sock = new DatagramSocket(PORT_SRC);

        DatagramSocket sock = new DatagramSocket(null);
        sock.setReuseAddress(true);
        sock.setBroadcast(true);
        sock.bind(new InetSocketAddress(PORT_SRC));

        InetSocketAddress dst = new InetSocketAddress(BROADCAST_IP, PORT_DST);

        String message =String.valueOf(id);

        message = message+","+ getFileName(id);
        byte [] sendData = new byte[100];


        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dst);
        sendData = message.getBytes();
        sendPacket.setData(sendData);

        sock.send(sendPacket);

        IS_RECEIVED = false;

        StoppableThread st = new StoppableThread();
        st.start();

        try {
            Thread.sleep(LISTEN_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(IS_RECEIVED == true)
            st.exit(true);


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

                    final File file2 = new File(FILES_PATH + FILES_NAME);

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
}

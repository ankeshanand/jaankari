package jaangari.opensoft.iitkgp.jaankari.hotspotUtils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import jaangari.opensoft.iitkgp.jaankari.AppStatus;

/**
 * Created by rohanraja on 21/01/15.
 */
public class WifiHOTSPOTManager {

    private static final long TIME_CONNCHECK = 10000;
    private static final long WIFI_SCAN_WAIT = 10000 ;
    WifiApManager wifiApManager;
    Context mContext;
    static String SSID_HOTSPOT = "InternetON";
    WifiManager wifiManager;
    int WAIT_TIME = 30000;
    int NetworkConnectState = 0;

    public WifiHOTSPOTManager(Context pContext) {
        mContext = pContext;
        wifiApManager = new WifiApManager(mContext);
        wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);

        while (true)
        {
            NetworkConnectState = EstablishConnection();

            try {
                Thread.sleep(TIME_CONNCHECK);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public int EstablishConnection() {
        //TODO is INTERNET AVAILABLE
        if(AppStatus.getInstance(this.mContext).isOnline())
        {
            Log.d("WifiManager", "Internet connection available exiting!!!");
            return 1;
        }

        if (isWifiConnected())
        {
            Log.d("WifiManager", "Wifi IS Connected!! EXITING");
            return 1;
        }


        Log.d("WifiManager", "Wifi Not Connected");

        if (isHotspotActive()) {
            Log.d("WifiManager", "HotSpot is Active!");
            if (isHotSpotHasClients())
            {
                Log.d("WifiManager", "HotSpot Has Clients");
                return 1;
            }

            else
            {
                Log.d("WifiManager", "Active But No Clients, Waiting......");
                WaitForUsersToConnect("30 seconds");
                if (isHotSpotHasClients())
                {
                    Log.d("WifiManager", "HotSpot Has Clients");
                    return 1;
                }
                Log.d("WifiManager", "Active Still No Clients, Destroying");
                destroyHotspot();

            }

        }
        switchONWifi();

        Log.d("WifiManager", "Switched On WIFI");
        if (isHubAvailable()) {

            Log.d("WifiManager", "HOTSPOT AVAILABLE");
            connectToHub();
            Log.d("WifiManager", "Connected");
            return 1;
        }

        createHotSpot();

        return 0;

    }

    private void destroyHotspot() {

        wifiApManager.setWifiApEnabled(null,false);


    }

    public boolean isHotspotActive() {

        return wifiApManager.isWifiApEnabled() ;

    }


    public boolean isWifiConnected() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", "\""+ wifiInfo.toString() + "\"");
        Log.d("SSID", wifiInfo.getSSID());
        System.out.println(wifiInfo.getSSID().equals(SSID_HOTSPOT));
        return (wifiInfo.getSSID().equals("\""+ SSID_HOTSPOT+ "\""));

    }


    public boolean isHubAvailable() {
        wifiManager.startScan();
        try {
            Thread.sleep(WIFI_SCAN_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ScanResult> list = wifiManager.getScanResults();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("WifiManager", "ResultSize = " + String.valueOf(list.size()));
        for (ScanResult i : list) {
            Log.d("WifiHubs", i.SSID);
            if (i.SSID.equals(SSID_HOTSPOT))
                return true;
        }
        return false;
    }


    public boolean isHotSpotHasClients() {

        BufferedReader br = null;
        final ArrayList<ClientScanResult> result = new ArrayList<ClientScanResult>();

        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");

                if ((splitted != null) && (splitted.length >= 4)) {
                    // Basic sanity check
                    String mac = splitted[3];

                    if (mac.matches("..:..:..:..:..:..")) {
                        boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(10000);

                        if (isReachable) {
                            result.add(new ClientScanResult(splitted[0], splitted[3], splitted[5], isReachable));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.toString());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), e.getMessage());
            }
        }
        return result.size()>0;
    }


    public boolean connectToHub() {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", SSID_HOTSPOT);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        return false;
    }


    public boolean createHotSpot() {

        WifiConfiguration wf = new WifiConfiguration();
        wf.SSID = SSID_HOTSPOT;
        wifiApManager.setWifiApEnabled(wf, true);
        return false;

    }


    public boolean WaitForUsersToConnect(String par) {

        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void switchONWifi() {
        if(wifiManager.isWifiEnabled() == false)
        {
            wifiManager.setWifiEnabled(true); // true or false to activate/deactivate wifi
        }

    }



}

package app.sunshine.android.example.com.carplay;


import android.app.Application;
import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;

import org.ccbeacon.beacon.Beacon;
import org.ccbeacon.beacon.BeaconConsumer;
import org.ccbeacon.beacon.BeaconManager;
import org.ccbeacon.beacon.BeaconParser;
import org.ccbeacon.beacon.Identifier;
import org.ccbeacon.beacon.MonitorNotifier;
import org.ccbeacon.beacon.RangeNotifier;
import org.ccbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MyApplication extends Application implements BeaconConsumer {
    private static final String TAG = ".MyApplication";

    public BeaconManager beaconManager = null;

    ArrayList<HashMap<String, String>> finddollList = new ArrayList<HashMap<String, String>>();

    private MainActivity main = null;
    private double distan;

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        beaconManager.setForegroundBetweenScanPeriod(500);

        main = new MainActivity();
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
             @Override
             public void didEnterRegion(Region region) {
                 Log.i(TAG, "Region : Enter , Region ID =" + region.getUniqueId() );

                 if(main == null)
                     return;


                 main.showDialog("Region : Enter, Region ID =" + region.getUniqueId(), Color.GRAY );

             }

             @Override
             public void didExitRegion(Region region) {
                 Log.i(TAG, "Region : Exit , Region ID =" + region.getUniqueId());

                 if(main == null)
                     return;

                 main.showDialog("Region : Enter, Region ID =" + region.getUniqueId() ,Color.GRAY);
             }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {

                Log.i(TAG,  " didDetermineStateForRegion() : I have just switched from seeing /not seeing beacons: " + state);
                if(main == null)
                    return;

                if (state==MonitorNotifier.INSIDE) {
                    main.showDialog(true);

                } else if (state ==MonitorNotifier.OUTSIDE){
                    main.showDialog(false);
                }

            }
        });



        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {

                    for (Beacon firstBeacon : beacons) {

                        distan = firstBeacon.getDistance();
                        Log.i(TAG, firstBeacon.getId1().toString()+firstBeacon.getId2() + "************* Distant : " + String.valueOf(distan) +"\n" );



                        if(main != null){
                            main.didFindBeaconInRegion(firstBeacon,distan);
                         //   Log.i(TAG, "ddfdsfsdfds");
                        }

                    }
                }
            }
        });


        try {
            //beaconManager.startRangingBeaconsInRegion(new Region("myCarId", null, null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("myCarId", null, Identifier.parse("45057"), Identifier.parse("57529")));
        } catch (RemoteException e) {
        }


    }


    public ArrayList<HashMap<String, String>> getfindDolldollList() {

        return this.finddollList;
    }

    public void setActive(MainActivity activity) {
        this.main = activity;
    }

    public void clearFinddollList() {
        finddollList.clear();
    }


}
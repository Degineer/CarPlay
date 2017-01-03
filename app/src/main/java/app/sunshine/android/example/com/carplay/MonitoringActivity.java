package app.sunshine.android.example.com.carplay;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import org.ccbeacon.beacon.Beacon;
import org.ccbeacon.beacon.BeaconConsumer;
import org.ccbeacon.beacon.BeaconManager;
import org.ccbeacon.beacon.MonitorNotifier;
import org.ccbeacon.beacon.Region;

import java.util.Collection;

public class MonitoringActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "MonitoryingActivity";
    private Activity monitoringActivity= null;

    private BeaconManager beaconManager =
            BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        beaconManager.bind(this);
        monitoringActivity=this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    private double distan;


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {




            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {
                    String  _major, _minor;

                    for (Beacon firstBeacon : beacons) {

                        distan = firstBeacon.getDistance();
                        Log.i(TAG, firstBeacon.getId1().toString()+firstBeacon.getId2() + "************* Distant : " + String.valueOf(distan) );

                        _major=(String)firstBeacon.getId1().toString();
                        _minor=(String)firstBeacon.getId2().toString();

                        if (_major.equals("45057") && _minor.equals("57529")) {
                            Toast.makeText(monitoringActivity, "Find Beacon ...", Toast.LENGTH_SHORT).show();
                        }
                        //if(main != null){
                        //    main.didFindBeaconInRegion(firstBeacon,distan);
                        //}

                    }
                }
            }

            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {

                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing /not seeing beacons: " + state);

                if (state==MonitorNotifier.INSIDE) {

                } else if (state ==MonitorNotifier.OUTSIDE){

                }
            }


        });

        try{
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        }catch (RemoteException e) {}


    }
}

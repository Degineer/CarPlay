package app.sunshine.android.example.com.carplay;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.ccbeacon.beacon.Beacon;
import org.ccbeacon.beacon.BeaconConsumer;
import org.ccbeacon.beacon.BeaconManager;
import org.ccbeacon.beacon.RangeNotifier;
import org.ccbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager =
            BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons.size() > 0 ){
                    Log.i(TAG, "This first beacon I see is about " + beacons.iterator().next().getDistance()+" meters aways. ");

                }
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));

        }catch (RemoteException e) {

        }
    }
}



package app.sunshine.android.example.com.carplay;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.TextView;

import org.ccbeacon.beacon.Beacon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_UUID = "uuid";
    public static final String KEY_MAJOR = "major";
    public static final String KEY_MINOR = "minor";
    private static final String TAG = ".MainActivity";
    private EventAgreeDialog mAgreeDlg;

    private ListView mDetectList;
    ArrayList<HashMap<String, String>> BeaconsList;
    private ListViewAdapter listadapter;
    private ArrayList<ListItem> itemlist = null;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        BeaconsList.clear();
        ((MyApplication) this.getApplication()).clearFinddollList();

        listadapter.clear();
        listadapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TextView textView = (TextView) findViewById(R.id.tvContents);

        textView.setText("Test");
       // textView.setText(Color.RED);


        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                5);

        itemlist = new ArrayList<ListItem>();

        BeaconsList = new ArrayList<HashMap<String, String>>();
        BeaconsList.addAll(getFindBeaconsList());

        initList();
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.
                    getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
        }
    }
    public void initList() {
        listadapter = new ListViewAdapter(this, R.layout.listitem_demo, itemlist);
        mDetectList = (ListView) findViewById(R.id.listView);
        mDetectList.setAdapter(listadapter);
    }


    //  Beacon App
    public ArrayList<HashMap<String, String>> getFindBeaconsList() {
        return ((MyApplication) this.getApplication()).getfindDolldollList();
    }

    public void didFindBeaconInRegion(Beacon beacon, double distance) {
        AddNewBeaconProcess(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString(), distance);




        Log.i(TAG,"AddNewBeaconProcess \n" );
    }

    public void AddNewBeaconProcess(final String id1, final String id2, final String id3, final double distance) {

        runOnUiThread(new Runnable() {
            public void run() {
                getData(id1 + " - " + id2 + " - " + id3, id1, id2, id3, distance);

                if (distance < 2) {
                    showDialog ("You are in the Car", Color.GREEN);
                }
                else {
                    showDialog ("You got off the Car",Color.RED);
                }

            }
        });

       /*
        Immediate Zone (0-20 cm) : 아주 가까운 거리로 정확도가 높다.
        Near Zone (20 cm - 2 m) : 가까운 거리로 어느 정도 정확도를 갖는다.
        Far Zone (2 - 70 m) : 먼 거리로 정확도가 떨어진다.
        */



    }


    public void getData(String result, String uuid, String major, String minor, double distance) {
        HashMap<String, String> song = null;
        String _uuid, _major, _minor;
        boolean is_bankfind = false;
        double distan = distance;

        ListItem item = new ListItem(result, " distance : " + String.format("%.3f", distan) +" \n");

        for (int i = 0; i < BeaconsList.size(); i++) {
            song = BeaconsList.get(i);

            _uuid = song.get(KEY_UUID);
            _major = song.get(KEY_MAJOR);
            _minor = song.get(KEY_MINOR);

            if ((_uuid.equals(uuid) == true) && (_major.equals(major) == true) && (_minor.equals(minor) == true)) {
                is_bankfind = true;
                listadapter.notifyDataSetInvalidated();
                item.setDistance(" distance : " + String.format("%.3f", distan) +" \n");
                itemlist.remove(i);
                itemlist.add(i, item);
                listadapter.notifyDataSetChanged();

                break;
            }
        }

        if (is_bankfind == false) {
            ListItem listitem = new ListItem(result, String.valueOf(item.getDistance()));
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(MainActivity.KEY_UUID, uuid);
            map.put(MainActivity.KEY_MAJOR, major);
            map.put(MainActivity.KEY_MINOR, minor);
            BeaconsList.add(map);
            itemlist.add(listitem);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
/*
        if (id == R.id.bt_clear) {
            BeaconsList.clear();
            ((ccbeaconService) this.getApplication()).clearFinddollList();
            listadapter.clear();
            listadapter.notifyDataSetChanged();

            return true;
        }
*/



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tell the Application not to pass off ranging updates to this activity
        ((MyApplication) this.getApplication()).setActive(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tell the Application to pass off ranging updates to this activity
        ((MyApplication) this.getApplication()).setActive(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showDialog(boolean b) {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Title");
        Log.i(TAG,"Show AlertDialog \n" );

        if(b) {
            ab.setMessage("내용 "+ "INSIDE");
        }else{
            ab.setMessage("내용 " + "OUTSIDE");
        }

        ab.setCancelable(false);
        //ab.setIcon(getResources().getDrawable(R.drawable.ic_launcher))

        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ab.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ab.show();


    }

    public void showDialog(String message, int color) {

        TextView str = (TextView) findViewById(R.id.tvContents);

        str.setText(message);
        str.setTextColor(color);

    }
}



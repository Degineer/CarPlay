package app.sunshine.android.example.com.carplay;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ListViewAdapter extends ArrayAdapter<ListItem> {
    Context context;
    List<ListItem> itemList;
    int layoutResID;

    public ListViewAdapter(Context context, int layoutResourceID,
                           List<ListItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.itemList = listItems;
        this.layoutResID = layoutResourceID;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItemHolder holder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            holder = new ListItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            holder.BeaconID = (TextView) view.findViewById(R.id.textID);
            holder.BeaconDistance = (TextView) view.findViewById(R.id.textDistance);

            view.setTag(holder);

        } else {
            holder = (ListItemHolder) view.getTag();

        }

        ListItem listItem = (ListItem) this.itemList.get(position);

        holder.BeaconDistance.setText(listItem.getBeaconId());
        holder.BeaconID.setText(listItem.getDistance());

        return view;
    }

    public static class ListItemHolder {
        TextView BeaconID;
        TextView BeaconDistance;
    }
}

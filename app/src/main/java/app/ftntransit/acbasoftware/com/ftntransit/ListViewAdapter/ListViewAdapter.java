package app.ftntransit.acbasoftware.com.ftntransit.ListViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.ftntransit.acbasoftware.com.ftntransit.MainActivity;
import app.ftntransit.acbasoftware.com.ftntransit.Objects.Order;
import app.ftntransit.acbasoftware.com.ftntransit.R;

/**
 * Created by user on 2016-10-21.
 */
public  class ListViewAdapter extends ArrayAdapter<Order> {
    private MainActivity ma;
    public ListViewAdapter(MainActivity ma,Context c, ArrayList<Order> values) {

        super(c, R.layout.listview_pending_orders_layout, values);
        this.ma=ma;
    }

    /**
     * Implement getView method for customizing row of list view.
     * this method creates store_list single view that correponds to the data being passed in.
     * get the STORE data by getItem(position)
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ma.getLayoutInflater();
        // Creating store_list view of row.
        View rowView = inflater.inflate(R.layout.listview_pending_orders_layout, parent, false);
        Order o = getItem(position);
        TextView tv_o = (TextView) rowView.findViewById(R.id.tv_order_number);
        tv_o.setText(o.order_number+"");
        TextView tv_pk = (TextView) rowView.findViewById(R.id.tv_pickup_sku);
        tv_pk.setText(o.pickup_sku);
        TextView tv_del = (TextView) rowView.findViewById(R.id.tv_delivery_sku);
        tv_del.setText(o.delivery_sku);
        TextView tv_status = (TextView) rowView.findViewById(R.id.tv_status);
        tv_status.setText(o.status);
        TextView tv_date = (TextView) rowView.findViewById(R.id.tv_pickup_date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        tv_date.setText(sdf.format(o.pickup_datetime));
        TextView tv_time = (TextView) rowView.findViewById(R.id.tv_pickup_time);
        sdf=new SimpleDateFormat("HH:mm");
        tv_time.setText(sdf.format(o.pickup_datetime));
        TextView tv_loc = (TextView) rowView.findViewById(R.id.tv_pickup_location);
        tv_loc.setText(o.start_dest_id+" :id ");
        return rowView;
    }
}
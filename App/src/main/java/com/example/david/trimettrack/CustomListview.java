package com.example.david.trimettrack;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by umeza on 7/25/17.
 */

public class CustomListview extends ArrayAdapter<String>{
    private String[] rideTypes;
    private String[] rideCosts;
    private String[] rideDurations;
    private String[] rIdeDistances;
    private Activity context;
    public CustomListview(Activity context, String[] rideTypes, String[] rideCosts, String[] rideDurations, String[] rideDistances) {
        super(context, R.layout.listview_layout, rideTypes);

        this.context = context;
        this.rideTypes = rideTypes;
        this.rideCosts = rideCosts;
        this.rideDurations = rideDurations;
        this.rIdeDistances = rideDistances;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();

        }
        viewHolder.tvrt.setText(rideTypes[position]);
        viewHolder.tvrc.setText(rideCosts[position]);
        viewHolder.tvrdur.setText(rideDurations[position]);
        viewHolder.tvrdist.setText(rIdeDistances[position]);

        return r;
    }

    class ViewHolder{
        TextView tvrt;
        TextView tvrc;
        TextView tvrdur;
        TextView tvrdist;

        ViewHolder(View v){
            tvrt = (TextView) v.findViewById(R.id.ridetypeText);
            tvrc = (TextView) v.findViewById(R.id.rideCostText);
            tvrdur = (TextView) v.findViewById(R.id.RideDurationText);
            tvrdist = (TextView) v.findViewById(R.id.distanceText);
        }
    }
}

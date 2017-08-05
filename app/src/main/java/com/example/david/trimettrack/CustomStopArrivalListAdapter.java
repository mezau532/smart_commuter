package com.example.david.trimettrack;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David on 8/5/2017.
 */

public class CustomStopArrivalListAdapter extends SimpleAdapter {

    private Context mContext;
    private ArrayList<HashMap<String,String>> mData;

    public CustomStopArrivalListAdapter(Context context, ArrayList<HashMap<String,String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView detourInfo = view.findViewById(R.id.detourInfo);
        if(detourInfo.getText().toString().equalsIgnoreCase("No Detour Information")){
            detourInfo.setVisibility(View.INVISIBLE);
        }
        else
            detourInfo.setVisibility(View.VISIBLE);
        return view;
    }
}

package com.example.SerarchNearBy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
public class MyAdapter<T> extends ArrayAdapter<T> {
    private int dropDownViewResourceId;
    private LayoutInflater inflater;

    public MyAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        init();
    }

    public MyAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init();
    }

    public MyAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
        init();
    }

    public MyAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
        init();
    }

    public MyAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
        init();
    }

    public MyAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        init();
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        dropDownViewResourceId = resource;
    }
    public void init() {
        inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        Log.d("Spinner", "getDropDownView at position " + position);
        Object item = getItem(position);
        LinearLayout dropDownItemView = (LinearLayout) inflater.inflate(dropDownViewResourceId,null);
        TextView text1 = (TextView) dropDownItemView.findViewById(R.id.spinner_dropdown_textView);
        text1.setText(item.toString());
        return dropDownItemView;
    }

}

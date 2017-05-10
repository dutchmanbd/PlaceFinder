package com.zxdmjr.placefinder.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxdmjr.placefinder.GridItem;
import com.zxdmjr.placefinder.R;

import java.util.ArrayList;

/**
 * Created by user on 9/7/2016.
 */
public class GridAdapter extends ArrayAdapter<GridItem>
{
    Context context;
    private int layoutResourceId;
    ArrayList<GridItem> data = new ArrayList<>();

    public GridAdapter(Context context, int layoutResourceId, ArrayList<GridItem> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RecordHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);

        }
        else
        {
            holder = (RecordHolder) row.getTag();
        }

        GridItem gridItem = data.get(position);
        holder.txtTitle.setText(gridItem.getTitle());
        holder.imageItem.setImageBitmap(gridItem.getImage());

        return row;
    }
    public class RecordHolder
    {
        TextView txtTitle;
        ImageView imageItem;
    }
}

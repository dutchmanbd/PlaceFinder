package com.zxdmjr.placefinder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zxdmjr.placefinder.R;
import com.zxdmjr.placefinder.map.nearby_places.PlaceDetails;
import java.util.List;

/**
 * Created by user on 9/7/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>
{

    private List<PlaceDetails> placeDetailsData;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, address;

        MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.address);
        }
    }

    public CategoryAdapter(List<PlaceDetails> placeDetailsData)
    {
        this.placeDetailsData = placeDetailsData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        PlaceDetails placeDetails = placeDetailsData.get(position);
        String vicinity = placeDetails.getVicinity();
        String formattedAddress = placeDetails.getFormatted_address();
        holder.title.setText(placeDetails.getName());
        if (vicinity != null)
        {
            holder.address.setText(vicinity);
        }
        else
        {
            holder.address.setText(formattedAddress);

        }
    }

    @Override
    public int getItemCount()
    {
        return placeDetailsData.size();
    }
}

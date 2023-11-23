package com.company.jsw_internal_shipment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BatchDetailsAdapter extends BaseAdapter {
    private Context mContext;
    private List<BatchDetailsBean> mealTypeList;
    private LayoutInflater mLayoutInflater;
    List<BatchDetailsBean> removedItems = new ArrayList<>();
    List<String> removedItem = new ArrayList<>();
    public BatchDetailsAdapter(Context context, List<BatchDetailsBean> mealTypeList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mealTypeList = mealTypeList;
    }

    @Override
    public int getCount() {
        return mealTypeList.size();
    }

    @Override
    public BatchDetailsBean getItem(int position) {
        return mealTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position) {

        BatchDetailsBean removedItem = mealTypeList.get(position);

        // Remove from both lists
        mealTypeList.remove(position);
        removedItems.remove(removedItem);

        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BatchDetailsAdapter.HViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_create_trip, null);
            holder = new BatchDetailsAdapter.HViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (BatchDetailsAdapter.HViewHolder) convertView.getTag();
        }
        holder.txtID = convertView.findViewById(R.id.jsw_id);
        holder.txtdetails=convertView.findViewById(R.id.jswid_details);

        holder.txtID.setText(mealTypeList.get(position).getBatchId()+
                "\n");
        holder.txtdetails.setText("\n"
                +"\nBatch Weight: "+mealTypeList.get(position).getBatchWeight()

                +"\nLoad Yard ID: "+mealTypeList.get(position).getLoadYardId()

        +"\nLoad Yard: "+mealTypeList.get(position).getLoadYard()

        +"\nUnload Yard: "+mealTypeList.get(position).getUnloadYard()

                +"\nUnload Yard ID: "+mealTypeList.get(position).getUnloadYardId()

        +"\nUnloading Point: "+mealTypeList.get(position).getUnloadingPoint()

        +"\nCustomer: "+mealTypeList.get(position).getCustomer()

        +"\nTrip ID: "+mealTypeList.get(position).getTripId());
        String id=mealTypeList.get(position).getBatchId();
        String weight=mealTypeList.get(position).getBatchWeight();
        String loadyardid=mealTypeList.get(position).getLoadYardId();
        String loadyard=mealTypeList.get(position).getLoadYard();
        String unloadyard=mealTypeList.get(position).getUnloadYard();
        String unloadyardid=mealTypeList.get(position).getUnloadYardId();
        String unloadpoint=mealTypeList.get(position).getUnloadingPoint();
        String customer=mealTypeList.get(position).getCustomer();
        String tripid=mealTypeList.get(position).getTripId();
//        holder.delete.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });

        return convertView;
    }

    class HViewHolder {
        TextView txtID,txtdetails;
        ImageView delete;
    }
//    public List<BatchDetailsBean> getPermanentlyRemovedItems() {
//        return  removedItems;
//    }
}

package com.company.jsw_internal_shipment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.prefs.BackingStoreException;

public class UnloadingYardSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<UnitNameBean> mealTypeList;
    private LayoutInflater mLayoutInflater;

    public UnloadingYardSpinnerAdapter(Context context, List<UnitNameBean> mealTypeList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mealTypeList = mealTypeList;
    }

    @Override
    public int getCount() {
        return mealTypeList.size();
    }

    @Override
    public UnitNameBean getItem(int position) {
        return mealTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UnloadingYardSpinnerAdapter.HViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_text, null);
            holder = new UnloadingYardSpinnerAdapter.HViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (UnloadingYardSpinnerAdapter.HViewHolder) convertView.getTag();
        }
        holder.txtName = convertView.findViewById(R.id.list_its_tag_reason_textView_value);

        holder.txtName.setText(mealTypeList.get(position).getYard_name());

        return convertView;
    }

    class HViewHolder {
        TextView txtName;
    }
}

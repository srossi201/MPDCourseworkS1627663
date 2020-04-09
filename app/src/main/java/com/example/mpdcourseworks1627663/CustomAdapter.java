package com.example.mpdcourseworks1627663;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements Filterable {

    private List<RSSTrafficItems> dataSet;
    private List<RSSTrafficItems> filteredData;
    Context mContext;
    Date filterDate;
    private LayoutInflater mLayoutInflater;


    public void setSelectedDate(Date time) {
        this.filterDate = time;
    }


    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView description;
        TextView geoRSS;
        TextView pubDate;
        TextView sDate;
        TextView eDate;
    }

    public CustomAdapter(List<RSSTrafficItems> data, Context context) {
        this.mContext = context;
        this.dataSet = data;
        this.filteredData = data;
        mLayoutInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return this.filteredData.size();
    }

    @Override
    public RSSTrafficItems getItem(int position) {
        return this.filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RSSTrafficItems dataModel = filteredData.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;


        viewHolder = new ViewHolder();
        convertView= mLayoutInflater.inflate(R.layout.rss_item_list_row, null);
        viewHolder.title = (TextView) convertView.findViewById(R.id.title);
        viewHolder.description = (TextView) convertView.findViewById(R.id.description);
        viewHolder.geoRSS = (TextView) convertView.findViewById(R.id.georss);
        viewHolder.pubDate = (TextView) convertView.findViewById(R.id.pubDate);
        viewHolder.sDate = (TextView) convertView.findViewById(R.id.sDate);
        viewHolder.eDate = (TextView) convertView.findViewById(R.id.eDate);

        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.description.setText(dataModel.getDescription());
        viewHolder.geoRSS.setText(dataModel.getGeorss());
        viewHolder.pubDate.setText(dataModel.getPubDate());
        viewHolder.sDate.setText(dataModel.getStartDate().toString());
        viewHolder.eDate.setText(dataModel.getEndDate().toString());
        System.out.println(dataModel.getStartDate());

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredData = (List<RSSTrafficItems>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<RSSTrafficItems> filteredArrayNames = new ArrayList<>();

                // perform your search here using the searchConstraint String.

                String constraintStr = constraint.toString().toLowerCase();

                if(constraintStr.isEmpty() && filterDate ==null) {
                    filteredData = dataSet;
                }
                else {

                    for (int i = 0; i < dataSet.size(); i++) {
                        String title = dataSet.get(i).getTitle();
                        String desc = dataSet.get(i).getDescription();

                        if (title.toLowerCase().contains(constraintStr.toString())
                                || desc.toLowerCase().contains(constraintStr.toString().toLowerCase())) {

                            if (filterDate != null) {
                                if ((filterDate.after(dataSet.get(i).getStartDate()) || filterDate.equals(dataSet.get(i).getStartDate())) && (filterDate.before(dataSet.get(i).getEndDate()) || filterDate.equals(dataSet.get(i).getEndDate()))) {
                                    filteredArrayNames.add(dataSet.get(i));

                                }
                            } else {
                                filteredArrayNames.add(dataSet.get(i));

                            }

                        }
                    }
                }
                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;

                return results;
            }
        };
    }
}
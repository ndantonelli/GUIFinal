package com.nantonelli.guifinal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nantonelli.guifinal.Model.Query;
import com.nantonelli.guifinal.Model.Song;
import com.nantonelli.guifinal.R;

import java.util.List;

/**
 * Created by ndantonelli on 12/13/15.
 */
public class RecentAdapter extends ArrayAdapter<Query>{
    private static class ViewHolder{
        TextView type;
        TextView searchString;
    }
    private Context context;
    private List<Query> queries;

    public RecentAdapter(Context context, List<Query> queries){
        super(context, 0, queries);
        this.queries = queries;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder vHold;
        final Query temp = queries.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            vHold = new ViewHolder();
            convertView = inflater.inflate(R.layout.component_recent_list, parent, false);
            vHold.type= (TextView) convertView.findViewById(R.id.recent_type);
            vHold.searchString= (TextView) convertView.findViewById(R.id.recent_query);
            convertView.setTag(vHold);
        } else {
            vHold = (ViewHolder)convertView.getTag();
        }

        vHold.searchString.setText(temp.item);
        vHold.type.setText(temp.attribute);

        return convertView;
    }
}

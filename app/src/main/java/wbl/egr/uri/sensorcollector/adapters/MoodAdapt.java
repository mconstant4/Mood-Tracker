package wbl.egr.uri.sensorcollector.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import wbl.egr.uri.sensorcollector.R;
import wbl.egr.uri.sensorcollector.models.MoodEntry;

/**
 * Created by root on 4/9/17.
 */

public class MoodAdapt extends ArrayAdapter<MoodEntry> {
    private static ArrayList<MoodEntry> users;
    private String allMoods[] = {"Happy", "Sad", "Angry", "Anxious", "Irritated"};
    private int[] colors = {Color.YELLOW, Color.CYAN, Color.RED, Color.GRAY, Color.GREEN};

    private LayoutInflater mInflater;

    public MoodAdapt(Context context, int textViewResourceId, ArrayList<MoodEntry> users) {
        super(context, textViewResourceId);
        this.users = users;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return users.size();
    }

    public MoodEntry getItem(int position) {
        return users.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mood_list, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.mood);
            holder.placeHolder = (TextView) convertView.findViewById(R.id.textView10);
            holder.placeHolder2 = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDate.setText(users.get(position).getMood());
        holder.txtDate.setGravity(Gravity.CENTER);

        for(int x = 0; x < allMoods.length; x++)
        {
            if(users.get(position).getMood().equals(allMoods[x]))
            {
                holder.txtDate.setBackgroundColor(colors[x]);
                holder.placeHolder.setBackgroundColor(colors[x]);
                holder.placeHolder2.setBackgroundColor(colors[x]);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtDate, placeHolder, placeHolder2;
    }
}

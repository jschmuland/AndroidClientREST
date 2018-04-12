package comjschmulandjavagroupproject.httpsgithub.restandroidclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StuffArrayAdapter extends ArrayAdapter<Stuff> {

    private static class ViewHolder{
        TextView stuffIDTextView;
        TextView stuffTextView;
        TextView moreStuffTextView;
    }

    //constructor to initialize superclass inherated members
    public StuffArrayAdapter(Context context, List<Stuff> stuffs){
        super(context, -1,stuffs);
    }

    //creates the custom views for the ListView's items
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        try {
            // get Stuff object for this specified ListView position
            Stuff stuffItem = getItem(position);
            ViewHolder viewHolder;  // object that reference's list item's views

            // check for reusable ViewHolder from a ListView item that scrolled
            // offscreen; otherwise, create a new ViewHolder

            if (convertView == null) {//no reusable ViewHolder, so create one
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView =
                        inflater.inflate(R.layout.list_item, parent, false);
                viewHolder.stuffIDTextView =
                        (TextView) convertView.findViewById(R.id.stuffIDTextView);
                viewHolder.stuffTextView =
                        (TextView) convertView.findViewById(R.id.stuffTextView);
                viewHolder.moreStuffTextView =
                        (TextView) convertView.findViewById(R.id.moreStuffTextView);
                convertView.setTag(viewHolder);

            } else {//reuse existing ViewHolder stored as the lis items tag
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // get other data from Stuff object and plage into views
            Context context = getContext();//for loading String resources
            viewHolder.stuffIDTextView.setText(context.getString(R.string.demoStuff_id, stuffItem.id));

            viewHolder.stuffTextView.setText(context.getString(R.string.demoStuff_stuff, stuffItem.stuff));

            viewHolder.moreStuffTextView.setText(context.getString(R.string.demoStuff_moreStuff, stuffItem.moreStuff));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return convertView; //return completed list item to display

    }
}

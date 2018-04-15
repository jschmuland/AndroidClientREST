package comjschmulandjavagroupproject.httpsgithub.restandroidclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StuffArrayAdapter extends ArrayAdapter<FishStick> {

    private static class ViewHolder{
        TextView fishStickIDTextView;
        TextView fishStickUUIDTextView;
        TextView fishStickLambdaTextView;
        TextView fishStickOmegaTextView;
        TextView fishStickRecordNumber;
    }

    //constructor to initialize superclass inherated members
    public StuffArrayAdapter(Context context, List<FishStick> stuffs){
        super(context, -1,stuffs);
    }

    //creates the custom views for the ListView's items
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        try {
            // get Stuff object for this specified ListView position
            FishStick fishStickItem = getItem(position);
            ViewHolder viewHolder;  // object that reference's list item's views

            // check for reusable ViewHolder from a ListView item that scrolled
            // offscreen; otherwise, create a new ViewHolder

            if (convertView == null) {//no reusable ViewHolder, so create one
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView =
                        inflater.inflate(R.layout.list_item, parent, false);

                viewHolder.fishStickIDTextView =
                        (TextView) convertView.findViewById(R.id.fishStickIDTextView);

                viewHolder.fishStickUUIDTextView =
                        (TextView) convertView.findViewById(R.id.fishStickUUIDTextView);

                viewHolder.fishStickLambdaTextView =
                        (TextView) convertView.findViewById(R.id.fishStickLambdaTextView);

                viewHolder.fishStickOmegaTextView =
                        (TextView) convertView.findViewById(R.id.fishStickOmegaTextView);

                viewHolder.fishStickRecordNumber =
                        (TextView) convertView.findViewById(R.id.fishStickRecordNumberTextView);

                convertView.setTag(viewHolder);

            } else {//reuse existing ViewHolder stored as the lis items tag
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // get other data from Stuff object and place into views
            Context context = getContext();//for loading String resources
            viewHolder.fishStickIDTextView.setText(context.getString(R.string.fishStick_id, fishStickItem.id));

            viewHolder.fishStickUUIDTextView.setText(context.getString(R.string.fishStick_uuid, fishStickItem.uuid));

            viewHolder.fishStickLambdaTextView.setText(context.getString(R.string.fishStick_lambda, fishStickItem.lambda));

            viewHolder.fishStickOmegaTextView.setText(context.getString(R.string.fishStick_omega, fishStickItem.omega));

            viewHolder.fishStickRecordNumber.setText(context.getString(R.string.fishStick_recordNumber, fishStickItem.recordNumber));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return convertView; //return completed list item to display

    }
}

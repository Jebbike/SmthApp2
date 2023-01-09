package melbet.malbet.hispone.plug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import melbet.malbet.hispone.R;

public class InfoListAdapter extends BaseAdapter {
    private Context context;
    List<Info> items;

    public InfoListAdapter(Context context, List<Info> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item, parent, false);
        }

        Info currentItem = (Info) getItem(position);

        TextView header = convertView.findViewById(R.id.item_header);
        TextView details = convertView.findViewById(R.id.item_details);

        header.setText(currentItem.getHeader());
        details.setText(currentItem.getDetails());



        return convertView;
    }
}

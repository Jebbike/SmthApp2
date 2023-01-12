package melbet.malbet.hispone.plug;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
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
        ImageView img = convertView.findViewById(R.id.item_icon);

        header.setText(currentItem.getHeader());
        details.setText(currentItem.getDetails());
        img.setImageDrawable(getDrawableFromAssets(currentItem.getImgSrc()));


        return convertView;
    }

    public Drawable getDrawableFromAssets(String fileName) {
        try {
            InputStream ims = context.getAssets().open(fileName);
            return Drawable.createFromStream(ims, null);
        } catch (Exception ex) {
            Log.d("InfoListAdapter", "Error: ", ex);
            return null;
        }
    }
}

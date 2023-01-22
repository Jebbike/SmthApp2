package bilyoner.vionter.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import bilyoner.vionter.R;


public class AnswerListAdapter extends ArrayAdapter<Answer> {

    public AnswerListAdapter(@NonNull Context context, List<Answer> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Answer answer = getItem(position);

        viewHolder.text = (TextView) convertView.findViewById(R.id.grid_item_title);
        viewHolder.text.setText(answer.getAnswer());
        convertView.setTag(R.string.TAG_ANSWER_ID, answer);


        return convertView;
    }

    public static class ViewHolder {
        TextView text;
    }
}
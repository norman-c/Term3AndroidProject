package ca.bcit.avoidit;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.bcit.avoidit.model.Record;

public class ObstructionListAdapter extends BaseAdapter {

    private Activity context;
    private List<Record> records;

    public ObstructionListAdapter(Activity context, List<Record> records){
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView obstruction_project_name;

        if(convertView == null){
            convertView = context.getLayoutInflater().inflate(R.layout.obstruction_detail,
                    parent, false);

            Record record = records.get(position);

            obstruction_project_name =
                    convertView.findViewById(R.id.txt_obstruction_project_name);

            obstruction_project_name.setText(record.getFields().getProject());

        }
        return convertView;
    }
}

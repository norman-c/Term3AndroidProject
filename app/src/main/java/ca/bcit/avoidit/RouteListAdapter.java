package ca.bcit.avoidit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import ca.bcit.avoidit.R;
import ca.bcit.avoidit.model.UserRoute;

public class RouteListAdapter extends ArrayAdapter<UserRoute> {
    private Activity context;
    private List<UserRoute> routeList;

    public RouteListAdapter(Activity context, List<UserRoute> routeList) {
        super(context, R.layout.routelist_layout, routeList);
        this.context = context;
        this.routeList = routeList;
    }

    public RouteListAdapter(Context context, int resource, List<UserRoute> objects, Activity context1, List<UserRoute> routeList) {
        super(context, resource, objects);
        this.context = context1;
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.routelist_layout, null, true);

        TextView tvName = listViewItem.findViewById(R.id.textViewName);
        TextView tvPointA = listViewItem.findViewById(R.id.textViewPointA);
        TextView tvPointB = listViewItem.findViewById(R.id.textViewPointB);

        UserRoute route = routeList.get(position);
        tvName.setText(route.getRouteName());
        tvPointA.setText(route.getRoutePointA());
        tvPointB.setText(route.getRoutePointB());

        return listViewItem;
    }
}

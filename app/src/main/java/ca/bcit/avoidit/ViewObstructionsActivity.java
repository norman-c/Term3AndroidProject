package ca.bcit.avoidit;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.bcit.avoidit.model.Record;

public class ViewObstructionsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //extract a string from a hazard
        ArrayList<Record> hazardList = new ArrayList<>();
        ArrayList<String> hazardStringList = new ArrayList<>();
        //TODO: extraction code from hazards goes here, but for now we have temp stuff
        hazardStringList.add("one");
        hazardStringList.add("two");
        hazardStringList.add("three");

        //add to listview
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, hazardStringList
        );

        ListView obstructionList = getListView();
        obstructionList.setAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast toast = Toast.makeText(getApplicationContext(), "Boop!", Toast.LENGTH_SHORT);
        toast.show();
    }
}

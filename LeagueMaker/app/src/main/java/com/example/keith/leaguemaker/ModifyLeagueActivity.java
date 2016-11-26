package com.example.keith.leaguemaker;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ModifyLeagueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String[] items = {"Football", "Ice Hockey", "Basketball"};
    private Cursor leagueToBeModified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_league);

        Spinner sports = (Spinner)findViewById(R.id.pickSport);
        sports.setOnItemSelectedListener(this);

        ArrayAdapter<String> spinData = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sports.setAdapter(spinData);

        Bundle passedRow = getIntent().getExtras();
        DBManager dbm = new DBManager(this);
        dbm.open();
        String query = "Select * from League where _id = " + passedRow.getInt("leagueRow");
        leagueToBeModified = dbm.rawQuery("Select * from League where _id = " + passedRow.getInt("leagueRow"));
        leagueToBeModified.moveToFirst();
        //Log.d("hello2", leagueToBeModified.getString(leagueToBeModified.getColumnIndex("leagueName")));

        dbm.close();

        String modifyName = leagueToBeModified.getString(leagueToBeModified.getColumnIndex("leagueName"));
        String modifySport = leagueToBeModified.getString(leagueToBeModified.getColumnIndex("sport"));

        EditText leagueName = (EditText)findViewById(R.id.editname);
        leagueName.setText(modifyName);
        int pos = spinData.getPosition(modifySport);
        sports.setSelection(pos);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}

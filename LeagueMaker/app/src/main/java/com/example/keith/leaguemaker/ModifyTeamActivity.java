package com.example.keith.leaguemaker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class ModifyTeamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Cursor teamToBeModified;
    private Cursor allLeagues;
    private Spinner leagues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_team);

        DBManager dbm = new DBManager(this);
        dbm.open();
        //get all leagues for spinner
        allLeagues = dbm.getAllLeagues();

        leagues = (Spinner) findViewById(R.id.pickLeague);
        leagues.setOnItemSelectedListener(this);

        String[] spinCol = new String[]{"leagueName"};
        int[] spinTo = new int[]{android.R.id.text1};

        SimpleCursorAdapter spinData = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allLeagues, spinCol, spinTo);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagues.setAdapter(spinData);

        Bundle passedRow = getIntent().getExtras();
        String query = "Select * from Team where _id = " + passedRow.getInt("teamRow");

        teamToBeModified = dbm.rawQuery(query);
        teamToBeModified.moveToFirst();


        dbm.close();

        String modifyName = teamToBeModified.getString(teamToBeModified.getColumnIndex("teamName"));
        String modifyLeague = teamToBeModified.getString(teamToBeModified.getColumnIndex("league"));
        Log.d("howdy", modifyLeague);

        EditText leagueName = (EditText)findViewById(R.id.editname);
        leagueName.setText(modifyName);

        int position = 0;
        for (int i=0; i < leagues.getCount(); i++){
            Cursor leagueRow = (Cursor) leagues.getItemAtPosition(i);
            if (leagueRow.getString(leagueRow.getColumnIndex("leagueName")).equals(modifyLeague)){
                position = i;
                Log.d("howdy2", String.valueOf(position));
            }
        }

        leagues.setSelection(position);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}

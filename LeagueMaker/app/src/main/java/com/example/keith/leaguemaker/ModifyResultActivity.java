package com.example.keith.leaguemaker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ModifyResultActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Cursor resultToBeModified;
    private Cursor allTeams;
    private Spinner team1, team2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_result);

        DBManager dbm = new DBManager(this);
        dbm.open();
        Bundle passedRow = getIntent().getExtras();
        String query = "Select * from Result where _id = " + passedRow.getInt("resultRow");

        resultToBeModified = dbm.rawQuery(query);
        resultToBeModified.moveToFirst();

        String league = resultToBeModified.getString(resultToBeModified.getColumnIndex("league"));
        String modifyTeam1 = resultToBeModified.getString(resultToBeModified.getColumnIndex("team1"));
        String modifyTeam2 = resultToBeModified.getString(resultToBeModified.getColumnIndex("team2"));
        String modifyTeam1Score = resultToBeModified.getString(resultToBeModified.getColumnIndex("team1score"));
        String modifyTeam2Score = resultToBeModified.getString(resultToBeModified.getColumnIndex("team2score"));

        allTeams = dbm.getLeagueTeams(league);

        team1 = (Spinner) findViewById(R.id.pickTeam1);
        team1.setOnItemSelectedListener(this);

        team2 = (Spinner) findViewById(R.id.pickTeam2);
        team2.setOnItemSelectedListener(this);

        String[] spinCol = new String[]{"teamName"};
        int[] spinTo = new int[]{android.R.id.text1};

        SimpleCursorAdapter spinData1 = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allTeams, spinCol, spinTo);
        spinData1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team1.setAdapter(spinData1);

        SimpleCursorAdapter spinData2 = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allTeams, spinCol, spinTo);
        spinData2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team2.setAdapter(spinData2);

        dbm.close();

        TextView leagueTV = (TextView)findViewById(R.id.leagueName);
        leagueTV.setText(league);

        EditText team1scoreET = (EditText)findViewById(R.id.team1score);
        team1scoreET.setText(modifyTeam1Score);

        EditText team2scoreET = (EditText)findViewById(R.id.team2score);
        team2scoreET.setText(modifyTeam2Score);

        int position = 0;
        for (int i=0; i < team1.getCount(); i++){
            Cursor teamRow = (Cursor) team1.getItemAtPosition(i);
            if (teamRow.getString(teamRow.getColumnIndex("teamName")).equals(modifyTeam1)){
                position = i;
            }
        }
        team1.setSelection(position);

        position = 0;
        for (int i=0; i < team2.getCount(); i++){
            Cursor teamRow = (Cursor) team2.getItemAtPosition(i);
            if (teamRow.getString(teamRow.getColumnIndex("teamName")).equals(modifyTeam2)){
                position = i;
            }
        }
        team2.setSelection(position);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}

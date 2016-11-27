package com.example.keith.leaguemaker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AddResultActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private View enterButton;
    Spinner team1, team2, leagues;
    Cursor allTeams, allLeagues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result);

        DBManager dbm = new DBManager(this);
        dbm.open();
        allLeagues = dbm.getAllLeagues();

        leagues = (Spinner) findViewById(R.id.pickLeague);
        leagues.setOnItemSelectedListener(this);

        String[] col = new String[]{"leagueName"};
        int[] to = new int[]{android.R.id.text1};

        SimpleCursorAdapter spinData = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allLeagues, col, to);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagues.setAdapter(spinData);

        dbm.close();

        enterButton = findViewById(R.id.insertButton);
        enterButton.setOnClickListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {
        if(parent.getId() == leagues.getId()) {
            Cursor leagueRow = (Cursor) leagues.getSelectedItem();
            String league = leagueRow.getString(leagueRow.getColumnIndex("leagueName"));

            DBManager dbm = new DBManager(this);
            dbm.open();
            //get all teams for the selected league
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
        }
    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void onClick(View v)
    {
        if(v.getId() == enterButton.getId())
        {
            String team1name, team2name, league;
            int team1score, team2score;

            Spinner leagueSpinner =(Spinner) findViewById(R.id.pickLeague);
            Cursor leagueRow = (Cursor) leagueSpinner.getSelectedItem();
            league = leagueRow.getString(leagueRow.getColumnIndex("leagueName"));

            Spinner team1Spinner =(Spinner) findViewById(R.id.pickTeam1);
            Cursor team1Row = (Cursor) team1Spinner.getSelectedItem();
            team1name = team1Row.getString(team1Row.getColumnIndex("teamName"));

            EditText team1scoreET = (EditText)findViewById(R.id.team1score);
            team1score = Integer.parseInt(team1scoreET.getText().toString());

            EditText team2scoreET = (EditText)findViewById(R.id.team2score);
            team2score = Integer.parseInt(team2scoreET.getText().toString());

            Spinner team2Spinner =(Spinner) findViewById(R.id.pickTeam2);
            Cursor team2Row = (Cursor) team2Spinner.getSelectedItem();
            team2name = team2Row.getString(team2Row.getColumnIndex("teamName"));

            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.insertResult(league, team1name, team2name, team1score, team2score);
            dbm.close();

            //confirm to user that result was added
            Toast.makeText(this, team1name + " " + team1score + " - " +
                            team2score + " " + team2name + " added to " + league , (Toast.LENGTH_SHORT)).show();

            //reset the add result form
            team1scoreET.setText("");
            team2scoreET.setText("");
            team1Spinner.setSelection(0);
            team2Spinner.setSelection(0);
        }
    }
}

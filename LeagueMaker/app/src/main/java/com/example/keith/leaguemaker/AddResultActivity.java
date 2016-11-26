package com.example.keith.leaguemaker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        allTeams = dbm.getAllTeams();

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

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void onClick(View v)
    {
        if(v.getId() == enterButton.getId())
        {
//            String name, sport, image;
//            EditText leagueNameET = (EditText)findViewById(R.id.editname);
//            name = leagueNameET.getText().toString();
//            Spinner sportSpinner =(Spinner) findViewById(R.id.pickSport);
//            sport = sportSpinner.getSelectedItem().toString();
//            //temporary image text
//            image = "pathToImage";
//            DBManager dbm = new DBManager(this);
//            dbm.open();
//            dbm.insertLeague(name, sport, image);
//            dbm.close();
//
//            //confirm to user that league was added
//            Toast.makeText(this, name + " added", (Toast.LENGTH_SHORT)).show();
//
//            //reset the add league form
//            leagueNameET.setText("");
//            sportSpinner.setSelection(0);
        }
    }
}

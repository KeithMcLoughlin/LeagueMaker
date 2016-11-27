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
import android.widget.Toast;

public class ModifyResultActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Cursor resultToBeModified;
    private Cursor allTeams;
    private Spinner team1, team2;
    private View deleteButton, modifyButton;
    int rowID;
    String league;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_result);

        DBManager dbm = new DBManager(this);
        dbm.open();
        Bundle passedRow = getIntent().getExtras();
        rowID = passedRow.getInt("resultRow");
        String query = "Select * from Result where _id = " + rowID;

        resultToBeModified = dbm.rawQuery(query);
        resultToBeModified.moveToFirst();

        league = resultToBeModified.getString(resultToBeModified.getColumnIndex("league"));
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

        deleteButton = (View)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        modifyButton = (View)findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void onClick(View v)
    {
        if(v.getId() == modifyButton.getId())
        {
            String team1name, team2name;
            int team1score, team2score;

            Cursor team1Row = (Cursor) team1.getSelectedItem();
            team1name = team1Row.getString(team1Row.getColumnIndex("teamName"));

            EditText team1scoreET = (EditText)findViewById(R.id.team1score);
            team1score = Integer.parseInt(team1scoreET.getText().toString());

            EditText team2scoreET = (EditText)findViewById(R.id.team2score);
            team2score = Integer.parseInt(team2scoreET.getText().toString());

            Cursor team2Row = (Cursor) team2.getSelectedItem();
            team2name = team2Row.getString(team2Row.getColumnIndex("teamName"));

            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.updateResult(rowID, league, team1name, team2name, team1score, team2score);
            dbm.close();

            Toast.makeText(this, "Result updated", (Toast.LENGTH_SHORT)).show();
            finish();
        }

        if(v.getId() == deleteButton.getId())
        {
            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.delete("Result", rowID);
            dbm.close();

            Toast.makeText(this, "Result deleted", (Toast.LENGTH_SHORT)).show();
            finish();
        }
    }
}

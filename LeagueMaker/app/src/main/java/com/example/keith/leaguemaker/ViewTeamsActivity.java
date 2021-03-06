package com.example.keith.leaguemaker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewTeamsActivity extends ListActivity implements AdapterView.OnItemSelectedListener{

    private Spinner leagues;
    private Cursor allLeagues;
    private Cursor allTeams;
    private String[] columns;
    private int[] to;
    private SimpleCursorAdapter teamsCA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teams);

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

        dbm.close();
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Cursor leagueRow = (Cursor) leagues.getSelectedItem();
        String league = leagueRow.getString(leagueRow.getColumnIndex("leagueName"));

        DBManager dbm = new DBManager(this);
        dbm.open();
        //get all teams for the selected league
        allTeams = dbm.getLeagueTeams(league);
        columns = new String[]{};
        to = new int[]{};

        setListAdapter(new TeamCustomAdapter(ViewTeamsActivity.this, R.layout.view_teams_row, allTeams, columns, to));
//        columns = new String[]{"teamName"};
//        to = new int[]{R.id.teamName};
//        teamsCA = new SimpleCursorAdapter(ViewTeamsActivity.this, R.layout.view_teams_row, allTeams, columns, to, 0);
//        setListAdapter(teamsCA);
//
//        allTeams.moveToFirst();
//        do{
//            String team = allTeams.getString(allTeams.getColumnIndex("teamName"));
//            Cursor results = dbm.getTeamResults(team);
//            int gamesPlayed = results.getCount();
//            TextView games = (TextView)findViewById(R.id.gamePlayed);
//            games.setText(String.valueOf(gamesPlayed));
//        }
//        while(allTeams.moveToNext());
        dbm.close();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        allTeams.moveToPosition(position);
        int rowID = allTeams.getInt(allTeams.getColumnIndex("_id"));

        Intent changeToModify = new Intent(this, ModifyTeamActivity.class);
        changeToModify.putExtra("teamRow", rowID);
        startActivity(changeToModify);
    }
}

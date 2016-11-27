package com.example.keith.leaguemaker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class ViewLeaguesActivity extends ListActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner leagues;
    private Cursor allLeagues;
    private Cursor allTeams;
    private String[] columns;
    private int[] to;
    private SimpleCursorAdapter teamsCA;
    private View modifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leagues);

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

        modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(this);

        dbm.close();
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Cursor leagueRow = (Cursor) leagues.getSelectedItem();
        String league = leagueRow.getString(leagueRow.getColumnIndex("leagueName"));
        Log.d("leagueName", league);

        DBManager dbm = new DBManager(this);
        dbm.open();
        //get all teams for the selected league
        allTeams = dbm.getLeagueTeams(league);

        columns = new String[]{"teamName"};
        to = new int[]{R.id.teamName};
        teamsCA = new SimpleCursorAdapter(ViewLeaguesActivity.this, R.layout.view_leagues_row, allTeams, columns, to, 0);
        setListAdapter(teamsCA);
        dbm.close();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

    }

    public void onClick(View v) {
        if (v.getId() == modifyButton.getId()) {
            //int position = leagues.getSelectedItemPosition() + 1;

            Cursor leagueRow = (Cursor) leagues.getSelectedItem();
            int row = Integer.parseInt(leagueRow.getString(leagueRow.getColumnIndex("_id")));

            Intent changeToModify = new Intent(this, ModifyLeagueActivity.class);
            changeToModify.putExtra("leagueRow", row);
            startActivity(changeToModify);

        }
    }
}

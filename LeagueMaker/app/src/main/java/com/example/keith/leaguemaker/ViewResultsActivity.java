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

public class ViewResultsActivity extends ListActivity  implements AdapterView.OnItemSelectedListener{

    private Spinner leagues;
    private Cursor allLeagues;
    private Cursor allResults;
    private String[] columns;
    private int[] to;
    private SimpleCursorAdapter resultCA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);

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
        //get all results for the selected league
        allResults = dbm.getLeagueResults(league);

        columns = new String[]{"team1", "team1score", "team2score", "team2"};
        to = new int[]{R.id.team1name, R.id.team1score, R.id.team2score, R.id.team2name};
        resultCA = new SimpleCursorAdapter(ViewResultsActivity.this, R.layout.view_results_row, allResults, columns, to, 0);
        setListAdapter(resultCA);
        dbm.close();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        allResults.moveToPosition(position);
        int rowID = allResults.getInt(allResults.getColumnIndex("_id"));

        Intent changeToModify = new Intent(this, ModifyResultActivity.class);
        changeToModify.putExtra("resultRow", rowID);
        startActivity(changeToModify);
    }
}

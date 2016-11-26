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

public class AddTeamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner leagues;
    private Cursor allLeagues;
    private View enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        DBManager dbm = new DBManager(this);
        dbm.open();
        allLeagues = dbm.getAllLeagues();

        leagues = (Spinner) findViewById(R.id.pickLeague);
        leagues.setOnItemSelectedListener(this);

        String[] spinCol = new String[]{"leagueName"};
        int[] spinTo = new int[]{android.R.id.text1};

        SimpleCursorAdapter spinData = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allLeagues, spinCol, spinTo);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagues.setAdapter(spinData);

        dbm.close();

        enterButton = findViewById(R.id.insertButton);
        enterButton.setOnClickListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void onClick(View v)
    {
        if(v.getId() == enterButton.getId())
        {
            String name, league, image;
            EditText teamNameET = (EditText)findViewById(R.id.editname);
            name = teamNameET.getText().toString();
            Spinner leagueSpinner =(Spinner) findViewById(R.id.pickLeague);
            Cursor leagueRow = (Cursor) leagueSpinner.getSelectedItem();
            league = leagueRow.getString(leagueRow.getColumnIndex("leagueName"));

            //temporary image text
            image = "pathToImage";
            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.insertTeam(name, league, image);
            dbm.close();

            //confirm to user that team was added to the league
            Toast.makeText(this, name + " added to " + league, (Toast.LENGTH_SHORT)).show();

            //reset the add team form
            teamNameET.setText("");
            leagueSpinner.setSelection(0);
        }
    }
}

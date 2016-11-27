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
import android.widget.TextView;
import android.widget.Toast;

public class ModifyTeamActivity extends AppCompatActivity implements View.OnClickListener{

    private Cursor teamToBeModified;
    private Cursor allLeagues;
    private TextView leagueTV;
    private View deleteButton, modifyButton;
    private EditText teamName;
    private int rowID;
    private String modifyName, modifyLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_team);

        DBManager dbm = new DBManager(this);
        dbm.open();

        Bundle passedRow = getIntent().getExtras();
        rowID = passedRow.getInt("teamRow");
        String query = "Select * from Team where _id = " + rowID;

        teamToBeModified = dbm.rawQuery(query);
        teamToBeModified.moveToFirst();

        dbm.close();

        modifyName = teamToBeModified.getString(teamToBeModified.getColumnIndex("teamName"));
        modifyLeague = teamToBeModified.getString(teamToBeModified.getColumnIndex("league"));

        teamName = (EditText)findViewById(R.id.editname);
        teamName.setText(modifyName);

        leagueTV = (TextView)findViewById(R.id.leagueName);
        leagueTV.setText(modifyLeague);

        deleteButton = (View)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        modifyButton = (View)findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        if(v.getId() == modifyButton.getId())
        {
            String name, league, image;

            name = teamName.getText().toString();
            league = modifyLeague;
            //temporary image text
            image = "pathToImage";

            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.updateTeam(rowID, name, league, image);
            //if team name changed
            if(modifyName != name)
            {
                dbm.updateResultTeamName(modifyName, name);
            }
            dbm.close();

            Toast.makeText(this, "Team updated", (Toast.LENGTH_SHORT)).show();
            finish();
        }

        if(v.getId() == deleteButton.getId())
        {
            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.delete("Team", rowID);
            dbm.close();

            Toast.makeText(this, "Team deleted", (Toast.LENGTH_SHORT)).show();
            finish();
        }
    }
}

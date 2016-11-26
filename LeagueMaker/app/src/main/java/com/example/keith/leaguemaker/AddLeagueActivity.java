package com.example.keith.leaguemaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddLeagueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private String[] items = {"Football", "Ice Hockey", "Basketball"};
    private View enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_league);

        Spinner sports = (Spinner)findViewById(R.id.pickSport);
        sports.setOnItemSelectedListener(this);
        enterButton = findViewById(R.id.insertButton);

        ArrayAdapter<String> spinData = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sports.setAdapter(spinData);
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
            String name, sport, image;
            EditText leagueNameET = (EditText)findViewById(R.id.editname);
            name = leagueNameET.getText().toString();
            Spinner sportSpinner =(Spinner) findViewById(R.id.pickSport);
            sport = sportSpinner.getSelectedItem().toString();
            //temporary image text
            image = "pathToImage";
            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.insertLeague(name, sport, image);
            dbm.close();

            //confirm to user that league was added
            Toast.makeText(this, name + " added", (Toast.LENGTH_SHORT)).show();

            //reset the add league form
            leagueNameET.setText("");
            sportSpinner.setSelection(0);
        }
    }
}

package com.example.keith.leaguemaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddLeagueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] items = {"Football", "Ice Hockey", "Basketball"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_league);

        Spinner sports = (Spinner)findViewById(R.id.pickSport);
        sports.setOnItemSelectedListener(this);

        ArrayAdapter<String> spinData = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sports.setAdapter(spinData);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}

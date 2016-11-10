package com.example.keith.leaguemaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    View leaguesButton;
    View teamsButton;
    View resultsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leaguesButton = findViewById(R.id.leaguesbutton);
        teamsButton = findViewById(R.id.teamsbutton);
        resultsButton = findViewById(R.id.resultsbutton);

        leaguesButton.setOnClickListener(this);
        teamsButton.setOnClickListener(this);
        resultsButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        //go to leagues page
        if(v.getId() == leaguesButton.getId()) {
            Intent leagues = new Intent(this, LeaguesActivity.class);
            startActivity(leagues);
        }

        //go to teams page
        if(v.getId() == teamsButton.getId()) {
            Intent teams = new Intent(this, TeamsActivity.class);
            startActivity(teams);
        }

        //go to results page
        if(v.getId() == resultsButton.getId()) {
            Intent results = new Intent(this, ResultsActivity.class);
            startActivity(results);
        }
    }
}

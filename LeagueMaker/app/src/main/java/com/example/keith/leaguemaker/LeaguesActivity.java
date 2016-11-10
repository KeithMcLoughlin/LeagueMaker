package com.example.keith.leaguemaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LeaguesActivity extends AppCompatActivity implements View.OnClickListener {

    View addButton;
    View viewButton;
    View backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);

        addButton = findViewById(R.id.addLeagueButton);
        viewButton = findViewById(R.id.viewLeaguesbutton);
        backButton = findViewById(R.id.backButton);

        addButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        //go to add a league page
        if(v.getId() == addButton.getId()) {
            Intent addLeague = new Intent(this, AddLeagueActivity.class);
            startActivity(addLeague);
        }

        //go to view/modify leagues page
        if(v.getId() == viewButton.getId()) {
            Intent viewLeagues = new Intent(this, ViewLeaguesActivity.class);
            startActivity(viewLeagues);
        }

        //go to previous page
        if(v.getId() == backButton.getId()) {
            finish();
        }
    }
}

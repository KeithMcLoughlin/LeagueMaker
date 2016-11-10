package com.example.keith.leaguemaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TeamsActivity extends AppCompatActivity implements View.OnClickListener
{

    View addButton;
    View viewButton;
    View backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        addButton = findViewById(R.id.addTeamButton);
        viewButton = findViewById(R.id.viewTeamsButton);
        backButton = findViewById(R.id.backButton);

        addButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        //go to add a team page
        if(v.getId() == addButton.getId()) {
            Intent addTeam = new Intent(this, AddTeamActivity.class);
            startActivity(addTeam);
        }

        //go to view/modify teams page
        if(v.getId() == viewButton.getId()) {
            Intent viewTeams = new Intent(this, ViewTeamsActivity.class);
            startActivity(viewTeams);
        }

        //go to previous page
        if(v.getId() == backButton.getId()) {
            finish();
        }
    }
}

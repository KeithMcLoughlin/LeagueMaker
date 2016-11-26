package com.example.keith.leaguemaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener{

    private View addButton;
    private View viewButton;
    private View backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        addButton = findViewById(R.id.addResultButton);
        viewButton = findViewById(R.id.viewsResultsButton);
        backButton = findViewById(R.id.backButton);

        addButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        //go to add a result page
        if(v.getId() == addButton.getId()) {
            Intent addResult = new Intent(this, AddResultActivity.class);
            startActivity(addResult);
        }

        //go to view/modify results page
        if(v.getId() == viewButton.getId()) {
            Intent viewResults = new Intent(this, ViewResultsActivity.class);
            startActivity(viewResults);
        }

        //go to previous page
        if(v.getId() == backButton.getId()) {
            finish();
        }
    }
}

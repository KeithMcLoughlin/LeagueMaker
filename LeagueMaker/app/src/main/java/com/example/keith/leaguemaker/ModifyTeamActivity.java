package com.example.keith.leaguemaker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ModifyTeamActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int CHOOSE_IMAGE_ID = 20;

    private Cursor teamToBeModified;
    private Cursor allLeagues;
    private TextView leagueTV;
    private View deleteButton, modifyButton;
    private EditText teamName;
    private int rowID;
    private String modifyName, modifyLeague;
    private Bitmap teamLogo;
    private ImageButton imageButton;

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

        teamLogo = dbm.getImage("Team", rowID);

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

        imageButton = (ImageButton)findViewById(R.id.logo);
        imageButton.setOnClickListener(this);
        imageButton.setImageBitmap(teamLogo);
    }

    public void onClick(View v)
    {
        if(v.getId() == modifyButton.getId())
        {
            String name, league;
            byte[] image;

            name = teamName.getText().toString();
            league = modifyLeague;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            teamLogo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            image = bos.toByteArray();

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
            dbm.deleteTeamResults(modifyName);
            dbm.close();

            Toast.makeText(this, "Team deleted", (Toast.LENGTH_SHORT)).show();
            finish();
        }

        if(v.getId() == imageButton.getId())
        {
            //invoke an intent for picking data
            Intent chooseImage = new Intent(Intent.ACTION_PICK);
            //choosing the type of data
            File choosenImageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String path = choosenImageDir.getPath();
            Uri data = Uri.parse(path);

            //setting the intent to choose from the data type
            chooseImage.setDataAndType(data, "image/*");
            startActivityForResult(chooseImage, CHOOSE_IMAGE_ID);
        }
    }

    @Override
    protected void onActivityResult(int id, int result, Intent data) {
        //check if anything went wrong
        if(result == RESULT_OK)
        {
            //check if its the choose image activity
            if(id == CHOOSE_IMAGE_ID)
            {
                Uri imageAddress = data.getData();
                //used to read image data
                InputStream inStream;

                try
                {
                    inStream = getContentResolver().openInputStream(imageAddress);

                    teamLogo = BitmapFactory.decodeStream(inStream);
                    imageButton.setImageBitmap(teamLogo);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

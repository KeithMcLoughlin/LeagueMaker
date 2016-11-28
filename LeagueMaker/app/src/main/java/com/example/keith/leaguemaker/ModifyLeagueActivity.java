package com.example.keith.leaguemaker;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class ModifyLeagueActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int CHOOSE_IMAGE_ID = 20;

    private String[] items = {"Football", "Ice Hockey", "Basketball"};
    private Cursor leagueToBeModified;
    private Bitmap leagueLogo;
    private View deleteButton, modifyButton;
    private ImageButton imageButton;
    private String modifyName, modifySport;
    private int leagueRow;
    private EditText leagueName;
    private TextView sportTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_league);

        Bundle passedRow = getIntent().getExtras();
        DBManager dbm = new DBManager(this);
        dbm.open();
        leagueRow = passedRow.getInt("leagueRow");

        leagueToBeModified = dbm.getLeague(leagueRow);
        leagueToBeModified.moveToFirst();

        leagueLogo = dbm.getImage("League", leagueRow);
        dbm.close();

        modifyName = leagueToBeModified.getString(leagueToBeModified.getColumnIndex("leagueName"));
        modifySport = leagueToBeModified.getString(leagueToBeModified.getColumnIndex("sport"));

        leagueName = (EditText)findViewById(R.id.editname);
        leagueName.setText(modifyName);
//        int pos = spinData.getPosition(modifySport);
//        sports.setSelection(pos);
        sportTV = (TextView)findViewById(R.id.sportName);
        sportTV.setText(modifySport);

        imageButton = (ImageButton)findViewById(R.id.logo);
        imageButton.setOnClickListener(this);
        imageButton.setImageBitmap(leagueLogo);

        deleteButton = (View)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        modifyButton = (View)findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        if(v.getId() == modifyButton.getId())
        {
            String name, sport;
            byte[] image;

            name = leagueName.getText().toString();
            sport = modifySport;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            leagueLogo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            image = bos.toByteArray();

            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.updateLeague(leagueRow, name, sport, image);
            //if team name changed
            if(modifyName != name)
            {
                dbm.updateLeagueName(modifyName, name);
            }
            dbm.close();

            Toast.makeText(this, "League updated", (Toast.LENGTH_SHORT)).show();
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

        if(v.getId() == deleteButton.getId())
        {
            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.delete("League", leagueRow);
            dbm.deleteLeagueTeamsAndResults(modifyName);
            dbm.close();

            Toast.makeText(this, "League deleted", (Toast.LENGTH_SHORT)).show();
            finish();
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

                    leagueLogo = BitmapFactory.decodeStream(inStream);
                    imageButton.setImageBitmap(leagueLogo);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class ModifyLeagueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    public static final int CHOOSE_IMAGE_ID = 20;

    private String[] items = {"Football", "Ice Hockey", "Basketball"};
    private Cursor leagueToBeModified;
    private Bitmap leagueLogo;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_league);

        Spinner sports = (Spinner)findViewById(R.id.pickSport);
        sports.setOnItemSelectedListener(this);

        ArrayAdapter<String> spinData = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sports.setAdapter(spinData);

        Bundle passedRow = getIntent().getExtras();
        DBManager dbm = new DBManager(this);
        dbm.open();
        int leagueRow = passedRow.getInt("leagueRow");

        leagueToBeModified = dbm.getLeague(leagueRow);
        leagueToBeModified.moveToFirst();

        leagueLogo = dbm.getImage("League", leagueRow);
        dbm.close();

        String modifyName = leagueToBeModified.getString(leagueToBeModified.getColumnIndex("leagueName"));
        String modifySport = leagueToBeModified.getString(leagueToBeModified.getColumnIndex("sport"));

        EditText leagueName = (EditText)findViewById(R.id.editname);
        leagueName.setText(modifyName);
        int pos = spinData.getPosition(modifySport);
        sports.setSelection(pos);

        imageButton = (ImageButton)findViewById(R.id.logo);
        imageButton.setOnClickListener(this);
        imageButton.setImageBitmap(leagueLogo);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void onClick(View v)
    {
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

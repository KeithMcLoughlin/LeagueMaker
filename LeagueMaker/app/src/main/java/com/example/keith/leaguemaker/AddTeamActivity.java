package com.example.keith.leaguemaker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddTeamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    public static final int CHOOSE_IMAGE_ID = 20;

    private Spinner leagues;
    private Cursor allLeagues;
    private View enterButton;
    private ImageView imageButton;
    private Bitmap chosenImage, defaultImage;
    private Drawable defaultDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        DBManager dbm = new DBManager(this);
        dbm.open();
        allLeagues = dbm.getAllLeagues();

        leagues = (Spinner) findViewById(R.id.pickLeague);
        leagues.setOnItemSelectedListener(this);

        String[] spinCol = new String[]{"leagueName"};
        int[] spinTo = new int[]{android.R.id.text1};

        SimpleCursorAdapter spinData = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allLeagues, spinCol, spinTo);
        spinData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagues.setAdapter(spinData);

        dbm.close();

        enterButton = findViewById(R.id.insertButton);
        enterButton.setOnClickListener(this);

        imageButton = (ImageView)findViewById(R.id.teamLogo);
        imageButton.setOnClickListener(this);

        Drawable defaultDrawable = imageButton.getBackground();
        chosenImage = ((BitmapDrawable)defaultDrawable).getBitmap();
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
            String name, league;
            byte[] image;

            EditText teamNameET = (EditText)findViewById(R.id.editname);
            name = teamNameET.getText().toString();
            Spinner leagueSpinner =(Spinner) findViewById(R.id.pickLeague);
            Cursor leagueRow = (Cursor) leagueSpinner.getSelectedItem();
            league = leagueRow.getString(leagueRow.getColumnIndex("leagueName"));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            chosenImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
            image = bos.toByteArray();

            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.insertTeam(name, league, image);
            dbm.close();

            //confirm to user that team was added to the league
            Toast.makeText(this, name + " added to " + league, (Toast.LENGTH_SHORT)).show();

            //reset the add team form
            teamNameET.setText("");
            leagueSpinner.setSelection(0);
            imageButton.setImageDrawable(defaultDrawable);
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

                    chosenImage = BitmapFactory.decodeStream(inStream);
                    imageButton.setImageBitmap(chosenImage);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

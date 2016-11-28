package com.example.keith.leaguemaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class AddLeagueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    public static final int CHOOSE_IMAGE_ID = 20;

    private String[] items = {"Football", "Ice Hockey", "Basketball"};
    private View enterButton;
    private ImageView imageButton;
    private Bitmap chosenImage, defaultImage;
    private Drawable defaultDrawable;

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

        imageButton = (ImageView)findViewById(R.id.leagueLogo);
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
            String name, sport;
            byte[] image;

            EditText leagueNameET = (EditText)findViewById(R.id.editname);
            name = leagueNameET.getText().toString();
            Spinner sportSpinner =(Spinner) findViewById(R.id.pickSport);
            sport = sportSpinner.getSelectedItem().toString();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            chosenImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
            image = bos.toByteArray();

            DBManager dbm = new DBManager(this);
            dbm.open();
            dbm.insertLeague(name, sport, image);
            dbm.close();

            //confirm to user that league was added
            Toast.makeText(this, name + " added", (Toast.LENGTH_SHORT)).show();

            //reset the add league form
            leagueNameET.setText("");
            sportSpinner.setSelection(0);
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

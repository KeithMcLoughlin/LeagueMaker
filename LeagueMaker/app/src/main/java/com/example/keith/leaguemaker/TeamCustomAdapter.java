package com.example.keith.leaguemaker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Keith on 28/11/2016.
 */
public class TeamCustomAdapter extends SimpleCursorAdapter {
    private Cursor teams;
    Context context;
    private LayoutInflater inflater;
    private int layout;

    public TeamCustomAdapter(Context context, int textViewResourceId,
                             Cursor teams, String[] columns, int[] to) {
        super(context, textViewResourceId, teams, columns, to, 0);

        this.teams = teams;
        this.context = context;
        layout = textViewResourceId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView team = (TextView) view.findViewById(R.id.teamName);
        TextView games = (TextView) view.findViewById(R.id.gamePlayed);
        ImageView image = (ImageView) view.findViewById(R.id.teamImage);

        String teamName = cursor.getString(cursor.getColumnIndex("teamName"));
        DBManager dbm = new DBManager(context);
        dbm.open();
        Cursor results = dbm.getTeamResults(teamName);
        String gamesPlayed = String.valueOf(results.getCount());

        team.setText(teamName);
        games.setText("Games Played: " + gamesPlayed);

        Bitmap teamLogo = dbm.getImage("Team", cursor.getInt(cursor.getColumnIndex("_id")));
        image.setImageBitmap(teamLogo);
        dbm.close();

    }
}

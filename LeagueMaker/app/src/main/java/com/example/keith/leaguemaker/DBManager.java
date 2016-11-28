package com.example.keith.leaguemaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Keith on 17/11/2016.
 * */

public class DBManager
{
    public static final String KEY_ROWID = "_id";

    //league keys
    public static final String KEY_LEAGUENAME 	= "leagueName";
    public static final String KEY_SPORT 	    = "sport";
    public static final String KEY_LEAGUEIMAGE 	= "image";

    //Team keys
    public static final String KEY_TEAMNAME 	= "teamName";
    public static final String KEY_LEAGUE	    = "league";
    public static final String KEY_TEAMIMAGE 	= "image";

    //Result keys
    public static final String KEY_RESULT_LEAGUE    = "league";
    public static final String KEY_TEAM1NAME 	    = "team1";
    public static final String KEY_TEAM2NAME 	    = "team2";
    public static final String KEY_TEAM1SCORE 	    = "team1score";
    public static final String KEY_TEAM2SCORE 	    = "team2score";

    //database name & table keys
    private static final String DATABASE_NAME 	= "LeagueMaker";
    private static final String DATABASE_LEAGUE_TABLE = "League";
    private static final String DATABASE_TEAM_TABLE = "Team";
    private static final String DATABASE_RESULT_TABLE = "Result";
    private static final int DATABASE_VERSION 	= 1;

    //sql to create database
    private static final String DATABASE_CREATE_LEAGUE =
            "create table League (_id integer primary key autoincrement, " +
                    "leagueName text unique not null, " +
                    "sport text not null, "  +
                    "image blob);";
    private static final String DATABASE_CREATE_TEAM =
            "create table Team (_id integer primary key autoincrement, " +
                    "teamName text unique not null, " +
                    "league text not null, "  +
                    "image blob);";
    private static final String DATABASE_CREATE_RESULT =
            "create table Result (_id integer primary key autoincrement, " +
                    "league text not null, " +
                    "team1 text not null, " +
                    "team2 text not null, "  +
                    "team1score int not null, " +
                    "team2score int not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBManager(Context ctx)
    {
        this.context 	= ctx;
        DBHelper 		= new DatabaseHelper(context);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE_LEAGUE);
            db.execSQL(DATABASE_CREATE_TEAM);
            db.execSQL(DATABASE_CREATE_RESULT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        }
    }

    public DBManager open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertLeague(String name, String sport, byte[] image) throws NullPointerException
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LEAGUENAME, name);
        initialValues.put(KEY_SPORT, sport);
        initialValues.put(KEY_LEAGUEIMAGE, image);
        return db.insert(DATABASE_LEAGUE_TABLE, null, initialValues);
    }

    public long insertTeam(String name, String league, byte[] image) throws NullPointerException
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TEAMNAME, name);
        initialValues.put(KEY_LEAGUE, league);
        initialValues.put(KEY_TEAMIMAGE, image);
        return db.insert(DATABASE_TEAM_TABLE, null, initialValues);
    }

    public long insertResult(String league, String team1, String team2, int score1, int score2) throws NullPointerException
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RESULT_LEAGUE, league);
        initialValues.put(KEY_TEAM1NAME, team1);
        initialValues.put(KEY_TEAM2NAME, team2);
        initialValues.put(KEY_TEAM1SCORE, score1);
        initialValues.put(KEY_TEAM2SCORE, score2);
        return db.insert(DATABASE_RESULT_TABLE, null, initialValues);
    }

    public boolean delete(String table, long rowId)
    {
        return db.delete(table, KEY_ROWID +
                "=" + rowId, null) > 0;
    }

    public Cursor getAllLeagues() throws SQLException
    {
        return db.query(DATABASE_LEAGUE_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_LEAGUENAME,
                                KEY_SPORT
                        },
                null, null, null, null, null);
    }

    public Bitmap getImage(String table, int rowId) throws SQLException
    {
        String imageQuery = "Select image from " + table + " where _id = " + rowId;
        Cursor tableRow = rawQuery(imageQuery);

        tableRow.moveToFirst();

        byte[] image = tableRow.getBlob(tableRow.getColumnIndex("image"));
        Bitmap rowImage = BitmapFactory.decodeByteArray(image, 0, image.length);

        return rowImage;
    }

    public Cursor getAllTeams() throws SQLException
    {
        return db.query(DATABASE_TEAM_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_TEAMNAME,
                                KEY_LEAGUE,
                                KEY_TEAMIMAGE
                        },
                null, null, null, null, null);
    }

    public Cursor getLeagueTeams(String leagueName) throws SQLException
    {
        return db.query(DATABASE_TEAM_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_TEAMNAME,
                                KEY_LEAGUE,
                                KEY_TEAMIMAGE
                        },
                "league=?", new String[]{ leagueName }, null, null, null);
    }

    public Cursor getAllResults() throws SQLException
    {
        return db.query(DATABASE_RESULT_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_RESULT_LEAGUE,
                                KEY_TEAM1NAME,
                                KEY_TEAM2NAME,
                                KEY_TEAM1SCORE,
                                KEY_TEAM2SCORE,
                        },
                null, null, null, null, null);
    }

    public Cursor getLeagueResults(String leagueName) throws SQLException
    {
        return db.query(DATABASE_RESULT_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_RESULT_LEAGUE,
                                KEY_TEAM1NAME,
                                KEY_TEAM2NAME,
                                KEY_TEAM1SCORE,
                                KEY_TEAM2SCORE,
                        },
                "league=?", new String[] { leagueName }, null, null, null);
    }

    public Cursor getLeague(long rowId) throws SQLException
    {
        return db.query(DATABASE_LEAGUE_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_LEAGUENAME,
                                KEY_SPORT
                        },
                "_id=" + rowId, null, null, null, null);
    }

    public Cursor getTeam(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TEAM_TABLE, new String[]
                                {
                                        KEY_ROWID,
                                        KEY_TEAMNAME,
                                        KEY_LEAGUE,
                                        KEY_TEAMIMAGE
                                },
                        KEY_ROWID + "=" + rowId,  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getResult(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_RESULT_TABLE, new String[]
                                {
                                        KEY_ROWID,
                                        KEY_RESULT_LEAGUE,
                                        KEY_TEAM1NAME,
                                        KEY_TEAM2NAME,
                                        KEY_TEAM1SCORE,
                                        KEY_TEAM2SCORE,
                                },
                        KEY_ROWID + "=" + rowId,  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public boolean updateLeague(long rowId, String name, String sport, byte[] image)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_LEAGUENAME, name);
        args.put(KEY_SPORT, sport);
        args.put(KEY_LEAGUEIMAGE, image);
        return db.update(DATABASE_LEAGUE_TABLE, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateTeam(long rowId, String name, String league, byte[] image)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TEAMNAME, name);
        args.put(KEY_LEAGUE, league);
        args.put(KEY_TEAMIMAGE, image);
        return db.update(DATABASE_TEAM_TABLE, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateResult(long rowId, String league, String team1, String team2, int score1, int score2)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_RESULT_LEAGUE, league);
        args.put(KEY_TEAM1NAME, team1);
        args.put(KEY_TEAM2NAME, team2);
        args.put(KEY_TEAM1SCORE, score1);
        args.put(KEY_TEAM2SCORE, score2);
        return db.update(DATABASE_RESULT_TABLE, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void updateResultTeamName(String originalName, String newName)
    {
        String teamResultsQuery = "Select * from Result where team1 = '" + originalName + "' OR team2 = '" + originalName + "'";
        Cursor teamResults = rawQuery(teamResultsQuery);

        String team1, team2, league;
        int score1, score2, rowId;
        while (teamResults.moveToNext())
        {
            rowId = Integer.parseInt(teamResults.getString(teamResults.getColumnIndex("_id")));
            team1 = teamResults.getString(teamResults.getColumnIndex("team1"));
            team2 = teamResults.getString(teamResults.getColumnIndex("team2"));
            league = teamResults.getString(teamResults.getColumnIndex("league"));
            score1 = Integer.parseInt(teamResults.getString(teamResults.getColumnIndex("team1score")));
            score2 = Integer.parseInt(teamResults.getString(teamResults.getColumnIndex("team2score")));

            ContentValues args = new ContentValues();
            args.put(KEY_RESULT_LEAGUE, league);

            if(team1.equals(originalName))
            {
                args.put(KEY_TEAM1NAME, newName);
                args.put(KEY_TEAM2NAME, team2);
            }
            else
            {
                args.put(KEY_TEAM1NAME, team1);
                args.put(KEY_TEAM2NAME, newName);
            }

            args.put(KEY_TEAM1SCORE, score1);
            args.put(KEY_TEAM2SCORE, score2);

            executeUpdate("Result", args, rowId);
        }
    }

    public void updateLeagueName(String orgleague, String league)
    {
        String leagueResultsQuery = "Select * from Result where league = '" + orgleague + "'";
        Cursor leagueResults = rawQuery(leagueResultsQuery);

        String team1, team2;
        int score1, score2, rowId;
        while (leagueResults.moveToNext())
        {
            rowId = Integer.parseInt(leagueResults.getString(leagueResults.getColumnIndex("_id")));
            team1 = leagueResults.getString(leagueResults.getColumnIndex("team1"));
            team2 = leagueResults.getString(leagueResults.getColumnIndex("team2"));
            score1 = Integer.parseInt(leagueResults.getString(leagueResults.getColumnIndex("team1score")));
            score2 = Integer.parseInt(leagueResults.getString(leagueResults.getColumnIndex("team2score")));

            ContentValues args = new ContentValues();
            args.put(KEY_RESULT_LEAGUE, league);
            args.put(KEY_TEAM1NAME, team1);
            args.put(KEY_TEAM2NAME, team2);
            args.put(KEY_TEAM1SCORE, score1);
            args.put(KEY_TEAM2SCORE, score2);

            executeUpdate("Result", args, rowId);
        }

        String leagueTeamsQuery = "Select * from Team where league = '" + orgleague + "'";
        Cursor leagueTeams = rawQuery(leagueTeamsQuery);

        String teamName;
        byte[] image;
        int rowID;
        while (leagueTeams.moveToNext())
        {
            rowId = Integer.parseInt(leagueTeams.getString(leagueTeams.getColumnIndex("_id")));
            teamName = leagueTeams.getString(leagueTeams.getColumnIndex("teamName"));
            image = leagueTeams.getBlob(leagueTeams.getColumnIndex("image"));

            ContentValues args = new ContentValues();
            args.put(KEY_TEAMNAME, teamName);
            args.put(KEY_LEAGUE, league);
            args.put(KEY_TEAMIMAGE, image);

            executeUpdate("Team", args, rowId);
        }
    }

    public void deleteLeagueTeamsAndResults(String league)
    {
        String leagueResultsQuery = "Select * from Result where league = '" + league + "'";
        Cursor leagueResults = rawQuery(leagueResultsQuery);

        int rowId;
        while (leagueResults.moveToNext())
        {
            rowId = Integer.parseInt(leagueResults.getString(leagueResults.getColumnIndex("_id")));

            delete("Result", rowId);
        }

        String leagueTeamsQuery = "Select * from Team where league = '" + league + "'";
        Cursor leagueTeams = rawQuery(leagueTeamsQuery);

        while (leagueTeams.moveToNext())
        {
            rowId = Integer.parseInt(leagueTeams.getString(leagueTeams.getColumnIndex("_id")));

            delete("Team", rowId);
        }
    }

    public void deleteTeamResults(String team)
    {
        String teamResultsQuery = "Select * from Result where team1 = '" + team + "' OR team2 = '" + team + "'";
        Cursor teamResults = rawQuery(teamResultsQuery);

        int rowId;
        while (teamResults.moveToNext())
        {
            rowId = Integer.parseInt(teamResults.getString(teamResults.getColumnIndex("_id")));

            delete("Result", rowId);
        }
    }

    public boolean executeUpdate(String table, ContentValues args, int rowId)
    {
        return db.update(table, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor rawQuery (String query)
    {
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }
}

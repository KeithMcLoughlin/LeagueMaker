package com.example.keith.leaguemaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Keith on 17/11/2016.
 * */

public class DBManager
{
    public static final String KEY_ROWID = "_id";

    //league keys
    public static final String KEY_LEAGUENAME 	= "leagueName";
    public static final String KEY_SPORT 	= "sport";
    public static final String KEY_LEAGUEIMAGE 	= "image";

    private static final String DATABASE_NAME 	= "LeagueMaker";
    private static final String DATABASE_LEAGUE_TABLE = "League";
    private static final String DATABASE_TEAM_TABLE = "Team";
    private static final String DATABASE_RESULT_TABLE = "Result";
    private static final int DATABASE_VERSION 	= 1;

    private static final String DATABASE_CREATE =
            "create table League (" +
                    "leagueName text not null, " +
                    "sport text not null, "  +
                    "image text);" +
            "create table Team (" +
                    "teamName text not null, " +
                    "league text not null, "  +
                    "image text);" +
            "create table Result (_id integer primary key autoincrement, " +
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
            db.execSQL(DATABASE_CREATE);
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

    public long insertLeague(String name, String sport, String image) throws NullPointerException
    {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LEAGUENAME, name);
        initialValues.put(KEY_SPORT, sport);
        initialValues.put(KEY_LEAGUEIMAGE, image);
        return db.insert(DATABASE_LEAGUE_TABLE, null, initialValues);

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
                                KEY_SPORT,
                                KEY_LEAGUEIMAGE
                        },
                null, null, null, null, null);
    }

    public Cursor getLeague(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_LEAGUE_TABLE, new String[]
                                {
                                        KEY_ROWID,
                                        KEY_LEAGUENAME,
                                        KEY_SPORT,
                                        KEY_LEAGUEIMAGE
                                },
                        KEY_ROWID + "=" + rowId,  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateLeague(long rowId, String name, String sport, String image)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_LEAGUENAME, name);
        args.put(KEY_SPORT, sport);
        args.put(KEY_LEAGUEIMAGE, image);
        return db.update(DATABASE_LEAGUE_TABLE, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor rawQuery (String query)
    {
        Cursor cur = db.rawQuery(query, null);
        return cur;
    }
}

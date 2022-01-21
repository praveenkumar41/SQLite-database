package com.example.infox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contact.db";
    private static final String TABLE_NAME = "contact_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "NAME";
    private static final String COL3 = "CONTACTNO";
    private static final String COL4 = "EMAILID";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,CONTACTNO TEXT,EMAILID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertdata(String name,String contactno,String emailid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2,name);
        contentValues.put(COL3,contactno);
        contentValues.put(COL4,emailid);

        long res = db.insert(TABLE_NAME,null,contentValues);

        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

/*
    public Cursor getalldatas()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }
*/

    public List<Contacts> getalldatas()
    {
        List<Contacts> contacts = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME,null);

        if(cursor.getCount()>0) {

            if (cursor.moveToFirst()) {
                do {
                    Contacts c = new Contacts();
                    c.setId(cursor.getString(0));
                    c.setName(cursor.getString(1));
                    c.setContactno(cursor.getString(2));
                    c.setEmailid(cursor.getString(3));
                    contacts.add(c);
                }while (cursor.moveToNext());
            }
        }

        return contacts;
    }


    public boolean updatedata(String id,String name,String contactno,String emailid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2,name);
        contentValues.put(COL3,contactno);
        contentValues.put(COL4,emailid);

        db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{id});
        return true;
    }

    public int deletedata(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int res=db.delete(TABLE_NAME,"ID = ?",new String[]{id});
        return res;
    }

}
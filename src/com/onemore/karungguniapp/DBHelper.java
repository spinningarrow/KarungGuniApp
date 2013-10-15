package com.onemore.karungguniapp;





import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper 


{
 private SQLiteDatabase db;
 public static final String KEY_ROWID = "_id";
   
    public static final String KEY_ROLE = "role";
    public static final String KEY_USER = "username";
    public static final String KEY_EMAIL = "email";
    


 DBHelper DB = null;
 private static final String DATABASE_NAME = " ";
 private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_TABLE_NAME = " ";
    
    private static final String DATABASE_TABLE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE_NAME + "(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "role TEXT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL);";
public DBHelper(Context context) {
  
  super(context, DATABASE_NAME, null, DATABASE_VERSION);
  System.out.println("In constructor");
}




@Override
public void onCreate(SQLiteDatabase db) {
 
 try{
  
  db.execSQL(DATABASE_TABLE_CREATE);
  
  
  
 }catch(Exception e){
  e.printStackTrace();
 }
}






@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 // TODO Auto-generated method stub
 
}




public Cursor rawQuery(String string, String[] strings) {
 // TODO Auto-generated method stub
 return null;
}




public void open() {
 
 getWritableDatabase(); 
}


public Cursor getDetails(String text) throws SQLException 
{
 
    Cursor mCursor =
            db.query(true, DATABASE_TABLE_NAME, 
              new String[]{KEY_ROWID,KEY_ROLE, KEY_USER, KEY_EMAIL}, 
              KEY_USER + "=" + text, 
              null, null, null, null, null);
    if (mCursor != null) 
    {
        mCursor.moveToFirst();
    }
    return mCursor;


}


}


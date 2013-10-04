package com.onemore.karungguniapp;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class welcome extends ListActivity
{
 

 protected TextView eun;
 protected SQLiteDatabase DB;
 protected Cursor cursor;
 protected ListAdapter adapter;
 protected TextView mUname;
 protected TextView mFname;
 protected TextView mLname;
 protected TextView mEmail;
 
 
  @Override
     public void onCreate(Bundle savedInstanceState) 
     {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.welcome);
         
         
        DB = (new DBHelper(this)).getWritableDatabase();
        // searchText = (EditText) findViewById (R.id.searchText);
         
         eun = (TextView)findViewById(R.id.textV);
   Bundle bundle = getIntent().getExtras();
   
   String UName = bundle.getString("UserName");
   
   eun.setText(UName);
   
   
   
     }
  
  public void search(View view)
  {
   cursor = DB.rawQuery("SELECT _id, role, username, email FROM sri1 WHERE username LIKE ?", 
     new String[] {"%" + eun.getText().toString() + "%"});
   adapter = new SimpleCursorAdapter(
     this,
     R.layout.dtl,
     cursor,
     new String[] {"role","username", "email"},
     new int[] { R.id.srole, R.id.suname, R.id.semail});
   
  
   setListAdapter(adapter);
  }
  
  @Override
  public void onBackPressed() 
  {
   super.onBackPressed();
   Intent i = new Intent(welcome.this, LoginActivity.class);
   startActivity(i);
  }
  }

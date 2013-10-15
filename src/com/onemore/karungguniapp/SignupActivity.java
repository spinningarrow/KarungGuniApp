package com.onemore.karungguniapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.turbomanage.httpclient.ParameterMap;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends Activity implements OnClickListener, OnItemSelectedListener {
    // Variable Declaration should be in onCreate()
    private Button mSubmit;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private Spinner mRole;
    private String rol;

    protected DBHelper DB = new DBHelper(SignupActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //Assignment of UI fields to the variables
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);


        mUsername = (EditText) findViewById(R.id.reuname);
        mPassword = (EditText) findViewById(R.id.repass);
        mEmail = (EditText) findViewById(R.id.eemail);


        mRole = (Spinner) findViewById(R.id.spinner1);

        // Spinner method to read the on selected value
        ArrayAdapter<State> spinnerArrayAdapter = new ArrayAdapter<State>(this,
                android.R.layout.simple_spinner_item, new State[] {
                new State("Karung Guni"),
                new State("Seller")});

        mRole.setAdapter(spinnerArrayAdapter);
        mRole.setOnItemSelectedListener(this);
    }


    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.submit:


                String uname = mUsername.getText().toString();
                String pass = mPassword.getText().toString();
                String email = mEmail.getText().toString();
                String role = mRole.getSelectedItem().toString();

                boolean invalid = false;


                if (uname.equals("")) {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Username", Toast.LENGTH_SHORT).show();
                } else if (pass.equals("")) {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();

                } else if (email.equals("")) {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Email ID", Toast.LENGTH_SHORT).show();
                } else if (invalid == false) {

//                    addEntry(rol, uname, pass, email);
                    AccountManager.createWithEmail(email, pass, role);

                    Intent i;
                    if (role.equals("Seller")) {
//					i = new Intent(getBaseContext(), <Seller>.class);
                    } else if (role.equals("KG")) {
//					i = new Intent(getBaseContext(), <KG>.class);
                    }
//				startActivity(i);
                    // Intent i_register = new Intent(SignupActivity.this, LoginActivity.class);
                    //  startActivity(i_register);
                    //finish();
                }

                break;
        }
    }


    public void onDestroy() {
        super.onDestroy();
        //DB.close();
    }


//    private void addEntry(String rol, String uname, String pass, String email) {
//
//        SQLiteDatabase db = DB.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put("role", rol);
//        values.put("username", uname);
//        values.put("password", pass);
//        values.put("email", email);
//
//        try {
//            db.insert(DBHelper.DATABASE_TABLE_NAME, null, values);
//
//            Toast.makeText(getApplicationContext(), "your details submitted Successfully...", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Get the currently selected State object from the spinner
        State st = (State) mRole.getSelectedItem();

        // Show it via a toast
        toastState("onItemSelected", st);
    }


    public void toastState(String name, State st) {
        if (st != null) {
            rol = st.name;
            //Toast.makeText(getBaseContext(), rol, Toast.LENGTH_SHORT).show();

        }

    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}

package com.steam.app.pdaOrder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText edUserName, edDatabase, edIP, edPassword;
    Button saveBtn;
    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "MyPrefs";
    String User = "UserName";
    String IP = "IP";
    String Database = "Database";
    String Password = "Password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        edUserName = findViewById(R.id.db_username);
        edDatabase = findViewById(R.id.db_name);
        edIP = findViewById(R.id.db_ip);
        edPassword = findViewById(R.id.db_password);

        saveBtn = findViewById(R.id.btn_save);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUserName  = edUserName.getText().toString();
                String mDatabase  = edDatabase.getText().toString();
                String mIP  = edIP.getText().toString();
                String mPassword = edPassword.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(User, mUserName);
                Log.i("User: ", mUserName);
                editor.putString(Database, mDatabase);
                Log.i("Database: ", mDatabase);
                editor.putString(IP, mIP);
                Log.i("IP: ", mIP);
                editor.putString(Password, mPassword);
                Log.i("Password: ", mPassword);
                editor.commit();
                Toast.makeText(SettingsActivity.this,"Thanks",Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                SettingsActivity.this.startActivity(myIntent);
            }
        });
    }
}

package com.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    public final static String PREFERENCES_FILE = "MyData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextEmailAddress = findViewById(R.id.editTextEmailAddress);

        Button myButton = findViewById(R.id.myButton);
        myButton.setOnClickListener((click) -> {
            Intent goToHome = new Intent(LoginActivity.this, MainActivity.class);
            goToHome.putExtra("email",editTextEmailAddress.getText().toString());
            startActivity(goToHome);
        });


        SharedPreferences emailAdd = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        String s = emailAdd.getString("email","");
        editTextEmailAddress.setText(s);

    }

    @Override
    protected void onPause(){
        super.onPause();

        EditText editTextTextEmailAddress = findViewById(R.id.editTextEmailAddress);

        SharedPreferences emailAdd = getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = emailAdd.edit();
        myEditor.putString("email", editTextTextEmailAddress.getText().toString());
        myEditor.apply();
    }
}
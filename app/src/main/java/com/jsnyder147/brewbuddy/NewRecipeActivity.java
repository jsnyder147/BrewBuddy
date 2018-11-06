package com.jsnyder147.brewbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class NewRecipeActivity extends AppCompatActivity {


    private EditText etYeast;
    private EditText etHops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);


    }


}

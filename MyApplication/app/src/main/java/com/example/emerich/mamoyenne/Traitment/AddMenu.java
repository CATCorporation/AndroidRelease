package com.example.emerich.mamoyenne.Traitment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.emerich.mamoyenne.BddPack.MyBddClass;
import com.example.emerich.mamoyenne.R;

import java.util.ArrayList;

public class AddMenu extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        Button one = (Button) findViewById(R.id.menuMatiere);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) findViewById(R.id.menuNote);
        two.setOnClickListener(this);
        Button four = (Button) findViewById(R.id.retour);
        four.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.menuMatiere:
                Intent aboutIntent =
                        new Intent(this, addMatiere.class);
                startActivity(aboutIntent);
                break;

            case R.id.menuNote:
                aboutIntent =
                        new Intent(this, addNote.class);
                startActivity(aboutIntent);
                break;
            case R.id.retour:
                finish();
                break;
        }
    }


}

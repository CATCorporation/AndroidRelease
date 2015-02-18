package com.example.emerich.mamoyenne.Traitment;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.emerich.mamoyenne.R;

public class About extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView myText = (TextView) findViewById(R.id.textView);
        myText.setMovementMethod(new ScrollingMovementMethod());
    }
}

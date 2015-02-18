package com.example.emerich.mamoyenne;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.emerich.mamoyenne.BddPack.MyBddClass;
import com.example.emerich.mamoyenne.Traitment.About;
import com.example.emerich.mamoyenne.Traitment.AddMenu;
import com.example.emerich.mamoyenne.Traitment.Show;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    MyBddClass maBdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mise en place listener sur les boutons
        Button one = (Button) findViewById(R.id.add);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) findViewById(R.id.show);
        two.setOnClickListener(this);
        Button three = (Button) findViewById(R.id.about);
        three.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // bouton ajouter/supprimer cliqué
            case R.id.add:
               Intent addMatiereIntent =
                        new Intent(this, AddMenu.class);
               startActivity(addMatiereIntent);
               break;

            // bouton menu principal cliqué


            // bouton consulter cliqué
            case R.id.show:
                Intent showIntent =
                        new Intent(this, Show.class);
                startActivity(showIntent);
                break;

            // bouton a propos cliqué
            case R.id.about:
                Intent aboutIntent =
                        new Intent(this, About.class);
                startActivity(aboutIntent);
                break;


            default:
                break;
        }
    }
}

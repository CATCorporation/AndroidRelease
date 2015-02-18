package com.example.emerich.mamoyenne.Traitment;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.emerich.mamoyenne.BddPack.MyBddClass;
import com.example.emerich.mamoyenne.R;

import java.util.ArrayList;

public class Show extends ActionBarActivity {

    String Semestre = "1";
    TableLayout tl;
    MyBddClass maBdd;
    ArrayList<String> maListe = new ArrayList<String>();
    double moyenneGenerale =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // on set la bdd
        maBdd = new MyBddClass(this);
        maBdd.openDatabase();

        setHeader();

        RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.sm1:
                        Semestre = "1";
                        break;

                    case R.id.sm2:
                        Semestre = "2";
                        break;
                }
                tl.removeAllViews();
                setHeader();
            }
        });

    }

    private void setHeader()
    {

        tl = (TableLayout) findViewById(R.id.main_table);

        TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView name = new TextView(this);
        name.setId(20);
        name.setTextSize(14);
        name.setText("Matières");
        name.setTextColor(Color.WHITE);
        name.setPadding(5, 5, 5, 5);
        tr_head.addView(name);

        TextView coef = new TextView(this);
        coef.setId(21);
        coef.setTextSize(14);
        coef.setText("Coef.");
        coef.setTextColor(Color.WHITE);
        coef.setPadding(5, 5, 5, 5);
        tr_head.addView(coef);

        TextView moyenne = new TextView(this);
        moyenne.setId(22);
        moyenne.setText("Moyenne");
        moyenne.setTextColor(Color.WHITE);
        moyenne.setPadding(5, 5, 5, 5);
        tr_head.addView(moyenne);
        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        setTable();
    }

    private void setTable()
    {
        maListe = maBdd.selectMoyenne(Semestre);

        if(maListe.size()>0)
        {
            int i;

            for(i = 0; i < maListe.size(); i++)
            {
                TableRow tr = new TableRow(this);
                if(i%2!=0) tr.setBackgroundColor(Color.GRAY);
                tr.setId(100+i);
                tr.setLayoutParams(new  TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));


                TextView name = new TextView(this);
                name.setId(200+i);
                name.setTextSize(14);
                name.setText(maListe.get(i).split("/")[0]);
                name.setPadding(2, 0, 5, 0);
                name.setTextColor(Color.WHITE);
                tr.addView(name);

                TextView coef = new TextView(this);
                coef.setId(200+i);
                coef.setTextSize(14);
                coef.setText(maListe.get(i).split("/")[1]);
                coef.setPadding(2, 0, 5, 0);
                coef.setTextColor(Color.WHITE);
                tr.addView(coef);

                TextView moyenne = new TextView(this);
                moyenne.setId(200+i);
                moyenne.setTextSize(14);
                moyenne.setText(maListe.get(i).split("/")[2]);
                moyenne.setTextColor(Color.WHITE);
                tr.addView(moyenne);

                try
                {

                }catch (NumberFormatException e) {}

                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

            }

            TableRow tr = new TableRow(this);
            if(i%2!=0) tr.setBackgroundColor(Color.GRAY);
            tr.setId(100+i);
            tr.setLayoutParams(new  TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            TextView name = new TextView(this);
            name.setId(200+i);
            name.setTextSize(14);
            name.setText(" ");
            name.setPadding(2, 0, 5, 0);
            name.setTextColor(Color.WHITE);
            tr.addView(name);
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            i++;

            tr = new TableRow(this);
            if(i%2!=0) tr.setBackgroundColor(Color.GRAY);
            tr.setId(100+i);
            tr.setLayoutParams(new  TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            name = new TextView(this);
            name.setId(200+i);
            name.setTextSize(14);
            name.setText("Générale");
            name.setPadding(2, 0, 5, 0);
            name.setTextColor(Color.WHITE);
            tr.addView(name);

            TextView coef = new TextView(this);
            coef.setId(200+i);
            coef.setTextSize(14);
            coef.setText(maBdd.getMoyGenerale(Semestre));
            coef.setPadding(2, 0, 5, 0);
            coef.setTextColor(Color.WHITE);
            tr.addView(coef);

            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }
    }
}

package com.android.nanodegree.udacity.myappportifolio.view.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.nanodegree.udacity.myappportifolio.R;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       seetupAcionBar();
    }

    /**
     *
     */
    private void seetupAcionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            // Apresenta o botão de voltar no menu
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Carrega as ações que aparecerão no action bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    /**
     * Trata as ações de cada menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()){
        case R.id.action_settings:
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        default:  return super.onOptionsItemSelected(item);
    }
}

}

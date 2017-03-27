package com.android.nanodegree.udacity.myappportifolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void launchPopularMoviesApp(View view){
        // Inicia  o Fragment
        startActivity(new Intent(this, MenuActivity.class));
    }

    public void launchStockHawkApp(View view){
        sendToastMessage("This button will launch my Stock Hanwk App!");
    }


    public void launchBuildItBiggerApp(View view){
        sendToastMessage("This button will launch my Build It Bigger App!");
    }

    public void launchMakeYourApp(View view){
        sendToastMessage("This button will launch my Make Your App Material!");
    }

    public void launchGoUbiquitousApp(View view){
        sendToastMessage("This button will launch my Go Ubiquitous!");
    }

    public void launchCapstoneApp(View view){
        sendToastMessage("This button will launch my Capstone App!");
    }

    /**
     * Send Message from Toast
     * @param text
     */
    private void sendToastMessage(CharSequence text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case:R.id.ax
        }

        return super.onOptionsItemSelected(item);
    }*/
}

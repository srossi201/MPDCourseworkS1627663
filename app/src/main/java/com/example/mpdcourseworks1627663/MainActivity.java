package com.example.mpdcourseworks1627663;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> RSSTrafficLinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Trafficbtn1 = findViewById(R.id.Trafficbtn1);
        Trafficbtn1.setOnClickListener(this);

        Button Trafficbtn2 = findViewById(R.id.Trafficbtn2);
        Trafficbtn2.setOnClickListener(this);

        Button Trafficbtn3 = findViewById(R.id.Trafficbtn3);
        Trafficbtn3.setOnClickListener(this);

        Button exitbutton = findViewById(R.id.exitbutton);
        exitbutton.setOnClickListener(this);



        RSSTrafficLinks.add("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        RSSTrafficLinks.add("https://trafficscotland.org/rss/feeds/roadworks.aspx");
        RSSTrafficLinks.add("https://trafficscotland.org/rss/feeds/currentincidents.aspx");

    }

    @Override
    public void onClick(View view){
        Intent mpd = new Intent(getApplicationContext(), RSSFeedActivity.class);
        switch(view.getId()){
            case R.id.Trafficbtn1:
                mpd.putExtra("rssLink", RSSTrafficLinks.get(0));
                startActivity(mpd);
                break;
            case R.id.Trafficbtn2:
                mpd.putExtra("rssLink", RSSTrafficLinks.get(1));
                startActivity(mpd);
                break;
            case R.id.Trafficbtn3:
                mpd.putExtra("rssLink", RSSTrafficLinks.get(2));
                startActivity(mpd);
                break;
            case R.id.exitbutton:
                showtbDialog();
                break;
        }

    }

    private void showtbDialog(){
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage("Do you want to exit the MPDTraffic App?");
        bld.setCancelable(false);
        bld.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Thank you for using the MPDTraffic App", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        });
        bld.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "You Pressed No", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog alert = bld.create();
        alert.show();
    }
}
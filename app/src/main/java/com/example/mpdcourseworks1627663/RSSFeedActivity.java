package com.example.mpdcourseworks1627663;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RSSFeedActivity extends ListActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener {

    private ProgressBar pbarDialog;
    ArrayList<HashMap<String,String>> rssItemList = new ArrayList<>();
    RSSParser rssParser = new RSSParser();
    List<RSSTrafficItems> rssitemList;
    Date selectedDate;
    private CustomAdapter lstadapter;
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_PUBDATE = "pubDate";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_GEORSS = "georss:point";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssfeed);

        //Create a main menu button.
        Button MainMenuBtn = findViewById(R.id.MainMenubtn);
        MainMenuBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent mmenu = new Intent(RSSFeedActivity.this, MainActivity.class);
                mmenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mmenu);
            }

        });

        String rss_link = getIntent().getStringExtra("rssLink");
        new LoadRSSFeedItems().execute(rss_link);
        ListView lstvw = getListView();
        lstvw.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        Intent mpd = new Intent(getApplicationContext(), MapsActivity.class);
        mpd.putExtra("rssTrafficItems", lstadapter.getItem((position)));
        startActivity(mpd);
    }


    public class LoadRSSFeedItems extends AsyncTask <String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbarDialog = new ProgressBar(RSSFeedActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            pbarDialog.setLayoutParams(layoutParams);
            pbarDialog.setVisibility(View.VISIBLE);
            relativeLayout.addView(pbarDialog);
        }

        @Override
        protected String doInBackground(String... args){
            String rss_url = args[0];
            rssitemList = rssParser.getRSSFeedItems(rss_url);
            for (final RSSTrafficItems item : rssitemList ) {
                if (item.link.toString().equals(""))
                    break;
                HashMap<String, String> map = new HashMap<String, String>();

                String givenDate = item.pubDate.trim();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                try{
                    Date mDate = sdf.parse(givenDate);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy - hh:mm a", Locale.UK);
                    item.pubDate = sdf2.format(mDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                map.put(TAG_TITLE, item.title);
                map.put(TAG_LINK, item.link);
                map.put(TAG_PUBDATE, item.pubDate);
                map.put(TAG_DESCRIPTION, item.description);
                map.put(TAG_GEORSS, item.georss);
                rssItemList.add(map);
            }
            runOnUiThread(new Runnable() {
                public void run(){

                    lstadapter = new CustomAdapter(rssitemList,getApplicationContext());
                    setListAdapter(lstadapter);

                    //Search bar
                    EditText ItemSearch = (EditText)findViewById(R.id.ItemSearch);
                    ItemSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println(s);
                            lstadapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            });
            return null;
        }

        protected void onPostExecute(String args){
            pbarDialog.setVisibility(View.GONE);
        }

    }

    public void showDatePicker(View v){
        DatePickerDialog dataPickerDialog =  createDataPickerDialog( v);
        dataPickerDialog.show();
    }

    public DatePickerDialog createDataPickerDialog(View v) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(v.getContext() , this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        this.lstadapter.setSelectedDate(calendar.getTime());
        TextView textView =  (TextView) findViewById(R.id.txtSelectedDate);
        textView.setText(calendar.getTime().toString());

        this.lstadapter.getFilter().filter("");

    }

}
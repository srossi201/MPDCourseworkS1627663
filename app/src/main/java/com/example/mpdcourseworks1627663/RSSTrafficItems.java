package com.example.mpdcourseworks1627663;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RSSTrafficItems implements Serializable {

    public String title;
    public String description;
    public String link;
    public String georss;
    public String pubDate;
    private Date startDate;
    private Date endDate;


    public RSSTrafficItems() {
        this.title = "UNDEFINED";
    }

    public RSSTrafficItems(String title, String description, String link, String georss, String pubDate){
        super();
        this.title = title;
        this.description = description;
        this.link = link;
        this.georss = georss;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        parseDates(description);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public Double[] getLatLng() {
        String[] latlngsplit = this.georss.split(" ", -1);
        Double[] coordinates = new Double[2];
        coordinates[0] = Double.parseDouble(latlngsplit[0]);
        coordinates[1] = Double.parseDouble(latlngsplit[1]);
        return coordinates;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    public Date stringToDate(String dateString) {
        DateFormat format = new SimpleDateFormat("EE, dd MMMMM y hh:mm:ss z", Locale.UK);
        DateFormat format2 = new SimpleDateFormat("EEEE, dd MMMMM y - kk:mm", Locale.UK);
        List<DateFormat> formatStrings = new ArrayList<>();
        formatStrings.add(format);
        formatStrings.add(format2);

        for (DateFormat formatString : formatStrings) {
            try {
                return formatString.parse(dateString.trim());
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public void parseDates(String description){
        String[] dateSplit = description.split("<br />", -1);
        if(dateSplit.length >= 2) {
            String sDate = dateSplit[0].split("Start Date: ")[1];
            String eDate = dateSplit[1].split("End Date: ")[1];
            this.startDate = stringToDate(sDate);
            this.endDate = stringToDate(eDate);
        }


        if(dateSplit.length>=3){
            this.description = dateSplit[2];
        }


    }


    @Override
    public String toString() {
        return "RSSTrafficItems{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", georss='" + georss + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

package com.nabak.nyt_topstories_example.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nabak on 2017. 1. 27..
 */

public class Article implements Serializable {
    String url;
    String title;
    String thumbnail;

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Article(String url, String title, String thumbnail){
        this.url = url;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public Article(JSONObject jsonObject){
        try{
            this.url = jsonObject.getString("url");
            this.title = jsonObject.getString("title");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length() > 2) {
                this.thumbnail = multimedia.getJSONObject(3).getString("url");
            }else {
                this.thumbnail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Article> results = new ArrayList<Article>();

        for(int i = 0; i < jsonArray.length(); i++){
            results.add(new Article(jsonArray.getJSONObject(i)));
        }
        return  results;
    }
}

package com.nabak.nyt_topstories_example.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nabak.nyt_topstories_example.EndlessRecyclerViewScrollListener;
import com.nabak.nyt_topstories_example.SpacesItemDecoration;
import com.nabak.nyt_topstories_example.adapters.ArticleAdapter;
import com.nabak.nyt_topstories_example.model.Article;
import com.nabak.nyt_topstories_example.volleyUtil.CustomRequest;
import com.nabak.nyt_topstories_example.volleyUtil.MyVolley;
import com.nabak.nyt_topstories_example.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nabak on 2017. 1. 27..
 */

public class MainActivity extends Activity {

    public static final String NYT_TOPNEWS_URL = "INPUT NYT URL AND YOUR API-KEY ";

    RecyclerView recyclerView;
    ArrayList<Article> articles;
    ArticleAdapter adapter;
    ProgressDialog progressDialog;

    String checkArticleTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        articles = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        adapter = new ArticleAdapter(articles);
        recyclerView.setAdapter(adapter);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);

        final RequestQueue queue = MyVolley.getInstane(getApplicationContext()).getRequestQueue();

        CustomRequest nReq = new CustomRequest(
                NYT_TOPNEWS_URL
                , networkSucceesListener()
                , networkErrorListener());
        queue.add(nReq);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        recyclerView.setOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                CustomRequest nReq = new CustomRequest(
                        NYT_TOPNEWS_URL
                        , loadMoreArticles()
                        , networkErrorListener());
                queue.add(nReq);
            }
        });
    }

    private Response.Listener<JSONObject> networkSucceesListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    checkArticleTime = response.getString("last_updated");
                    adapter.enroll(Article.fromJsonArray(response.getJSONArray("results")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.Listener<JSONObject> loadMoreArticles() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getString("last_updated").equals(checkArticleTime)) {
                        //Log.d("nabak", response.getString("last_updated"));
                        articles.addAll(Article.fromJsonArray(response.getJSONArray("results")));
                        int curSize = adapter.getItemCount();
                        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener networkErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        };
    }
}

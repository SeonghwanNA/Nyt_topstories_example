package com.nabak.nyt_topstories_example.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nabak.nyt_topstories_example.R;
import com.nabak.nyt_topstories_example.activities.ArticleActivity;
import com.nabak.nyt_topstories_example.model.Article;
import com.nabak.nyt_topstories_example.volleyUtil.MyVolley;

import java.util.ArrayList;

/**
 * Created by nabak on 2017. 1. 27..
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cardLayout;
        NetworkImageView thumbnail;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            cardLayout = (RelativeLayout) itemView.findViewById(R.id.cardLayout);
            thumbnail = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private ArrayList<Article> mArticles;
    private Context context;

    public ArticleAdapter(ArrayList<Article> articles){
        mArticles = articles;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.cardview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, final int position) {

        final Article article = mArticles.get(position);
        ImageLoader imageLoader = MyVolley.getInstane(context).getImageLoader();
        String thumbnail = article.getThumbnail();

        holder.thumbnail.setImageUrl(thumbnail,imageLoader);
        holder.title.setText(article.getTitle());

        if (!TextUtils.isEmpty(thumbnail)) {
            holder.thumbnail.setVisibility(View.VISIBLE);
            imageLoader.get(thumbnail, ImageLoader.getImageListener(holder.thumbnail, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        } else {
            holder.thumbnail.setVisibility(View.GONE);
        }

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ArticleActivity.class);
                i.putExtra("article", article);
                context.startActivity(i);
            }
        });
    }

    public void enroll(ArrayList<Article> articles){
        mArticles.clear();
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }



}

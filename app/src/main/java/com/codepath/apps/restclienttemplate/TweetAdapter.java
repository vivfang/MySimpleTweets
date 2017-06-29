package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vf608 on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    List<Tweet> mTweets;
    Context context;
    private TwitterClient client;
    public TweetAdapter(List<Tweet>tweets){
        mTweets = tweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        client = TwitterApp.getRestClient();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Tweet tweet = mTweets.get(position);
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvRelativeTime.setText(getRelativeTimeAgo(tweet.createdAt));
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        holder.favorite.setSelected(tweet.favorited);
        holder.retweet.setSelected(tweet.retweeted);
        holder.reply.setTag(tweet.user.screenName);
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ComposeActivity.class);
                i.putExtra("tweetUser", holder.reply.getTag().toString());
                ((Activity)context).startActivityForResult(i, 1);
            }
        });
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.favoriteTweet(tweet.uid, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        holder.favorite.setSelected(true);
                        Log.d("TwitterClient", response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TwitterClient", responseString);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.d("TwitterClient", errorResponse.toString());
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("TwitterClient", errorResponse.toString());
                        throwable.printStackTrace();
                    }
                });
            }
        });
        holder.retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.retweet(tweet.uid, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        holder.retweet.setSelected(true);
                        Log.d("TwitterClient", response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TwitterClient", responseString);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.d("TwitterClient", errorResponse.toString());
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("TwitterClient", errorResponse.toString());
                        throwable.printStackTrace();
                    }
                });
            }
        });
    }
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
            relativeDate = relativeDate.replace("ago", "");
            relativeDate = relativeDate.replace(" sec.", "s");
            relativeDate = relativeDate.replace(" min.", "m");
            relativeDate = relativeDate.replace(" hr.", "h");
            relativeDate = relativeDate.replace(" days", "d");
            relativeDate = relativeDate.replace("Yesterday", "1d");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvRelativeTime;
        public ImageView reply;
        public ImageView favorite;
        public ImageView retweet;

        public ViewHolder(View itemView){
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime);
            reply = (ImageView) itemView.findViewById(R.id.reply);
            favorite = (ImageView) itemView.findViewById(R.id.favorite);
            retweet = (ImageView) itemView.findViewById(R.id.retweet);
            itemView.setOnClickListener(this);
        }
        public void onClick(View v){
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Tweet tweet = mTweets.get(position);
                Intent intent = new Intent(context, DetailTweetActivity.class);
                intent.putExtra("tweet", tweet);
                context.startActivity(intent);
            }
        }
    }
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

}

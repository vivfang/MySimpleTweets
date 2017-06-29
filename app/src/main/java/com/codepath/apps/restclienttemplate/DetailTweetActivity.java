package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class DetailTweetActivity extends AppCompatActivity {
    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);
        client = TwitterApp.getRestClient();
        final Context context = getContext();
        final Tweet tweet = getIntent().getParcelableExtra("tweet");
        TextView name = (TextView) findViewById(R.id.detailName);
        TextView handle = (TextView) findViewById(R.id.detailHandle);
        TextView body = (TextView) findViewById(R.id.detailBody);
        TextView time = (TextView) findViewById(R.id.detailTime);
        ImageView profpic = (ImageView) findViewById(R.id.detailProfpic);
        name.setText(tweet.user.name);
        handle.setText("@"+tweet.user.screenName);
        body.setText(tweet.body);
        Glide.with(context).load(tweet.user.profileImageUrl).into(profpic);
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(tweet.createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time.setText(relativeDate);

        final ImageView rtBttn = (ImageView) findViewById(R.id.retweetButton);
        final ImageView reBttn = (ImageView) findViewById(R.id.replyBttn);
        reBttn.setTag(tweet.user.screenName);
        reBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ComposeActivity.class);
                i.putExtra("tweetUser", reBttn.getTag().toString());
                startActivityForResult(i, 1);
            }
        });
        final ImageView favBttn = (ImageView) findViewById(R.id.favoriteButton);
        rtBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.retweet(tweet.uid, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        rtBttn.setSelected(true);
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
        favBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.favoriteTweet(tweet.uid, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        favBttn.setSelected(true);
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
}

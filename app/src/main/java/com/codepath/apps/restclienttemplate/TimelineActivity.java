package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.menu.main_menu;

public class TimelineActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        client = TwitterApp.getRestClient();

        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);
        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(main_menu, menu);
        return true;
    }
    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        i.putExtra("tweetUser", "");
        startActivityForResult(i, 1); // brings up the second activity
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check request code and result code first
        // Use data parameter
        Tweet tweet = data.getParcelableExtra("tweet");
        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }
    private void populateTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
               // Log.d("TwitterClient", response.toString());
                for(int i = 0; i < response.length(); i++){
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                tweetAdapter.clear();
                // ...the data has come back, add new items to your adapter...
                populateTimeline();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }
}

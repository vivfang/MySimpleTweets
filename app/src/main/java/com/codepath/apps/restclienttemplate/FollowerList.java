package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowerList extends AppCompatActivity {
    private TwitterClient client;
    FollowAdapter FollowAdapter;
    ArrayList<User> followers;
    RecyclerView rvUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);
        client = TwitterApp.getRestClient();

        rvUsers = (RecyclerView) findViewById(R.id.rvUser);
        followers = new ArrayList<>();
        FollowAdapter = new FollowAdapter(followers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(FollowAdapter);
        User user = getIntent().getParcelableExtra("tweetUser");
        populateList(user);
    }
    private void populateList(User user){
        client.getFollowers(user.uid, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                JSONArray arr = null;
                try {
                    arr = response.getJSONArray("users");
                    for(int i = 0; i < arr.length(); i++){
                        try {
                            User follower = User.fromJson(arr.getJSONObject(i));
                            Log.i("follower", follower.name);
                            followers.add(follower);
                            FollowAdapter.notifyItemInserted(followers.size()-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Log.d("TwitterClient", response.toString());
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
}

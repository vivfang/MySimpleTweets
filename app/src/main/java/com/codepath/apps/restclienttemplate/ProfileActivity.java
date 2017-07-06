package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screen_name");
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        client = TwitterApp.getRestClient();
        client.getUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    user = User.fromJson(response);
                    getSupportActionBar().setTitle("@"+user.screenName);
                    populateUserTimeline(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void populateUserTimeline(User user){
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollower);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);
        Glide.with(this)
                .load(user.profileImageUrl)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivProfileImage);
        tvFollowers.setText("" + user.followers + " Followers");
        tvFollowing.setText("" + user.following + " Following");
        tvTagline.setText(user.tagLine);
    }

    public void openFollower(View v){
        Intent i = new Intent(ProfileActivity.this, FollowerList.class);
        i.putExtra("tweetUser", user);
        startActivity(i);
    }

    public void openFollowing(View v){
        Intent i = new Intent(ProfileActivity.this, FollowingList.class);
        i.putExtra("tweetUser", user);
        startActivity(i);
    }
}

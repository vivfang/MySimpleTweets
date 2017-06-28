package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.id.charCount;
import static com.codepath.apps.restclienttemplate.R.id.etBody;
import static com.codepath.apps.restclienttemplate.models.Tweet.fromJSON;

public class ComposeActivity extends AppCompatActivity {
    private TwitterClient client;
    private Tweet tweet;
    private TextView mTextView;
    private EditText mEditText;
    private TextView username;
    private TextView handle;
    private ImageView profpic;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        String replyTo = getIntent().getExtras().getString("tweetUser");
        client = TwitterApp.getRestClient();
        mEditText = (EditText) findViewById(etBody);
        if(!replyTo.equals("")) {
            mEditText.setText("@" + replyTo + " ");
            mEditText.setSelection(replyTo.length() + 2);
        }
        mTextView = (TextView) findViewById(charCount);
        mEditText.addTextChangedListener(mTextEditorWatcher);
        username = (TextView) findViewById(R.id.tvUserName);
        handle = (TextView) findViewById(R.id.tvHandle);
        profpic = (ImageView) findViewById(R.id.ivProfpic);
        client.getUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    user = User.fromJson(response);
                    Log.i("ComposeActivity", user.name);
                    username.setText(user.name);
                    handle.setText("@"+user.screenName);
                    Glide.with(profpic.getContext())
                            .load(user.profileImageUrl)
                            .into(profpic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
    public void closeCompose(View v){
        setResult(RESULT_CANCELED);
        finish();
    }
    public void onSubmit(View v) {
        client.sendTweet(mEditText.getText().toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tweet = fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i =  new Intent(ComposeActivity.this, TimelineActivity.class);
                i.putExtra("tweet", tweet);
                setResult(RESULT_OK, i);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTextView.setText(String.valueOf(140-s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}

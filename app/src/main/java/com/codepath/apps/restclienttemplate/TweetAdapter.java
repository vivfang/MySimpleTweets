package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Created by vf608 on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    private List<Tweet> mTweets;
    Context context;
    public TweetAdapter(List<Tweet>tweets){
        mTweets = tweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvRelativeTime.setText(getRelativeTimeAgo(tweet.createdAt));
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvRelativeTime;

        public ViewHolder(View itemView){
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime);
        }
    }
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

}

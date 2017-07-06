package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by vf608 on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    private HomeTimelineFragment homeTimelineFragment;
    private MentionsTimelineFragment mentionsTimelineFragment;
    private int currentPage;

    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public TweetsListFragment getItem(int position) {
        if(position == 0) {
            if(homeTimelineFragment == null)
                homeTimelineFragment = new HomeTimelineFragment();
            currentPage = 0;
            return homeTimelineFragment;
        }
        else if (position == 1) {
            if(mentionsTimelineFragment == null)
                mentionsTimelineFragment = new MentionsTimelineFragment();
            currentPage = 1;
            return mentionsTimelineFragment;
        }
        else
            return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public int getCurrentPage(){ return currentPage; }
}

package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by vf608 on 6/26/17.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder>{
    List<User> mFollow;
    Context context;

    public FollowAdapter(List<User>users){
        mFollow = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View userView = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(userView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User user = mFollow.get(position);
        holder.tvUsername.setText(user.name);
        holder.tvScreename.setText("@"+user.screenName);
        Glide.with(context).load(user.profileImageUrl).bitmapTransform(new CropCircleTransformation(context)).into(holder.ivProfileImage);
    }
    @Override
    public int getItemCount() {
        return mFollow.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvScreename;

        public ViewHolder(View itemView){
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvScreename = (TextView) itemView.findViewById(R.id.tvTagline);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, OtherProfileActivity.class);
                    User user = mFollow.get(position);
                    i.putExtra("user", user);
                    context.startActivity(i);
                }
            });
        }
    }

}

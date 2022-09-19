package com.liaudev.dicodingbpaa.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.liaudev.dicodingbpaa.Constants;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;
import com.liaudev.dicodingbpaa.databinding.RowItemStoryBinding;
import com.liaudev.dicodingbpaa.ui.detail.DetailActivity;

import java.util.Locale;

/**
 * Created by Budiliauw87 on 2022-06-04.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class AdapterStory extends PagingDataAdapter<StoryEntity, AdapterStory.StoryViewHolder> {

    public static final int LOADING_ITEM = 0;
    public static final int STORY_ITEM = 1;
    public AdapterStory() {
        super(new UiModelComparator());
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryViewHolder(RowItemStoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        StoryEntity storyEntity = getItem(position);
        // Check for null
        holder.bind(storyEntity);

    }
    @Override
    public int getItemViewType(int position) {
        // set ViewType
        return position == getItemCount() ? STORY_ITEM : LOADING_ITEM;
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        // Define movie_item layout view binding
        RowItemStoryBinding itemStoryBinding;

        public StoryViewHolder(@NonNull RowItemStoryBinding binding) {
            super(binding.getRoot());
            // init binding
            this.itemStoryBinding = binding;
        }

        public void bind(StoryEntity storyEntity) {
            if (storyEntity != null) {
                itemView.setOnClickListener((v)->{
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra(Constants.EXTRA_STORY,storyEntity);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair[] pairs = new Pair[6];
                        pairs[0] = new Pair<View, String>(itemStoryBinding.tvRoundName, "logo_name");
                        pairs[1] = new Pair<View, String>(itemStoryBinding.tvUsername, "username");
                        pairs[2] = new Pair<View, String>(itemStoryBinding.tvCreatedAt, "createdAt");
                        pairs[3] = new Pair<View, String>(itemStoryBinding.imgLocation, "imgLocation");
                        pairs[4] = new Pair<View, String>(itemStoryBinding.tvDescreption, "descreption");
                        pairs[5] = new Pair<View, String>(itemStoryBinding.imgPhoto, "photourl");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                                (Activity) itemView.getContext(),pairs);
                        itemView.getContext().startActivity(intent, options.toBundle());
                    }else{
                        itemView.getContext().startActivity(intent);
                    }

                });
                // Set rating of movie
                final String name = storyEntity.getName();
                final String firstCharName = Character.toString(name.charAt(0)).toUpperCase(Locale.ROOT);
                itemStoryBinding.tvRoundName.setText(firstCharName);
                itemStoryBinding.tvUsername.setText(name);
                itemStoryBinding.tvCreatedAt.setText(storyEntity.getCreatedAt());
                itemStoryBinding.tvDescreption.setText(storyEntity.getDescription());

                RequestOptions options = new RequestOptions();
                RequestBuilder<Drawable> requestBuilder= Glide.with(itemView.getContext())
                        .asDrawable().sizeMultiplier(0.25f);
                options.placeholder(R.drawable.ic_launcher_foreground);
                options.centerCrop();
                Glide.with(itemView.getContext())
                        .load(storyEntity.getPhotoUrl()) // Load the image
                        .thumbnail(requestBuilder)
                        .apply(options)
                        .into(itemStoryBinding.imgPhoto);
            }

        }
    }
}

class UiModelComparator extends DiffUtil.ItemCallback<StoryEntity> {
    @Override
    public boolean areItemsTheSame(@NonNull StoryEntity oldItem, @NonNull StoryEntity newItem) {
        return  oldItem.getId().equals(newItem.getId());
    }

    @SuppressLint("DiffUtilEquals")
    @Override
    public boolean areContentsTheSame(@NonNull StoryEntity oldItem, @NonNull StoryEntity newItem) {
        return oldItem.equals(newItem);
    }
}

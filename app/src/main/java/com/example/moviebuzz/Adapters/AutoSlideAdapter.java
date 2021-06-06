package com.example.moviebuzz.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.moviebuzz.Model.MovieModel;
import com.example.moviebuzz.Model.SlideItem;
import com.example.moviebuzz.Model.SlideItemPosition;
import com.example.moviebuzz.MovieHomePage;
import com.example.moviebuzz.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AutoSlideAdapter extends SliderViewAdapter<AutoSlideAdapter.Holder> {
    List<SlideItem> images;
    List<MovieModel> movieList;
    Context context;
    OnSlideClickListener onSlideClickListener;

    public AutoSlideAdapter(List<SlideItem> images, Context context, List<MovieModel> movieList,
                            OnSlideClickListener onSlideClickListener) {

        this.images = images;
        this.context = context;
        this.movieList = movieList;
        this.onSlideClickListener = onSlideClickListener;
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;
        OnSlideClickListener onSlideClickListener;

        public Holder(View itemView, OnSlideClickListener onSlideClickListener) {
            super(itemView);
            this.onSlideClickListener = onSlideClickListener;
            imageView = itemView.findViewById(R.id.imageViewForSlider);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_image_container, parent, false);

        return new Holder(view, onSlideClickListener);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        Glide.with(context.getApplicationContext()).load(images.get(position).getImage()).into(viewHolder.imageView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSlideClickListener.onSlideClick(position);
            }
        });
    }

    @Override
    public int getCount() {
        return images.size();
    }

    public interface OnSlideClickListener {
        void onSlideClick(int position);
    }
}
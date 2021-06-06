package com.example.moviebuzz.Adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuzz.Model.CategoryModel;
import com.example.moviebuzz.Model.MovieModel;
import com.example.moviebuzz.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>  {

    onClickListener clickListener;
    Context context;
    List<CategoryModel> movieList;

    public CategoryAdapter(Context context, List<CategoryModel> movieList, onClickListener clickListener) {
        this.context = context;
        this.movieList = movieList;
        this.clickListener = clickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView categoryPosterView;
        TextView categoryTitleView,categoryRatingView,categoryDurationView;
        onClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, onClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            categoryPosterView = itemView.findViewById(R.id.categoryPoster);
            categoryTitleView = itemView.findViewById(R.id.caterogyMovieName);
            categoryRatingView = itemView.findViewById(R.id.categoryMovieIMDB);
            categoryDurationView = itemView.findViewById(R.id.categoryMovieDuration);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_items, parent, false);
        return new MyViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        String a = "<font color='#F44336'>Duration: </font>";
        String b = "<font color='#F44336'>IMDB: </font>";
        holder.categoryTitleView.setText(movieList.get(position).getTitle());
        holder.categoryRatingView.setText(Html.fromHtml(b + " "+ movieList.get(position).getImdbRatings()));
        holder.categoryDurationView.setText(Html.fromHtml(a + " " + movieList.get(position).getDuration()));
        Glide.with(holder.categoryPosterView.getContext()).load(movieList.get(position).getPoster_path()).into(holder.categoryPosterView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface onClickListener {
        void onClick(int position);
    }

}

package com.example.moviebuzz.Adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.ImageDecoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuzz.Model.MovieModel;
import com.example.moviebuzz.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    onClickListener clickListener;
    onClickListen clickListen;
    Context context;
    List<MovieModel> movieList;
    private static final String TAG = "MovieAdapter";
    public MovieAdapter(Context context, List<MovieModel> movieList, onClickListener clickListener,onClickListen clickListen) {
        this.context = context;
        this.movieList = movieList;
        this.clickListener = clickListener;
        this.clickListen = clickListen;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        onClickListener clickListener;
        onClickListen clickListen;

        public MyViewHolder(@NonNull View itemView, onClickListener clickListener,onClickListen clickListen) {
            super(itemView);
            this.clickListener = clickListener;
            this.clickListen = clickListen;
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
            clickListen.onClick2(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items, parent, false);

        return new MyViewHolder(itemView, clickListener,clickListen);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(movieList.get(position).getTitle());
        Glide.with(holder.imageView.getContext()).load(movieList.get(position).getPoster_path()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface onClickListener {
        void onClick(int position);
    }
    public interface onClickListen {
        void onClick2(int position);
    }

}

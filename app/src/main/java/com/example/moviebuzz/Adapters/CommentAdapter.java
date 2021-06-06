package com.example.moviebuzz.Adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuzz.Model.CategoryModel;
import com.example.moviebuzz.Model.CommentModel;
import com.example.moviebuzz.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>  {

    onClickListener clickListener;
    Context context;
    List<CommentModel> commentList;
    private static final String TAG = "CommentAdapter";

    public CommentAdapter(Context context, List<CommentModel> commentList) {
        this.context = context;
        this.commentList = commentList;
        this.clickListener = clickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userImage;
        TextView userName,userComment;
        onClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, onClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            userName = itemView.findViewById(R.id.userName);
            userComment = itemView.findViewById(R.id.userComment);
            userImage = itemView.findViewById(R.id.userImage);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new MyViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {

        holder.userName.setText(commentList.get(position).getUserName());
        holder.userComment.setText(Html.fromHtml(commentList.get(position).getContent()));
        Glide.with(holder.userImage.getContext()).load(commentList.get(position).getUserImage()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public interface onClickListener {
        void onClick(int position);
    }

}

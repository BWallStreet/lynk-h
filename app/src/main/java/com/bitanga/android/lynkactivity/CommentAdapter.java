package com.bitanga.android.lynkactivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.Query;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CommentAdapter extends FirestoreAdapter<CommentAdapter.CommentHolder> {

    public CommentAdapter(Query query) {
        super(query);
    }

    @Override
    public CommentHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.bind(getSnapshot(position).toObject(com.bitanga.android.lynkactivity.Comment.class));
    }

    static class CommentHolder extends RecyclerView.ViewHolder {

        private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
                "MM/dd/yyyy", Locale.US);

        private TextView nameView;
        private TextView textView;
        private TextView dateView;

        public CommentHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.user);
            textView = itemView.findViewById(R.id.commentContent);
            dateView = itemView.findViewById(R.id.commentDate);
        }

        public void bind(com.bitanga.android.lynkactivity.Comment comment) {
            nameView.setText(comment.getUserName());
            textView.setText(comment.getContent());

            if (comment.getTimestamp() != null) {
                dateView.setText(FORMAT.format(comment.getTimestamp()));
            }
        }


    }
}

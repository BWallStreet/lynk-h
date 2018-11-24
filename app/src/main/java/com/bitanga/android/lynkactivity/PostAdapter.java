package com.bitanga.android.lynkactivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;



import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PostAdapter extends FirestoreAdapter<PostAdapter.PostHolder>{



    public interface OnPostSelectedListener {

        void onPostSelected(DocumentSnapshot post);

    }

    private OnPostSelectedListener mListener;
    private FirebaseAuth mAuth;

    public PostAdapter(Query query, OnPostSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mAuth = FirebaseAuth.getInstance();

        return new PostHolder(inflater.inflate(R.layout.list_item_post, parent, false));

    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }



    static class PostHolder extends RecyclerView.ViewHolder {

        private Post mPost;
        private User mUser;
        private TextView mContentTextView;
        private TextView likes;
        private ImageView mPhotoImageView;
        private ImageButton mLike;
        private ImageButton mComment;
        private ImageButton mFlag;
        private TextView mTimePostedTextView;
        private TextView mUsernameTextView;
        private int numOfLikes;
        private int numOfTimesFlagged;
        private FirebaseFirestore db;
        private DocumentReference mUserRef;

        public PostHolder (View itemView) {
            super(itemView);
            //what is butterknife?
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnPostSelectedListener listener) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            db = FirebaseFirestore.getInstance();
                mUserRef = db.collection("users").document();
                mContentTextView = (TextView) itemView.findViewById(R.id.post_content);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.post_photo);
                mUsernameTextView = (TextView) itemView.findViewById(R.id.username);
                mLike = (ImageButton) itemView.findViewById(R.id.imageButton2);
                mComment = (ImageButton) itemView.findViewById(R.id.imageButton);
                mFlag = (ImageButton) itemView.findViewById(R.id.imageButton3);
                mTimePostedTextView = (TextView) itemView.findViewById(R.id.timestamp);
                numOfTimesFlagged = 0;
                likes = (TextView) itemView.findViewById(R.id.numOfLikes);

            final Post post = snapshot.toObject(Post.class);
            if (user != null) {
                Resources resources = itemView.getResources();
                mContentTextView.setText(post.getContent());
                mUsernameTextView.setText(user.getDisplayName());
                numOfTimesFlagged = post.getNumOfTimesFlagged();
                DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                mTimePostedTextView.setText(dateFormat.format(post.getTimestamp()));
                //error here
//            mPost.setTimestamp(post.getTimestamp());
            }
            else {

            }


            String photoName = post.getPhotoFilename();

            if (!post.hasPhoto()) {
                mPhotoImageView.setVisibility(View.GONE);
            }


//            File mPhotoFile = PostLab.get(getActivity()).getPhotoFile(mPost);
//            if (mPhotoImageView != null) {
//                Bitmap bitmap = PictureUtils.getScaledBitmap(
//                        mPhotoFile.getPath(), getActivity());
//                mPhotoImageView.setImageBitmap(bitmap);
//            } else {
//                mPhotoImageView.setVisibility(View.GONE);
//            }

            numOfTimesFlagged = post.getNumOfTimesFlagged();
            mFlag.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     numOfTimesFlagged++;
                     if (numOfTimesFlagged == 1) {
                         mFlag.setColorFilter(Color.rgb(255,165,0));
                     }
                     if (numOfTimesFlagged == 2) {
                         mFlag.setColorFilter(Color.rgb(255,0,0));
                     }
                     if (numOfTimesFlagged == 3) {
                         deletePost(mUserRef, mPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Log.d("error", "this was a success");
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Log.w("error", "this was not a success", e);
                             }
                         });
                     }
                 }

                 private Task<Void> deletePost(final DocumentReference userRef, final Post post) {
                     return db.runTransaction(new Transaction.Function<Void>() {
                         @Override
                         public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                             DocumentReference postRef = userRef.collection("posts").document(snapshot.getId());
                             transaction.delete(postRef);

                             return null;
                         }
                     });
                 }
             });
            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numOfLikes++;
                    if(numOfLikes > 0){
                        mLike.setColorFilter(Color.RED);
                        likes.setText(String.valueOf(numOfLikes));
                    }
                }
            });
            //Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPostSelected(snapshot);
                    }
                }
            });
        }
    }
}

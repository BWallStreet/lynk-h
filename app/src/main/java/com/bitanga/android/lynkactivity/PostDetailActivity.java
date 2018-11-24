package com.bitanga.android.lynkactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Text;

import java.util.EventListener;
import java.util.InputMismatchException;

/**Displays original post as well as comments**/
public class PostDetailActivity extends AppCompatActivity
implements com.google.firebase.firestore.EventListener<DocumentSnapshot>, CommentDialogFragment.CommentListener {

    public static final String KEY_POST_ID =
            "com.hbernal.android.Lynk";

    private static final String LOG_VAL = "Lynk";

    private RecyclerView mCommentRecycler;

    private TextView mContentView;
    private TextView mTimestampView;
    private ImageView mPhotoView;
    private FloatingActionButton mAddCommentButton;

    private CommentDialogFragment mCommentDialog;

    private FirebaseFirestore db;
    private DocumentReference mPostRef;
    private ListenerRegistration mPostRegistration;
    private CommentAdapter mCommentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mCommentRecycler = findViewById(R.id.recyclerComments);

        //Get post Id from extras
        String postId = getIntent().getExtras().getString(KEY_POST_ID);
        if (postId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_POST_ID);
        }

        db = FirebaseFirestore.getInstance();

        mPostRef = db.collection("users").document("testUser").collection("posts").document(postId);

        //get comments
        Query commentsQuery = mPostRef
                .collection("comments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);

        //RecyclerView
        mCommentAdapter = new CommentAdapter(commentsQuery) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mCommentRecycler.setVisibility(View.GONE);
                } else {
                    mCommentRecycler.setVisibility(View.VISIBLE);
                }
            }
        };

        mCommentRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecycler.setAdapter(mCommentAdapter);

        mCommentDialog = new CommentDialogFragment();

    }

    @Override
    public void onStart() {
        super.onStart();

        mCommentAdapter.startListening();
        mPostRegistration = mPostRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mCommentAdapter.stopListening();

        if (mPostRegistration != null) {
            mPostRegistration.remove();
            mPostRegistration = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * Listener for Post document
     **/
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(LOG_VAL, "post:onEvent", e);
            return;
        }

        onPostLoaded(snapshot.toObject(Post.class));
    }

    private void onPostLoaded(Post post) {
        mContentView = (TextView)findViewById(R.id.post_content);
        mTimestampView = (TextView) findViewById(R.id.post_timestamp);
        mPhotoView = (ImageView) findViewById(R.id.post_photo);
        mAddCommentButton = (FloatingActionButton) findViewById(R.id.fabShowCommentDialog);

        mContentView.setText(post.getContent());
        mTimestampView.setText(post.getTimestamp().toString());
        if (post.hasPhoto()) {
            mPhotoView.setVisibility(View.VISIBLE);
        }
        else {
            mPhotoView.setVisibility(View.INVISIBLE);
        }

        mAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**shows comment dialog**/
                mCommentDialog.show(getSupportFragmentManager(), CommentDialogFragment.LOG_VAL);
            }
        });
    }

    @Override
    public void onComment(Comment newComment) {
        //add new comment and update the aggregate totals
        addComment(mPostRef, newComment)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_VAL, "Comment added");

                        //Hide keyboard and scroll to top
                        hideKeyboard();
                        mCommentRecycler.smoothScrollToPosition(0);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_VAL, "Add comment failed", e);

                        //show failure message and hide keyboard
                        hideKeyboard();
                        Snackbar.make(findViewById(android.R.id.content), "Failed to add comment",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private Task<Void> addComment(final DocumentReference postRef, final Comment comment) {
        //create new reference for new comment, for use into the transaction
        final DocumentReference commentRef = postRef.collection("comments").document();

        //In a transaction, add the new comment and update the aggregate totals
        return db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                Post post = transaction.get(postRef).toObject(Post.class);

                //Compute new number of comments
                int newNumComments = post.getNumComments() + 1;

                //Set new post info
                post.setComments(newNumComments);

                //Commit to Firestore db
                transaction.set(postRef, post);
                transaction.set(commentRef, comment);

                return null;
            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

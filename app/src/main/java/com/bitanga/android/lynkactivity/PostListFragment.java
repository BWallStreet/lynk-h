package com.bitanga.android.lynkactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import org.w3c.dom.Document;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PostListFragment extends Fragment
implements PostAdapter.OnPostSelectedListener {
    private static final String LOG_VAL = "Lynk";

    //actually has the post recycler view
    private RecyclerView mPostRecyclerView;
    private PostAdapter mAdapter;
    private ViewGroup mEmptyView;
    private int mPostPosition;

    private FirebaseFirestore db;
    private DocumentReference mUserRef;
    private CollectionReference mPostRef;
    private ListenerRegistration mUserRegistration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        mPostRecyclerView = (RecyclerView) view.findViewById(R.id.post_recycler_view);
//        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyView = (ViewGroup) view.findViewById(R.id.viewEmpty);

        db = FirebaseFirestore.getInstance();
        mUserRef = db.collection("users").document("testUser");
        mPostRef = db.collection("users").document("testUser").collection("posts");


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        /**When click add button, call to PostPagerActivity, where it will create new Post**/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(getActivity(), PostPagerActivity.class));
            }

    });

        // Get limit of posts and order by timestamp of posts
        Query postsQuery = mUserRef
                .collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);


        //RecyclerView
        mAdapter = new PostAdapter(postsQuery, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mPostRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                    Log.d(LOG_VAL, "mPostRecyclerView is Invisible");
                } else {
                    mPostRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                    Log.d(LOG_VAL, "mPostRecyclerView is Visible");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.w(LOG_VAL, "Error: check logs for info.", e);
            }
        };

        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPostRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter.startListening();
//        mPostRegistration = mUserRef.addSnapshotListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();

//        if (mPostRegistration != null) {
//            mPostRegistration.remove();
//            mPostRegistration = null;
//        }
    }

    @Override
    public void onPostSelected(DocumentSnapshot post) {
        //Go to post and modify if you're the user of post
//        Intent intent = PostPagerActivity.newIntent(getActivity(),UUID.fromString(post.getId()));
        Intent intent = new Intent(getContext(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.KEY_POST_ID, post.getId());

        startActivity(intent);
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_post_list, menu);

    }
}

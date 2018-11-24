package com.bitanga.android.lynkactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Document;

import java.util.List;
import java.util.UUID;


/**calls PostFragment activity**/
/**model like PostDetailActivity but just goes to Fragment**/
public class PostPagerActivity extends AppCompatActivity
implements PostFragment.PostListener{

    public static final String EXTRA_POST_ID =
            "com.hbernal.android.Lynk";

    private static final String LOG_VAL = "Lynk";

    private ViewPager mViewPager;

    private FirebaseFirestore db;
    private DocumentReference mUserRef;
    private ListenerRegistration mUserRegistration;


    private PostAdapter mPostAdapter;

    private PostFragment mPostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.post_view_pager);

        db = FirebaseFirestore.getInstance();
        mUserRef = db.collection("users").document("testUser");

        mPostFragment = new PostFragment();


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(android.R.id.content, PostFragment.newInstance()).commit();

        PostFragment.newInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
//        mUserRegistration = mUserRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

//        if (mUserRegistration != null) {
//            mUserRegistration.remove();
//            mUserRegistration = null;
//        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onPost(Post post) {
        Log.d(LOG_VAL, "TEST: " + post.getContent());
        addPost(mUserRef, post)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_VAL, "Post added");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(LOG_VAL, "Add post failed", e);

                    }
                });
    }

    private Task<Void> addPost(final DocumentReference userRef, final Post post) {
        final DocumentReference postRef = userRef.collection("posts").document();

        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                User user = transaction.get(userRef).toObject(User.class);

                //add user specs?

//                transaction.set(userRef, user);
                transaction.set(postRef, post);
                return null;
            }
        });
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to leave this page?")
                .setMessage("Your post will be deleted if you choose to leave")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete post here
//                        PostLab.get(getBaseContext()).deletePost(mPosts.get(mViewPager.getCurrentItem()));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}

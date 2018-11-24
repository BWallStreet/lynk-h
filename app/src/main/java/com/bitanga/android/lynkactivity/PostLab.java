package com.bitanga.android.lynkactivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PostLab {
    private static final String LOG_VAL = "Lynk";
    private static PostLab sPostLab;

    private String postId;
    //use list for now
    private List<Post> mPosts;
    private Post mPost;
    private Context mContext;
    //firebase database
    private FirebaseFirestore db;
    private CollectionReference mCollectRef;

    public static PostLab get(Context context) {
        if (sPostLab == null) {
            sPostLab = new PostLab(context);
        }
        return sPostLab;
    }

    public PostLab(Context context) {
        mContext = context.getApplicationContext();
        //database = post base helper with context and get writable db
        //for now
        mPosts = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mCollectRef = db.collection("users").document("testUser").collection("posts");
    }

    //add post
    public void addPost(Post p) {
        mPosts.add(p);

        Map<String, Object> post = new HashMap<>();
        post.put("uuid", p.getId().toString());
        post.put("content", p.getContent());
        if (p.hasPhoto()) {
            post.put("photoFile", p.getPhotoFilename());
        }

        mCollectRef.document(p.getId().toString())
                .set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_VAL, "Post has been saved");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(LOG_VAL, "Post did not get saved", e);
            }
        });

    }

    //delete post
    public void deletePost(Post p) {
        mPosts.remove(p);
        //get db id of post
        mCollectRef.document(p.getId().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_VAL, "Post has been deleted");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(LOG_VAL, "Problem in deleting post", e);
            }
        });
    }

    public List<Post> getPosts() {
        //for now
        //retrieve all posts from database and return those
//        mPosts = new ArrayList<>();

//        mCollectRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.w(LOG_VAL, "Listen failed", e);
//                    return;
//                }
//
//                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
//                    if (snapshot != null && snapshot.exists()) {
//                        Log.d(LOG_VAL, "Current data: " + snapshot.getData());
//                        Post post = snapshot.toObject(Post.class);
//                        mPosts.add(post);
//                    } else {
//                        Log.d(LOG_VAL, "Current data: null");
//                    }
//                }
//            }
//        });

        /**other way to get posts from db**/

//        //get all post documents
//        mCollectRef.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if (queryDocumentSnapshots != null) {
////                            Log.d(LOG_VAL, "Retrieving posts...");
//                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
////                                Log.d(LOG_VAL, document.getId() + "=> " + document.getData());
//
//                                Post post = document.toObject(Post.class);
//                                mPosts.add(post);
//
//                            }
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(LOG_VAL, "Error getting post documents.", e);
//            }
//        });
//
//
//        }
        Log.d(LOG_VAL, "size: " + mPosts.size());
        return mPosts;
    }


    //redo this one to get post from database???
    public Post getPost(UUID id) {
//        postId = id.toString();
//        mCollectRef.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
//                            if (document.getId() == postId.toString()) {
//                                mPost = document.toObject(Post.class);
//                            }
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//        return mPost;

        for (Post post : mPosts) {
            if (post.getId().equals(id)) {
                return post;
            }
        }
        return null;
    }

    public void updatePost(Post post) {
        String uuidString = post.getId().toString();
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("uuid",uuidString);
        postMap.put("content", post.getContent());
        if (post.hasPhoto()) {
            postMap.put("photoFile", post.getPhotoFilename());
        }
        mCollectRef.document(uuidString)
                .set(postMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_VAL, "Post updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_VAL, "Error in updating post",e);
                    }
                });
    }

    public File getPhotoFile(Post post) {
        File filesDir = mContext.getFilesDir();
        //error here
        String photoFileTest = post.getPhotoFilename();
        return new File(filesDir, post.getPhotoFilename());
    }

    interface FirebaseCallback {
        void onCallBack(List<Post> list);
    }
}

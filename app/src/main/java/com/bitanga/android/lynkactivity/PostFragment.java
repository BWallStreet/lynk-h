package com.bitanga.android.lynkactivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

//import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.firestore.Firestore;

//import com.google.firebase.FirebaseOptions;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PostFragment extends Fragment {
    private static final String ARG_POST_ID = "post_id";
    private int CAMERA_PERMISSION_CODE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String LOG_VAL = "Lynk";
    private static String projectId = "lynk-2f86b";

//    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/testUser/posts/");
    private DocumentReference mPostRef;
    private FirebaseFirestore db;

    private Post mPost;
    private File mPhotoFile;
    private EditText mContentField;
    private Button mSubmitButton;
    private Uri mImageUri = null;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    /**Initializes the PostFragment
     * @Parameters: a Post instance's postID (UUID)
     * @Returns an instance of PostFragment**/
    public static PostFragment newInstance() {

        PostFragment fragment = new PostFragment();

        return fragment;
    }

    interface PostListener {
        void onPost(Post post);
    }

    private PostListener mPostListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //get rid of this
//        mPost = PostLab.get(getActivity()).getPost(postId);

        mPost = new Post();

        //and this
//        mPhotoFile = PostLab.get(getActivity()).getPhotoFile(mPost);

//        db = FirebaseFirestore.getInstance();
//        mPostRef = db.collection("users").document("testUser").collection("posts").document(postId.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        mContentField = (EditText) v.findViewById(R.id.post_content);
        mContentField.setText(mPost.getContent());
        mContentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPost.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPost.setTimestamp(Calendar.getInstance().getTime());

        mSubmitButton = (Button) v.findViewById(R.id.submit_post);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                savePost();
                if (mPost.getContent() == null) {
                    Toast.makeText(getContext(),
                            "Cannot submit an empty post",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //when clicked, go back to PostListActivity menu
                    //update post
//                    PostLab.get(getContext()).updatePost(mPost);
                    /**when click submit, create new post**/
                    /**go back in PostPagerActivity, which will
                     * add new post in database
                     */
//                    post.setContent(mContentField.getText().toString());

                    Log.d(LOG_VAL, "CONTENT VAL: " + mContentField.getText().toString());

                    if (mPhotoView != null) {
                        mPost.setHasPhoto(true);
                    }
                    else {
                        mPost.setHasPhoto(false);
                    }
                    mPost.setUsername("testName");
                    mPost.setComments(0);
                    mPost.setLikes(0);
                    mPost.setNumOfTimesFlagged(0);
                    //not sure if this is right
                    mPost.setTimestamp(new Date());

                    if (mPostListener != null) {
                        mPostListener.onPost(mPost);
                    }
//                    if(mPost.getNumOfTimesFlagged() == 3){
//
//                    }
                    /**go back to postpageractivity**/
                    getActivity().finish();
                }
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.post_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(getActivity().getPackageManager()) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_VAL, "this: " + ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA));
                Log.d(LOG_VAL, "that:  " + PackageManager.PERMISSION_GRANTED);

                Log.d(LOG_VAL, "is this true? " + (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED));

                //if the user has permission for camera use, allows for camera to bue sued
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    dispatchTakePictureIntent();

                    //use camera, store image to thumbnail
                    Uri uri = FileProvider.getUriForFile(getActivity(),
                            "com.bitanga.android.lynkactivity.fileprovider",
                            mPhotoFile);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    List<ResolveInfo> cameraActivities = getActivity()
                            .getPackageManager().queryIntentActivities(captureImage,
                                    PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo activity : cameraActivities) {
                        getActivity().grantUriPermission(activity.activityInfo.packageName,
                                uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }

                    startActivityForResult(captureImage, REQUEST_IMAGE_CAPTURE);

                } else {
                    //else ask user for permission (alert dialog)
                    requestCameraPermission();
                }

            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.post_photo);
        //default to no photo uploaded
        mPhotoView.setVisibility(View.INVISIBLE);
//        updatePhotoView();

        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PostListener) {
            mPostListener = (PostListener) context;
        }
    }



    /**Saves post to firebase database when user submits**/
//    private void savePost() {
//        Map<String, Object> post = new HashMap<>();
//        post.put("uuid", mPost.getId().toString());
//        post.put("content", mPost.getContent());
//        //works!
//        if (mPhotoView != null && mPhotoFile.exists()) {
//            post.put("picture", mPost.getPhotoFilename());
//        }
//        mCollectRef.document(mPost.getId().toString())
//                .set(post)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(LOG_VAL, "Post has been saved");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(LOG_VAL, "Post did not get saved", e);
//            }
//        });
//    }

    /**Inflates view of option menu
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_post, menu);
    }


    /**Determines which menu item user clicked on
     *
     * @param item
     * @return boolean value if menu item was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_pic:
                //check if permission already granted
                //if so then proceed (toast message for now)
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();

                    //else ask user for permission (alert dialog)
                } else {
                    requestCameraPermission();
                }
                return true;
            case android.R.id.home:
                //when back button pressed, asks user if sure want to leave page
                new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure you want to leave this page?")
                        .setMessage("Your post will be deleted if you choose to leave")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /**change so post is not added in the first place?**/
                                //deletes post
//                                PostLab.get(getActivity()).deletePost(mPost);
                                getActivity().finish();
                            }
                        })
                        //exits out of dialog view
                        .setNegativeButton("No", null)
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePhotoView() {
        if (mPhotoView == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setVisibility(View.VISIBLE);
//            mPost.setHasPhoto(true);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            takePictureIntent.setType("image/*"); //?
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("Camera permission is needed to attach photos to your post")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String [] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mPhotoView = (ImageView) getActivity().findViewById(R.id.image);
//            mPhotoView.setImageBitmap(imageBitmap);
//
//            //insert image to database here
//        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bitanga.android.lynkactivity.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            updatePhotoView();
        }

    }
}

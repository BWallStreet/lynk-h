package com.bitanga.android.lynkactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CommentDialogFragment extends DialogFragment {

    public static final String LOG_VAL = "Lynk";

    private EditText mCommentContent;

    interface CommentListener {
        void onComment(Comment comment);
    }

    private CommentListener mCommentListener;
    private Button mSubmitButton;
    private Button mCancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_comment, container, false);

        mCommentContent = (EditText) v.findViewById(R.id.commentContent);

        mSubmitButton = v.findViewById(R.id.postFormButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**create new comment when click submit on dialog**/
                Comment comment = new Comment(
                        FirebaseAuth.getInstance().getCurrentUser(),
                        //error: java.lang.NullPointerException: Attempt to invoke virtual method 'android.text.Editable android.widget.EditText.getText()' on a null object reference
                        mCommentContent.getText().toString());
                if (mCommentListener != null) {
                    mCommentListener.onComment(comment);
                }
                dismiss();
            }

        });

        mCancelButton = v.findViewById(R.id.postFormCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CommentListener) {
            mCommentListener = (CommentListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}

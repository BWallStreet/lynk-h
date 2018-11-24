package com.bitanga.android.lynkactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MostLynkedPostListFragment extends Fragment {
    //when user presses like on a post (in PostListFragment)
    //that post gets saved to firebase as user's collection of
    //lyked posts
    //then that is accessed here
    //and displayed in recyclerview

    private RecyclerView mPostRecyclerView;
    private int mPostCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lynked_post_list, container, false);
        mPostRecyclerView = (RecyclerView) view.findViewById(R.id.post_recycler_view);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //for now
        mPostCount = 0;
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //if in database, the number of lyked posts is 0, add text that says,
    //lyked no posts yet




}

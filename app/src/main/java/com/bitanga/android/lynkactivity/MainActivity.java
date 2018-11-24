package com.bitanga.android.lynkactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

//based off code from http://codedivasbysrishti.blogspot.com/2017/04/combining-navigation-drawer-and.html
/**Implements the navigation drawer with other fragments**/
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private ViewPager mViewPager;
    private DrawerLayout drawer;
    private CharSequence mTitle;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager)findViewById(R.id.view_pager);

        mTitle = getTitle();

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        //creates the toolbar/actionbar on top of the page
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //creates toggle to open and close navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //creates navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //creates instance of ViewPagerAdapter that assigns fragments to nav
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_post_list, 0);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        switch(item.getItemId()) {
            case R.id.nav_post_list:
                mTitle="Forum";
                mViewPager.setCurrentItem(0);
                break;
            case R.id.nav_map:
                mTitle="Campus Map";
                mViewPager.setCurrentItem(1);
                break;
            case R.id.nav_most_lynked:
                mTitle="Your Most Lynked Posts";
                mViewPager.setCurrentItem(2);
                break;
            case R.id.nav_friend_list:
                mTitle="Friend List";
                mViewPager.setCurrentItem(3);
                break;
            default:
                mTitle="Settings";
                mViewPager.setCurrentItem(4);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        item.setChecked(true);
        getSupportActionBar().setTitle(mTitle);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

//    public android.support.v4.app.Fragment fragmentForPosition(int sectionNumber) {
////        switch (sectionNumber) {
////            case 0:
//////                return new Tab1Fragment();
////                return new PostListFragment();
////                //add more
////            //add most lynked posts
////            //add friend list
////            case 1:
////            //add map view
////                //set add button to invisible?
////                return new MapFragment();
////            default:
////                return new PostListFragment();
////        }
////    }

}

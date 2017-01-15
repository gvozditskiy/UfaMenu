package com.gvozditskiy.ufamenu.ActivitiesAndFragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gvozditskiy.ufamenu.Interfaces.DataUpdateCallback;
import com.gvozditskiy.ufamenu.Interfaces.OnCategoryClickListener;
import com.gvozditskiy.ufamenu.Interfaces.OnOfferClickListener;
import com.gvozditskiy.ufamenu.Interfaces.RegisterUpdateCallback;
import com.gvozditskiy.ufamenu.Interfaces.UnRegisterUpdateCallback;
import com.gvozditskiy.ufamenu.LoaderService;
import com.gvozditskiy.ufamenu.R;
import com.gvozditskiy.ufamenu.Constants;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnCategoryClickListener, OnOfferClickListener, RegisterUpdateCallback, UnRegisterUpdateCallback {

    List<DataUpdateCallback> callbacks = new ArrayList<>();
    ProgressDialog progressDialog;
    int currentPosition = 0;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.dialig_title));
        progressDialog.setMessage(getResources().getString(R.string.dialig_message));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        if (savedInstanceState == null) {
            startService();
        }

        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

        ResponseReceiver responseReceiver = new ResponseReceiver();

        //регистрируем BroadcastReceiver и IntentFilter
        LocalBroadcastManager.getInstance(this).registerReceiver(
                responseReceiver, intentFilter
        );

        if (savedInstanceState == null) {
            checkMenu(0);}
//        } else {
//            checkMenu(savedInstanceState.getInt(Constants.NAVBAR_POSITION));
//        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentByTag(Constants.FRAG_TAG);
                if (fragment instanceof CategoriesFragment || fragment instanceof OffersListFragment
                        || fragment instanceof OfferFragment) {
                    navigationView.setCheckedItem(R.id.nav_categories);
                } else {
                    navigationView.setCheckedItem(R.id.nav_contacts);

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_categories) {
            fragment = new CategoriesFragment();

        } else if (id == R.id.nav_contacts) {
            fragment = new ContactsFragment();
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_main, fragment, Constants.FRAG_TAG);
        ft.addToBackStack(null);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Запускает сервис для загрузки данных с сервера
     */
    private void startService() {
        Intent intent = new Intent(this, LoaderService.class);
        startService(intent);
        progressDialog.show();
    }

    @Override
    public void onCategoryClicked(String id) {
        OffersListFragment fragment = OffersListFragment.newInstance(id);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onOfferClicked(String id) {
        OfferFragment fragment = OfferFragment.newInstance(id);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onRegisterInterface(DataUpdateCallback callback) {
        addCallback(callback);
    }

    @Override
    public void unRegisterInterface(DataUpdateCallback callback) {
        removeCallback(callback);
    }

    /**
     * Подкласс для приема широковещательного интента
     */
    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra(Constants.EXTRA_STATUS);
            if (status.equals(Constants.STATUS_OK)) {
                for (DataUpdateCallback callback : callbacks) {
                    try {
                        callback.updateData();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), getResources()
                        .getString(R.string.error_data_loading), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    public void addCallback(DataUpdateCallback dataUpdateCallback) {
        callbacks.add(dataUpdateCallback);
    }

    public void removeCallback(DataUpdateCallback dataUpdateCallback) {
        callbacks.remove(dataUpdateCallback);
    }

    public void releaseCallbacks() {
        callbacks.clear();
    }

    private void checkMenu(int i) {
        int id=0;
        switch (i) {
            case 1:
                id=R.id.nav_contacts;
                break;
            case 0:
                id = R.id.nav_categories;
                break;
        }
        if (id!=0) {
            navigationView.setCheckedItem(id);
            navigationView.getMenu().performIdentifierAction(R.id.nav_categories, 0);
        }

    }



}

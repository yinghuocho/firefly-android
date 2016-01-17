package org.gofirefly.firefly_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.gofirefly.firefly_android.fragment.TabFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private LinearLayout mDrawerMenu;
    private ListView mDrawerMenuList;

    private int mCurrentMenuItemPosition = -1;
    private String[] drawer_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drw_layout);

        // Setup the shadow for the drawer
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Create drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.app_name,
                R.string.app_name){

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mCurrentMenuItemPosition > -1) {
                    getSupportActionBar().setTitle(
                            drawer_menu[mCurrentMenuItemPosition]);
                } else {
                    getSupportActionBar().setTitle(R.string.app_name);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initDrawerMenu();

        initTabFragment(savedInstanceState);
    }

    private void initTabFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            TabFragment tabFragment = new TabFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment, tabFragment)
                    .commit();
        }
    }

    private void initDrawerMenu() {
        mDrawerMenu = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerMenuList = (ListView) findViewById(R.id.drawer_menu);

        drawer_menu = this.getResources().getStringArray(R.array.drawer_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_menu_item, drawer_menu);
        mDrawerMenuList.setAdapter(adapter);

        mDrawerMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectMenuItem(position);
            }
        });
    }

    private void selectMenuItem(int position) {
        mCurrentMenuItemPosition = position;

        mDrawerMenuList.setItemChecked(position, true);

        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerMenu);

        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case 1:
                intent = new Intent(this, HelpActivity.class);
                break;
            case 2:
                intent = new Intent(this, FeedbackActivity.class);
                break;
            case 3:
                intent = new Intent(this, AboutActivity.class);
                break;
            default:
                intent = new Intent(this, SettingsActivity.class);
        }

        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

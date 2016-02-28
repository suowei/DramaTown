package net.saoju.dramatown;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import net.saoju.dramatown.Adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout loginView;
    private LinearLayout userView;
    private TextView userNameView;
    private LinearLayout userDropdown;

    private SharedPreferences sharedPref;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        loginView = (LinearLayout) headerView.findViewById(R.id.login_view);
        userView = (LinearLayout) headerView.findViewById(R.id.user_view);
        Button loginButton = (Button) loginView.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        userNameView = (TextView) userView.findViewById(R.id.username);
        sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String username = sharedPref.getString("NAME", "");
        if (username.isEmpty()) {
            userView.setVisibility(View.GONE);
            loginView.setVisibility(View.VISIBLE);
        } else {
            userView.setVisibility(View.VISIBLE);
            loginView.setVisibility(View.GONE);
            userNameView.setText(username);
        }
        ImageView userButton = (ImageView) userView.findViewById(R.id.user_button);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("id", sharedPref.getInt("ID", 0));
                MainActivity.this.startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        userDropdown = (LinearLayout) userView.findViewById(R.id.user_dropdown);
        userNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDropdown.setVisibility(userDropdown.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        TextView logout = (TextView) userView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDropdown.setVisibility(View.GONE);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("退出账号")
                        .setMessage("确定要退出当前账号吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPref.edit().clear().apply();
                                getSharedPreferences("cookies", 0).edit().clear().apply();
                                userView.setVisibility(View.GONE);
                                loginView.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);

        NewEpisodesFragment newEpisodesFragment = new NewEpisodesFragment();
        ReviewIndexFragment reviewIndexFragment = new ReviewIndexFragment();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(newEpisodesFragment);
        fragments.add(reviewIndexFragment);

        List<String> titles = new ArrayList<>();
        titles.add("新剧");
        titles.add("评论");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchResultsActivity.class)));
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_drama) {
            Intent intent = new Intent(MainActivity.this, DramaIndexActivity.class);
            MainActivity.this.startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String username = sharedPref.getString("NAME", "");
        if (username.isEmpty()) {
            userView.setVisibility(View.GONE);
            loginView.setVisibility(View.VISIBLE);
        } else {
            userView.setVisibility(View.VISIBLE);
            loginView.setVisibility(View.GONE);
            userNameView.setText(username);
        }
    }
}

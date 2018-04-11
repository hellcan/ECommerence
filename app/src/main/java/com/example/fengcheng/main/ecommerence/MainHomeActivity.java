package com.example.fengcheng.main.ecommerence;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class MainHomeActivity extends AppCompatActivity{
    DrawerLayout drawerLayout;
    ListView leftDrawer;
    Toolbar toolbar;
    private final int DEFAULT_POS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();

        initView();

        initDrawer();

        clickListener();



    }

    private void clickListener() {
        //click listener
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displaySelectedScreen(position);
            }
        });

        //listview click

        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displaySelectedScreen(position);
            }
        });
    }

    private void displaySelectedScreen(int position) {
        if (position > 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment(), "hfgt").commit();
            leftDrawer.setItemChecked(position, true);
            drawerLayout.closeDrawers();
        }
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_open);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();



        //listview header
        View header = LayoutInflater.from(this).inflate(R.layout.left_drawer_header, null);
        leftDrawer.addHeaderView(header);
        leftDrawer.setAdapter(new DrawerAdapter(this));

        //this is default position
        displaySelectedScreen(DEFAULT_POS);

    }

    private void initView() {

        drawerLayout = findViewById(R.id.drawer_layout);
        leftDrawer = findViewById(R.id.left_drawer);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.title_tv);
        textView.setText("Home");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_menu, menu);
        return true;
    }
}

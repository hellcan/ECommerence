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
import android.widget.SlidingDrawer;
import android.widget.TextView;

import java.util.zip.Inflater;

public class MainHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView leftDrawer;
    Toolbar toolbar;
    private final int DEFAULT_POS = 1;
    int[] menuId = {R.id.profile, R.id.shop, R.id.order, R.id.logout};

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
        leftDrawer.setNavigationItemSelectedListener(this);
    }

//    private void displaySelectedScreen(int position) {
//        if (position > 0) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment(), "hfgt").commit();
//            leftDrawer.setItemChecked(position, true);
//            drawerLayout.closeDrawers();
//        }
//    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_open);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // header
        View header = LayoutInflater.from(this).inflate(R.layout.left_drawer_header, null);

        //this is default position
//        displaySelectedScreen(DEFAULT_POS);
        leftDrawer.setCheckedItem(R.id.shop);

    }

    private void initView() {

        drawerLayout = findViewById(R.id.drawer_layout);
        leftDrawer = findViewById(R.id.left_drawer);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
//        TextView textView = toolbar.findViewById(R.id.title_tv);
//        textView.setText("Home");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile:
//                toolbar.setTitle("我的动态");
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProfileFragment(), "hfgt").commit();
                break;
            case R.id.shop:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment(), "hfgt").commit();
//                toolbar.setTitle("我的留言");
                break;
            case R.id.order:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new FragmentThree()).commit();
//                toolbar.setTitle("附近的人");
                break;
            case R.id.logout:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new FragmentThree()).commit();
//                toolbar.setTitle("附近的人");
                break;
        }

        menuItem.setChecked(true);//点击了把它设为选中状态


        drawerLayout.closeDrawers();//关闭抽屉
        return true;
    }
}

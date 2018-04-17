package com.example.fengcheng.main.ecommerence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengcheng.main.utils.SpUtil;

public class MainHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView leftDrawer;
    Toolbar toolbar;
    TextView toolBarTitle;
    private final int DEFAULT_POS = 1;
    int[] menuId = {R.id.profile, R.id.shop, R.id.order, R.id.logout};
    private static final String TAG = "MainHomeActivity";


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


    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_open);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // header
        View header = leftDrawer.getHeaderView(0);
        TextView nameTv = header.findViewById(R.id.username_tv);
        nameTv.setText("Welcome " + SpUtil.getLastName(getBaseContext()));


        //this is default position
        leftDrawer.setCheckedItem(R.id.shop);
        switchFragment(R.string.m_home, new HomeFragment(), "shopFgt");

    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        leftDrawer = findViewById(R.id.left_drawer);

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolBarTitle = toolbar.findViewById(R.id.title_tv);
        toolBarTitle.setText(R.string.m_home);
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
                switchFragment(R.string.m_profile, new ProfileFragment(), "profileFgt");
                break;
            case R.id.shop:
                switchFragment(R.string.m_home, new HomeFragment(), "shopFgt");
                break;
            case R.id.order:
                switchFragment(R.string.m_order, new FragmentOrder(), "orderFgt");
                break;
            case R.id.aboutus:
                switchFragment(R.string.about, new FragmentAbout(), "aboutFgt");
                break;

            case R.id.logout:
                SpUtil.clearUserInfo(getBaseContext());
                startActivity(new Intent(MainHomeActivity.this, MainActivity.class));
                finish();
                break;
        }

        menuItem.setChecked(true);//点击了把它设为选中状态

        drawerLayout.closeDrawers();//关闭抽屉

        return true;
    }

    public void switchFragment(int title, Fragment fragment, String tag) {
        toolBarTitle.setText(title);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
    }

    public void updateTitle(String title) {
        toolBarTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ShoppingCartFragment(), "shoppingCartFgt").commit();
                break;
            case R.id.wish:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WishFragment(), "wishFgt").commit();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

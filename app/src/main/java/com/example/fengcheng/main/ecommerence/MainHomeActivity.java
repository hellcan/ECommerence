package com.example.fengcheng.main.ecommerence;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengcheng.main.db.DbManager;
import com.example.fengcheng.main.utils.SpUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView leftDrawer;
    Toolbar toolbar;
    TextView toolBarTitle, nameTv;
    ImageView avatarIv;
    ImageButton cartIbtn;
    private Badge qBadgeView;
    private final static int GALLERY_CODE = 900;
    private static final String TAG = "MainHomeActivity";
    private DbManager dbManager;
    int cartItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initDb();

        initToolbar();

        initView();

        initDrawer();

        clickListener();

    }

    private void initDb() {
        if (dbManager == null) {
            dbManager = new DbManager(this);
        }
        dbManager.openDatabase();
    }


    private void clickListener() {
        Log.i(TAG, "clickListener: ");
        leftDrawer.setNavigationItemSelectedListener(this);

        cartIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(R.string.cart_list, new ShoppingCartFragment(), "shoppingCartFgt");
            }
        });
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
        nameTv = header.findViewById(R.id.username_tv);
        nameTv.setText("Welcome " + SpUtil.getLastName(getBaseContext()));

        //avatar
        avatarIv = header.findViewById(R.id.avatar_iv);
        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gallery intent
                Intent startGallery = new Intent(Intent.ACTION_GET_CONTENT);

                startGallery.setType("image/*");

                startActivityForResult(startGallery, GALLERY_CODE);
            }
        });


        //this is default position
        leftDrawer.setCheckedItem(R.id.shop);
        switchFragment(R.string.m_home, new HomeFragment(), "shopFgt");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_CODE:
                //user canceled
                if (data == null) {
                    return;
                } else {
                    //system will return photo uri instead of a photo
                    Uri uri = data.getData();
                    Picasso.with(this)
                            .load(uri)
                            .resize(200, 200)
                            .into(avatarIv);
                }
        }
    }


    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        leftDrawer = findViewById(R.id.left_drawer);

    }

    private void initToolbar() {
        Log.i(TAG, "initToolbar: ");
        toolbar = findViewById(R.id.toolbar);
        toolBarTitle = toolbar.findViewById(R.id.title_tv);
        cartIbtn = toolbar.findViewById(R.id.cart_ibtn);
        toolBarTitle.setText(R.string.m_home);
        setSupportActionBar(toolbar);

        cartItemCount = dbManager.getCartItemList(SpUtil.getUserId(getBaseContext())).size();
        if (cartItemCount <= 0) {
            qBadgeView = new QBadgeView(getBaseContext()).bindTarget(cartIbtn).
                    setBadgeGravity(Gravity.END | Gravity.TOP).
                    setBadgeNumber(0);
        } else {
            qBadgeView = new QBadgeView(getBaseContext()).bindTarget(cartIbtn).
                    setBadgeGravity(Gravity.END | Gravity.TOP).
                    setBadgeNumber(cartItemCount);
        }

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

        menuItem.setChecked(true);//set up checked menu item

        drawerLayout.closeDrawers();//close drawer menu

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
            case R.id.wish:
                switchFragment(R.string.wish_list, new WishFragment(), "wishFgt");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDatabase();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Integer num) {
        cartItemCount = cartItemCount + num;
        if (cartItemCount <= 0) {
            qBadgeView.setBadgeNumber(0);

        } else {
            qBadgeView.setBadgeNumber(cartItemCount);
        }

    }

}

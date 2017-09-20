package yxy.pra0914;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.TextView;

import yxy.pra0914.MyView.CircleImageView;
import yxy.pra0914.MyView.HomeToolbar;
import yxy.pra0914.base.BaseActivity;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    private TextView txt_city;
    private static final int REQUESTCODE_FINDCITY = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeToolbar toolbar = (HomeToolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //点击头像跳往个人信息的编辑
        CircleImageView circleImageView = (CircleImageView)navigationView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.imageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forEdite = new Intent(getApplicationContext(),InfoEdite.class);
                    startActivity(forEdite);
                }
            });

        //点击主页上方的城市，跳往选择城市
        txt_city = (TextView)findViewById(R.id.txt_city);
        txt_city.setOnClickListener(this);


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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_journey) {
            // Handle the camera action
        } else if (id == R.id.nav_wallet) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txt_city:
                Intent findCity = new Intent(MainActivity.this,ChooseCity.class);
                startActivityForResult(findCity,REQUESTCODE_FINDCITY);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_FINDCITY:
                    if(data != null)
                    {
                        txt_city.setText(data.getExtras().get("city").toString());
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

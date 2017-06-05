package nickgao.com.meiyousample.myfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/5/28.
 */

public class TestActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple), new Fruit("Banana", R.drawable.banana),
            new Fruit("Orange", R.drawable.orange), new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear), new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple), new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Cherry", R.drawable.cherry), new Fruit("Mango", R.drawable.mango)};


    private List<Fruit> mFruitList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FruitAdapter mAdapter;

    private void initList() {
        for (int i = 0; i < 10; i++) {
            mFruitList.add(fruits[i]);
        }
    }

    private void updateList() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFruitList.clear();
                        for (int i = 9; i >= 0; i--) {
                            mFruitList.add(fruits[i]);
                        }
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipRefreshlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tb_toolbar);
        toolbar.setTitle("FruitsApp");
        //toolbar.setLogo(R.drawable.banana);

        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = (ActionBar) getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setCheckedItem(R.id.nav_call);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.float_tag);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(TestActivity.this,"Float menu clicked",Toast.LENGTH_LONG).show();
                Snackbar.make(floatingActionButton, "Data deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TestActivity.this, "Data restored", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });

        initList();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new FruitAdapter(this, mFruitList);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_backup:
                Toast.makeText(this, "backup", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_delete:
                Toast.makeText(this, "delete", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return true;
    }
}

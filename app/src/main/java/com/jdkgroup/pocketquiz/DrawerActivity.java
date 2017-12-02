package com.jdkgroup.pocketquiz;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jdkgroup.baseclass.BaseActivity;
import com.jdkgroup.customviews.slidingrootnav.SlidingRootNav;
import com.jdkgroup.customviews.slidingrootnav.SlidingRootNavBuilder;
import com.jdkgroup.customviews.vegalayoutmanager.VegaLayoutManager;
import com.jdkgroup.pocketquiz.drawer.DrawerAdapter;
import com.jdkgroup.pocketquiz.drawer.DrawerItem;
import com.jdkgroup.pocketquiz.drawer.SimpleItem;
import com.jdkgroup.pocketquiz.drawer.SpaceItem;
import com.jdkgroup.pocketquiz.fragment.CenteredTextFragment;

import java.util.Arrays;

public class DrawerActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener {

    private Toolbar toolbar;

    private final int POS_DASHBOARD = 0;
    private final int POS_PROFILE = 1;
    private final int POS_NOTIFICATION = 2;
    private final int POS_CONTACT_US = 3;
    private final int POS_ABOUT_US = 4;
    private final int POS_LOGOUT = 6;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.recyclerview_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_PROFILE),
                createItemFor(POS_NOTIFICATION),
                createItemFor(POS_CONTACT_US),
                createItemFor(POS_ABOUT_US),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(true);
        //recyclerView.setLayoutManager(new VegaLayoutManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finish();
        }
        slidingRootNav.closeMenu();

        toolbar.setTitle(screenTitles[position]);
        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.colorDrawerIconTint))
                .withTextTint(color(R.color.colorDrawerTextTint))
                .withSelectedIconTint(color(R.color.colorDrawerSelectedIconTint))
                .withSelectedTextTint(color(R.color.colorSelectedTextTint));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}

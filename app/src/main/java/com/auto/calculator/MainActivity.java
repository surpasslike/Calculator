package com.auto.calculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        bottomNav = findViewById(R.id.bottom_navigation);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HexToBinaryFragment());
        fragments.add(new TimestampConverterFragment());

        viewPager.setAdapter(new SimpleFragmentAdapter(this, fragments));

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_hex) {
                viewPager.setCurrentItem(0);
            } else if (item.getItemId() == R.id.nav_time) {
                viewPager.setCurrentItem(1);
            }
            return true;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNav.getMenu().getItem(position).setChecked(true);
            }
        });
    }
}
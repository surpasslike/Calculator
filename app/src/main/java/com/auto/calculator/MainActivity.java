package com.auto.calculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private final String[] tabTitles = {
            "抓包核心",
            "时间戳转换",
            "大页计算",
            "网络计算器",
            "网页"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HexToBinaryFragment());
        fragments.add(new TimestampConverterFragment());
        fragments.add(new HugepageCalFragment());
        fragments.add(new NetworkCalculatorFragment());
        fragments.add(new BrowserFragment());

        viewPager.setAdapter(new SimpleFragmentAdapter(this, fragments));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabTitles[position]);
            switch (position) {
                case 0:
                    tab.setIcon(android.R.drawable.ic_menu_edit);       // 抓包核心
                    break;
                case 1:
                    tab.setIcon(android.R.drawable.ic_menu_today);      // 时间戳转换
                    break;
                case 2:
                    tab.setIcon(android.R.drawable.ic_menu_manage);     // 大页计算
                    break;
                case 3:
                    tab.setIcon(android.R.drawable.ic_menu_compass);    // 网络计算器
                    break;
                case 4:
                    tab.setIcon(android.R.drawable.ic_menu_view);       // 网页
                    break;
            }
        }).attach();
    }
}
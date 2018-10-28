package com.example.shihab.smartattendance;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.shihab.smartattendance.adapter.ViewPagerAdapter;
import com.example.shihab.smartattendance.fragment.AttendanceFragment;
import com.example.shihab.smartattendance.fragment.CourseFragment;
import com.example.shihab.smartattendance.fragment.ProfileFragment;

public class FragmentCollectionActivity extends AppCompatActivity {
     Toolbar toolbar;
     TabLayout tabLayout;
     ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmant_collection);
        toolbar = findViewById(R.id.toolBar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        setSupportActionBar(toolbar);
        setDataOnViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setDataOnViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AttendanceFragment(),"ATTENDANCE");
        viewPagerAdapter.addFragment(new CourseFragment(),"COURSE");
        viewPagerAdapter.addFragment(new ProfileFragment(),"PROFILE");
        viewPager.setAdapter(viewPagerAdapter);


    }
}

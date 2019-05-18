package com.example.curieotask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.curieotask.Fragments.AllRecordingsFragment;
import com.example.curieotask.Fragments.RecordFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView username;
    final int REQUEST_PERMISSION_CODE = 1000;
    RecordFragment recordFragment = new RecordFragment();
    private List<Recordings> recordingsList =new ArrayList<>();
    private RecordingsAdapter recordingsAdapter;
    AllRecordingsFragment allRecordingsFragment = new AllRecordingsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordingsAdapter = new RecordingsAdapter(recordingsList,MainActivity.this);

        if(checkPermissionFromDevice()){

        }
        else{
            requestPermission();
        }


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new RecordFragment(),"Record");
        viewPagerAdapter.addFragment(new AllRecordingsFragment(),"All Recordings");


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {


                if(i == 0){
                  recordFragment.refreshAdapter();
                }
                else if(i == 1){
                   allRecordingsFragment.refreshAdapter();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<Fragment> fragments;
        public ArrayList<String> titles;
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }
    }
    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

}

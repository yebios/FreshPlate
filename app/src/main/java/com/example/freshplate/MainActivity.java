package com.example.freshplate;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1.找到导航栏
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        // 2.找到 NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        // 3. 拿到 NavController
        NavController navController = navHostFragment.getNavController();
        // 4. (关键!) 将底部导航栏和 NavController 自动连接起来
        NavigationUI.setupWithNavController(navView, navController);
    }
}
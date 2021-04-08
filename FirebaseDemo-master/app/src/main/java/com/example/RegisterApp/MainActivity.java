
package com.example.RegisterApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
//    public Toolbar toolbar;
//    public DrawerLayout drawerLayout;
//    public NavController navController;
//    public NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setupNavigationView();




    }
//    public void setupNavigationView(){
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    //    getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//
//        //for drawerlayout
//        drawerLayout = findViewById(R.id.mainDrawer);
//
//        //navigation view
//        navigationView = findViewById(R.id.navigationView);
//
//        navController = Navigation.findNavController(this, R.id.host_fragment);
//
////        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout);
//
//    //    NavigationUI.setupWithNavController(navigationView, navController);
//
//
////        navigationView.setNavigationItemSelectedListener(this);
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }else{
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.host_fragment), drawerLayout);
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        item.setCheckable(true);
//
//        drawerLayout.closeDrawers();
//
//        switch (item.getItemId()){
//
//            case R.id.logout:
//                navController.navigate(R.id.loginFragment);
//                break;
//
//            case R.id.profile:
//                FirebaseAuth.getInstance().signOut();
//                navController.navigate(R.id.loginFragment);
//
//                break;
//        }
//        return true;
//    }
}
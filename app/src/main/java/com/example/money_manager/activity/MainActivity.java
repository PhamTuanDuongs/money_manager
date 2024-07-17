package com.example.money_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.money_manager.R;
import com.example.money_manager.activity.authentication.LoginActivity;
import com.example.money_manager.ui.AddIncomeFragment;
import com.example.money_manager.ui.CreateCategoryFragment;
import com.example.money_manager.ui.ExpenseListFragment;
import com.example.money_manager.ui.IncomeListFragment;
import com.example.money_manager.ui.ListCategoryFragment;
import com.example.money_manager.ui.ListReminderFragment;
import com.example.money_manager.ui.ProfileFragment;
import com.example.money_manager.ui.ReportFragment;
import com.example.money_manager.utils.AccountState;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ImageView img_user_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_logins);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        img_user_profile = headerView.findViewById(R.id.img_user_profile);
        TextView txt_email = headerView.findViewById(R.id.txt_user_email);
        txt_email.setText(AccountState.getEmail(this, "email"));
        handleOnclideUserProfile();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new ReportFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            setToolbarTitle("Home");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectedFragment = new ReportFragment();
            setToolbarTitle("Home");
        } else if (id == R.id.nav_reminder) {
            selectedFragment = new ListReminderFragment();
            setToolbarTitle("Reminder");
        } else if (id == R.id.nav_income) {
            selectedFragment = new IncomeListFragment();
            setToolbarTitle("Income");
        } else if (id == R.id.nav_expense) {
            selectedFragment = new ExpenseListFragment();
            setToolbarTitle("Expense");
        } else if (id == R.id.nav_add_income) {
            selectedFragment = new AddIncomeFragment();
            setToolbarTitle("Add income");
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent myintent = new Intent(this, LoginActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_category) {
            selectedFragment = new ListCategoryFragment();
            setToolbarTitle("Category");
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, selectedFragment).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void handleOnclideUserProfile() {
        img_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, new ProfileFragment()).commit();
                setToolbarTitle("Profile");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }
}

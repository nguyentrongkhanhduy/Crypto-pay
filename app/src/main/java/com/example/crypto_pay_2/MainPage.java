package com.example.crypto_pay_2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainPage extends AppCompatActivity {

    private BottomNavigationView botNav;

//    Bundle extras = getIntent().getExtras();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main_page);

        Bundle extras = getIntent().getExtras();
        String my_email = "";
        if (extras!=null){
            my_email = extras.getString("email");
        }

        botNav = findViewById(R.id.bottom_navigation);
        botNav.setOnItemSelectedListener(bottomNavMethod);

        Fragment start_frag = new HomeFragment();
        Bundle data = new Bundle();
        data.putString("email",my_email);
        start_frag.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,start_frag).commit();
    }

    private BottomNavigationView.OnItemSelectedListener bottomNavMethod=new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            Bundle extras = getIntent().getExtras();
            String my_email = "";
            if (extras!=null){
                my_email = extras.getString("email");
            }

            switch (item.getItemId()){
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.history:
                    fragment = new HistoryFragment();
                    break;
                case R.id.wallet:
                    fragment = new WalletFragment();
                    break;
            }

            Bundle data = new Bundle();
            data.putString("email",my_email);
            fragment.setArguments(data);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            return true;
        }
    };

}
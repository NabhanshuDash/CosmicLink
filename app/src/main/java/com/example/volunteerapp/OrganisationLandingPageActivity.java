package com.example.volunteerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.volunteerapp.Fragment.VolBookmarkFragment;
import com.example.volunteerapp.Fragment.VolHomeFragment;
import com.example.volunteerapp.Fragment.VolMapFragment;
import com.example.volunteerapp.Fragment.VolProfileFragment;
import com.example.volunteerapp.databinding.ActivityOrganisationLandingPageBinding;
import com.example.volunteerapp.databinding.ActivityVolunteerLandingPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class OrganisationLandingPageActivity extends AppCompatActivity {

    ActivityOrganisationLandingPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Using view binding here
        binding = ActivityOrganisationLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        // Starting at Home fragment that is the feed
        replaceFragment(new VolHomeFragment());
        binding.bottomNavigationViewOrg.setBackground(null);

        binding.bottomNavigationViewOrg.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_vol) {
                replaceFragment(new VolHomeFragment());
            } else if (itemId == R.id.bookmark_vol) {
                replaceFragment(new VolBookmarkFragment());
            } else if (itemId == R.id.map) {
                replaceFragment(new VolMapFragment());
            } else if (itemId == R.id.profile_vol) {
                replaceFragment(new VolProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_org, fragment);
        fragmentTransaction.commit();
    }

}

package com.example.volunteerapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volunteerapp.Activities.LoginActivity;
import com.example.volunteerapp.Activities.qrCodeScanner;
import com.example.volunteerapp.Adapters.SearchAdapter;
import com.example.volunteerapp.Chat.Model.Users;
import com.example.volunteerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BfollowFragment extends Fragment {

    private FirebaseAuth auth;
    private RecyclerView searchRecyclerView;
    private SearchAdapter adapter;
    private FirebaseDatabase database;
    private ArrayList<Users> usersArrayList;
    private ImageButton qrCodeScan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bfollow, container, false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("Organisations");

        usersArrayList = new ArrayList<>();

        searchRecyclerView = view.findViewById(R.id.recyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SearchAdapter(getActivity(), usersArrayList);
        searchRecyclerView.setAdapter(adapter);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when the user submits the query (e.g., press search button)
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search as the user types (real-time search)
                performSearch(newText);
                return true;
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear(); // Clear the list before adding updated data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users != null) {
                        usersArrayList.add(users);
                        users.setFullname("Click to view Profile");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish(); // Finish this activity to prevent returning here after logging in
        }

        return view;
    }

    private void performSearch(String query) {
        DatabaseReference reference = database.getReference().child("Organisations");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot organizationSnapshot : snapshot.getChildren()) {
                    String organizationName = organizationSnapshot.getKey();

                    // Check if the organization name contains the search query
                    if (organizationName != null && organizationName.toLowerCase().contains(query.toLowerCase())) {
                        Log.d("Search", "Organization Name: " + organizationName);

                        // Extract organization details
                        String email = organizationSnapshot.child("email").getValue(String.class);
                        String fullname = "Click to view Profile";
                        String image_url = organizationSnapshot.child("image_url").getValue(String.class);
                        String userId = organizationSnapshot.child("userId").getValue(String.class);
                        String username = organizationSnapshot.child("username").getValue(String.class);

                        // Create a Users object with the organization details
                        Users users = new Users();
                        users.setFullname(fullname);
                        users.setEmail(email);
                        users.setImage_url(image_url);
                        users.setUserId(userId);
                        users.setUsername(username);

                        usersArrayList.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}

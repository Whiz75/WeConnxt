package com.example.weconnxt.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weconnxt.R;
import com.example.weconnxt.adapters.UsersAdapter;
import com.example.weconnxt.model.AppUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragments extends Fragment implements UsersAdapter.OnButtonsClickListener<QuerySnapshot>{

    ViewGroup friendsViewGroup;;
    RecyclerView recyclerView;
    List<AppUser> usersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       friendsViewGroup = (ViewGroup) inflater.inflate(R.layout.activity_friends_fragments,container,false);

       iniFriends(friendsViewGroup);
        populateUserRecycler(friendsViewGroup);

        return friendsViewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void iniFriends(final View view)
    {
        //final List<AppUser> friendsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.friends_rv);

        /*FirebaseDatabase.getInstance()
                .getReference()
                .child("TestUsers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ds : snapshot.getChildren()){
                                AppUser user = ds.getValue(AppUser.class);
                                user.setId(ds.getKey());
                                friendsList.add(user);
                            }
                            FriendsAdapter adapter = new FriendsAdapter(friendsList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

    }

    private void populateUserRecycler(final View view) {
        usersList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        String userid = FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .getUid();

        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .whereNotEqualTo("id", userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                AppUser users = snapshot.toObject(AppUser.class);
                                usersList.add(users);
                            }

                            Context context = view.getContext();

                            UsersAdapter usersAdapter = new UsersAdapter(usersList, context, this);
                            recyclerView.setAdapter(usersAdapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //show error
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSendRequestClick(int pos) {
        Toast.makeText(getActivity(), "you wanna send a request? at position: "+pos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIgnoreButtonClick(int pos) {

    }

   /* @Override
    public void sendRequest(int position)
    {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //start with the send request code
        //start with the send request code
        String uid = usersList.get(position).getId();

        HashMap<String, String> request = new HashMap<>();
        request.put("from", currentUid);
        request.put("to", uid);
        request.put("status", "pending");

        try
        {
            FirebaseFirestore
                    .getInstance()
                    .collection("Requests")
                    .document(currentUid)
                    .set(request)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getActivity(), "Request sent successfully!...", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
}

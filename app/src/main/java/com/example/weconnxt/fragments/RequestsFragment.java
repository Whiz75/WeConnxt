package com.example.weconnxt.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weconnxt.R;
import com.example.weconnxt.adapters.FriendRequestAdapter;
import com.example.weconnxt.model.AppUser;
import com.example.weconnxt.model.FriendRequestModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RequestsFragment extends Fragment implements FriendRequestAdapter.OnButtonsClickListener {

    private RecyclerView rv;
    private FriendRequestAdapter requestAdapter;
    private List<AppUser> requestsList;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        //set fragment context
        Context context = view.getContext();
        //call methods here
        init(view);
        populateRequest(view);

        return view;
    }

    private void init(View view)
    {
        rv = view.findViewById(R.id.requests_rv);
    }

    private void populateRequest(final View view)
    {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestsList = new ArrayList<>();

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FriendRequestModel model = new FriendRequestModel();
        //thima another test
        FirebaseFirestore
                .getInstance()
                .collection("Requests")
                .document(currentUser)
                .collection(model.getFrom())
                .document()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        Toast.makeText(view.getContext(), "request from"+model.getFrom(), Toast.LENGTH_SHORT).show();
                    }
                });

        //get user
        /*if (currentUser != null)
        {
            //get request form database
            FirebaseFirestore
                    .getInstance()
                    .collection("Requests")
                    .whereEqualTo("status","Pending")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot snapshot: task.getResult())
                                {
                                    FriendRequestModel model = snapshot.toObject(FriendRequestModel.class);

                                    //get the user that the request was sent to
                                    *//*FirebaseFirestore
                                            .getInstance()
                                            .collection("Users")
                                            .document(model.getTo())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(view.getContext(), "It comes here fam", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });*//*

                                    Toast.makeText(view.getContext(), "to :"+model.getTo(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }*/


    }

    @Override
    public void onAcceptButtonClick(int pos)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

        //start with the send request code
        //start with the send request code

        String uid = requestsList.get(pos).getId();

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
    }

    @Override
    public void onIgnoreButtonClick(int pos)
    {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String uid = requestsList.get(pos).getId();

        HashMap<String, String> request = new HashMap<>();
        request.put("from", currentUid);
        request.put("to", uid);
        request.put("status", "cancelled");

        FirebaseFirestore
                .getInstance()
                .collection("Requests")
                .document(currentUid)
                .set(request);
    }
}
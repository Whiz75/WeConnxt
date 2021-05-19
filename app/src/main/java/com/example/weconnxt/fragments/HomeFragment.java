package com.example.weconnxt.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/*import com.weconnect.we_connect.R;
import com.weconnect.we_connect.adapters.PostsAdapter;
import com.weconnect.we_connect.model.PostsModel;*/

import com.example.weconnxt.R;
import com.example.weconnxt.adapters.PostsAdapter;
import com.example.weconnxt.dialogs.AddPostDialogFragment;
import com.example.weconnxt.dialogs.PopUpCommentsDialog;
import com.example.weconnxt.model.AppUser;
import com.example.weconnxt.model.PostsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;


public class HomeFragment extends Fragment {


    private ArrayList<PostsModel> Items;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    int counter =0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab_new_post = (FloatingActionButton) view.findViewById(R.id.fab_add_post);

        fab_new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUser users = new AppUser("Thima" + counter , "email",null, null);
                //call to show post dialog
                ShowPostDialog(v);

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("TestUsers")
                        .push()
                        .setValue(users);
                counter++;
            }
        });
        ConnectViews(view);

        return view;
    }

    private void ConnectViews(View view) {

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.RecyclerPosts);
        final ArrayList<PostsModel> Items = new ArrayList<PostsModel>();
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        final PostsAdapter adapter = new PostsAdapter(Items, getChildFragmentManager());
        recycler.setAdapter(adapter);

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("Feeds")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value != null){
                                for (final DocumentChange dc : value.getDocumentChanges()){
                                    switch (dc.getType()) {
                                        case ADDED:
                                            Items.add(dc.getDocument().toObject(PostsModel.class));
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case MODIFIED:
                                            Items.set(dc.getOldIndex(), dc.getDocument().toObject(PostsModel.class));
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case REMOVED:
                                            //to remove item
                                            Items.remove(dc.getOldIndex());
                                            adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    });

        }
        catch (Exception ex){
            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowPostDialog(View view)
    {
        context = view.getContext();

        AddPostDialogFragment postDialogFragment = new AddPostDialogFragment();
        postDialogFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "post dialog");
    }
}
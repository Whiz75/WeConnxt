package com.example.weconnxt.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.weconnxt.R;
import com.example.weconnxt.adapters.CommentAdapter;
import com.example.weconnxt.model.CommentModel;
import com.example.weconnxt.model.PostsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class PopUpCommentsDialog extends DialogFragment {

    MaterialToolbar commentsToolbar;
    private EditText comment_et;
    private ImageView comment_post_iv;

    private RecyclerView comment_list;
    private List<CommentModel> commentsList;
    private CommentAdapter commentAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String blog_post_id;
    private String current_userId;

    private ArrayList<PostsModel> Items;

    Context context;

    public PopUpCommentsDialog() {
        // Required empty public constructor
    }

    String commentId;
    public PopUpCommentsDialog(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle);

        //initialize firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pop_up_comments_dialog, container, false);

        context = view.getContext();
        //call methods here
        init(view);
        commentsDialogToolBar(view);
        postComment(view);
        loadComments(view);

        return view;
    }

    private void init(View view)
    {
        commentsToolbar = view.findViewById(R.id.comment_toolbar);
        comment_et = view.findViewById(R.id.comment_field);
        comment_post_iv = view.findViewById(R.id.comment_post_btn);
        comment_list = view.findViewById(R.id.comment_list);
    }

    private void commentsDialogToolBar(View view)
    {
        context = view.getContext();

        commentsToolbar.setTitle("Comments");
        commentsToolbar.setNavigationIcon(R.drawable.ic_close);
        commentsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss the dialog
                dismiss();
            }
        });
    }

    private void postComment(final View view)
    {

        context = view.getContext();

        Items = new ArrayList<>();

        comment_post_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current_userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                //get current time for the comment
                String currentTime = new SimpleDateFormat("HH:mm:ss aa",Locale.getDefault()).format(new Date());
                String user_comment = comment_et.getText().toString().trim();

                final Map<String, Object> commentsMap = new HashMap<>();
                commentsMap.put("message", user_comment);
                commentsMap.put("user_id", current_userId);
                commentsMap.put("timestamp", FieldValue.serverTimestamp());
                commentsMap.put("comment_id", commentId);
                //Toast.makeText(getActivity(), commentId, Toast.LENGTH_LONG).show();

                //another try here
                FirebaseFirestore
                        .getInstance()
                        .collection("Feeds/" + commentId + "/Comments")
                        .add(commentsMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(final DocumentReference documentReference) {

                                Toast.makeText(getActivity(), "Comment successful!", Toast.LENGTH_LONG).show();
                                //clear the edit text after it is posted
                                comment_et.getText().clear();
                                documentReference.update("comment_id", documentReference.getId());

                                //get the date time stamp
                                //Date creationDate = document.getDate("creationDate");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    private void loadComments(View view)
    {
        context = view.getContext();
        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentsList);

        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        comment_list.setAdapter(commentAdapter);

        //.collection("Feeds/" + commentId + "/Comments")
        Query query = firebaseFirestore
                .collection("Feeds/" + commentId + "/Comments")
                .orderBy("timestamp", Query.Direction.ASCENDING);

        query.addSnapshotListener(Objects.requireNonNull(getActivity()), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(documentSnapshots != null){
                    Log.d("zzzzzzzz", documentSnapshots.getDocuments().toString());
                    for (final DocumentChange dc : documentSnapshots.getDocumentChanges()){
                        switch (dc.getType()) {
                            case ADDED:
                                try
                                {
                                    CommentModel mc = dc.getDocument().toObject(CommentModel.class);
                                    commentsList.add(mc);
                                    commentAdapter.notifyDataSetChanged();

                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                break;
                            case MODIFIED:
                                break;
                            case REMOVED:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    if(commentsList.removeIf(new Predicate<CommentModel>() {
                                        @Override
                                        public boolean test(CommentModel post) {
                                            return post.getComment_id().contains(dc.getDocument().getId());
                                        }
                                    }))
                                    {
                                        //adapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                        }
                    }
                }else
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
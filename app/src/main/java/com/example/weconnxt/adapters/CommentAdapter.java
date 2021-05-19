package com.example.weconnxt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weconnxt.R;
import com.example.weconnxt.model.AppUser;
import com.example.weconnxt.model.CommentModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;
import java.util.Objects;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public List<CommentModel> commentsList;
    public Context context;
    private FirebaseAuth mAuth;

    public CommentAdapter(List<CommentModel> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_row, parent, false);

        context = parent.getContext();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);

        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(commentsList.get(position).getUser_id())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value != null)
                        {
                            AppUser user = value.toObject(AppUser.class);
                            holder.setUserData(String.format("%s %s", Objects.requireNonNull(user).getName(), user.getLastName()), null);
                        }else
                        {
                            Toast.makeText(context.getApplicationContext(), "value is empty", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private final View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //row view
            mView = itemView;
        }

        public void setComment_message(String message){

            TextView comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);
        }

        public void setComment_time(String time){

            //Date creationDate = document.getDate("creationDate");
            TextView comment_time = mView.findViewById(R.id.comment_time);
            comment_time.setText(time);
        }

        public void setUserData(String fullNames, String profileImageUrl) {

            ImageView comment_image = mView.findViewById(R.id.comment_image);
            TextView comment_username = mView.findViewById(R.id.comment_username);

            comment_username.setText(fullNames);

            //load the profile image
           /* Picasso
                    .get()
                    .load(profileImageUrl)
                    .centerCrop()
                    .into(comment_image);*/
        }
    }

}

package com.example.weconnxt.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weconnxt.R;
import com.example.weconnxt.dialogs.PopUpCommentsDialog;

import com.example.weconnxt.model.PostsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder>  {

    private ArrayList<PostsModel> Items = new ArrayList<PostsModel>();

    public PostsAdapter(ArrayList<PostsModel> items, FragmentManager fragmentTransaction) {
        Items = items;
        Fm = fragmentTransaction;
    }

    private FragmentManager Fm;
    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_post_row, parent, false);
        return new PostsViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsViewHolder holder, final int position) {
        holder.Row_Video.setVisibility(View.GONE);
        holder.Row_DeletePost.setVisibility(View.GONE);
        holder.Row_EditPost.setVisibility(View.GONE);

        holder.Row_TxtPostMessage.setText(Items.get(position).getMessage());
        holder.Row_DateTime.setText(Items.get(position).getDate_posted());


        if(Items.get(position).getUrl() != null) {
            Picasso.get()
                    .load(Items.get(position).getUrl())
                    .resize(150, 150)
                    .into(holder.Row_PostImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.Row_PostImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });


        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*CommentsDialogFragment dlg = new CommentsDialogFragment(Items.get(position).getId());
                FragmentTransaction ft = Fm.beginTransaction();
                dlg.show(ft, "Comments");*/
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return Items.size();
    }

    private View mView;
    public class PostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatImageView Row_SenderProfile;
        public AppCompatImageView Row_PostImage;
        public AppCompatImageView Row_DeletePost;
        public AppCompatImageView Row_EditPost;
        public MaterialTextView Row_TxtSenderName;
        public MaterialTextView Row_TxtPostMessage;
        public VideoView Row_Video;
        public AppCompatImageView Row_CommentImg;
        public MaterialTextView Row_TxtComment_Count;
        public AppCompatImageView Row_LikeImg;
        public MaterialTextView Row_Like_Count;
        public MaterialTextView Row_DateTime;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            Row_PostImage = itemView.findViewById(R.id.row_post_img);
            Row_SenderProfile = itemView.findViewById(R.id.row_post_user_profile);
            Row_DeletePost = itemView.findViewById(R.id.row_post_delete);
            Row_EditPost = itemView.findViewById(R.id.row_post_edit);
            Row_CommentImg = itemView.findViewById(R.id.row_post_comment_img);
            Row_LikeImg = itemView.findViewById(R.id.row_post_like_img);
            Row_Video =  itemView.findViewById(R.id.row_post_video);

            Row_TxtPostMessage =  itemView.findViewById(R.id.row_post_caption);
            Row_TxtSenderName =  itemView.findViewById(R.id.row_post_sender_name);
            Row_TxtComment_Count =  itemView.findViewById(R.id.row_post_count_comments);
            Row_Like_Count = itemView.findViewById(R.id.row_post_count_likes);
            Row_DateTime =  itemView.findViewById(R.id.row_post_date_and_time);

            Row_CommentImg.setOnClickListener(this);
            Row_LikeImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();

            if (v.getId() == Row_CommentImg.getId())
            {

                //clickListener.onButtonClick(Items.get(pos).getId(), pos);
                Toast.makeText(mView.getContext(), Items.get(pos).getId(),Toast.LENGTH_LONG).show();

                PopUpCommentsDialog dlg = new PopUpCommentsDialog(Items.get(pos).getId());
                FragmentTransaction ft = Fm.beginTransaction();
                dlg.show(ft, "Comments");
            }

            String postId = Items.get(pos).getId();

            if (v.getId() == Row_LikeImg.getId())
            {
                //final int pos = getAdapterPosition();
                try
                {
                    //test this 1
                    postLikes(pos);

                    FirebaseFirestore
                            .getInstance()
                            .collection("Posts/" + postId + "/Likes")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                                    if (!value.isEmpty()) {

                                        int counter = value.size();
                                        Row_Like_Count.setText(Integer.toString(counter));

                                    } else {
                                        Row_Like_Count.setText(" ");
                                    }
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void postLikes(final int pos)
    {
        FirebaseFirestore
                .getInstance()
                .collection("Feeds/" + Items.get(pos).getId() + "/Likes")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            FirebaseFirestore
                                    .getInstance()
                                    .collection("Feeds/" + Items.get(pos).getId() + "/Likes").document(FirebaseAuth.getInstance().getUid())
                                    .set(likesMap);

                            Toast.makeText(mView.getContext(), "liked", Toast.LENGTH_LONG).show();

                        } else {
                            FirebaseFirestore
                                    .getInstance()
                                    .collection("Feeds/" + Items.get(pos).getId() + "/Likes").document(FirebaseAuth.getInstance().getUid())
                                    .delete();

                            Toast.makeText(mView.getContext(), "Unliked", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void listenToLikes(int position)
    {
    }
}

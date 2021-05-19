package com.example.weconnxt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weconnxt.R;
import com.example.weconnxt.model.AppUser;
import com.example.weconnxt.model.FriendRequestModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    List<AppUser> usersList;
    Context context;
    OnCompleteListener<QuerySnapshot> listener;

    public UsersAdapter(List<AppUser> usersList, Context context, OnCompleteListener<QuerySnapshot> listener) {
        this.usersList = usersList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_friend_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppUser mdl = usersList.get(position);
        holder.username.setText(mdl.getName());
        //holder.add_friend_btn.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MaterialTextView username;
        MaterialButton add_friend_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_row_username);
            add_friend_btn = itemView.findViewById(R.id.row_add_friend_btn);

            add_friend_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (v.getId() == add_friend_btn.getId())
            {
                int pos = getAdapterPosition();

                final AppUser user = usersList.get(pos);
                final FriendRequestModel model = new FriendRequestModel();

                final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (model.getStatus().contains("Pending"))
                {
                    add_friend_btn.setEnabled(false);
                }

                //start with the send request code
                model.setFrom(currentUid);
                model.setTo(user.getId());
                model.setStatus("Pending");

                final HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("status","Pending");
                hashMap.put("id",user.getId());

                try
                {
                    //first
                    FirebaseFirestore
                            .getInstance()
                            .collection("Requests")
                            .document(user.getId())
                            .collection(currentUid)
                            .add(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    Toast.makeText(v.getContext(), "friend request was sent to "+model.getTo(), Toast.LENGTH_LONG).show();
                                    //another
                                    /*FirebaseFirestore
                                            .getInstance()
                                            .collection("Requests")
                                            .document(currentUid)
                                            .collection(user.getId())
                                            .add(hashMap);*/
                                }
                            });

                    Toast.makeText(v.getContext(), "Attempted to send a request at position :", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*set(hashMap.put("status","Pending"))
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(v.getContext(), "Requsets sent successfully",Toast.LENGTH_LONG).show();

                                add_friend_btn.setText("sent");
                                add_friend_btn.setEnabled(false);
                            }
                        });*/
            }
        }
    }

    public interface OnButtonsClickListener<Q> {
        void onSendRequestClick(int pos);
        void onIgnoreButtonClick(int pos);
    }

}

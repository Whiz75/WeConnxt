package com.example.weconnxt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weconnxt.R;
import com.example.weconnxt.model.AppUser;
import com.example.weconnxt.model.FriendsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    List<AppUser> friendsList = new ArrayList<>();

    public FriendsAdapter(List<AppUser> friendsList) {
        this.friendsList = friendsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtUsername,txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUsername = (TextView)itemView.findViewById(R.id.friend_username);
            txtStatus = (TextView)itemView.findViewById(R.id.friend_status);
        }
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_friend_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendsAdapter.ViewHolder holder, final int position) {


        holder.txtUsername.setText(friendsList.get(position).name);
        holder.txtStatus.setText("oFFLINE");

        if(friendsList.get(position).getId().contains("-MWF5iu9-dv9p0JAPWxR")){
            Toast.makeText(holder.itemView.getContext(), friendsList.get(position).getId(), Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Online")
                    .child(friendsList.get(position).id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                if(snapshot.child("Last_Seen").exists()){
                                    SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                    String d = dateFormat.format(Calendar.getInstance().getTime());

                                    if(d.contains(snapshot.child("Last_Seen").getValue().toString()))
                                    {
                                        holder.txtStatus.setText("Online");
                                    }
                                    else{
                                        holder.txtStatus.setText(String.format("Lats Seen :%s", snapshot.child("Last_Seen").getValue().toString()));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}

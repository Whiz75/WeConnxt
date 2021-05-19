package com.example.weconnxt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weconnxt.R;
import com.example.weconnxt.model.AppUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    List<AppUser> UserModels;
    private OnCompleteListener clickListener;
    private OnCompleteListener<QuerySnapshot> listener;

    public FriendRequestAdapter(List<AppUser> UserModels, OnCompleteListener<QuerySnapshot> listener)
    {
        this.UserModels = UserModels;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtname;
        private MaterialButton accept_btn , ignore_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtname = (TextView) itemView.findViewById(R.id.request_username);
            accept_btn = itemView.findViewById(R.id.request_accept_button);
            ignore_btn = itemView.findViewById(R.id.request_ignore_button);

            accept_btn.setOnClickListener(this);
            ignore_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();

            if (v.getId() == accept_btn.getId())
            {
                /*clickListener.onAcceptButtonClick(pos);*/

            }

            if (v.getId() == ignore_btn.getId())
            {
                /*clickListener.onIgnoreButtonClick(pos);*/
            }
        }
    }

    @NonNull
    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friend_request, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.ViewHolder holder, int position) {

        AppUser user = UserModels.get(position);
        holder.txtname.setText(user.getName());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return UserModels.size();
    }

    public interface OnButtonsClickListener
    {
        void onAcceptButtonClick(int pos);
        void onIgnoreButtonClick(int pos);
    }

    public interface OnClickListener<QuerySnapshot>
    {
        void onAcceptButtonClick(int pos);
        void onIgnoreButtonClick(int pos);
    }
}

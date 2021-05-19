package com.example.weconnxt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weconnxt.R;
import com.example.weconnxt.model.ChatsModel;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ChatsModel> items =  new ArrayList<>();

    public ChatsAdapter(ArrayList<ChatsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == R.layout.row_recieved)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recieved, parent, false);

            return new MessageReceivedViewHolder(view);

        }else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent, parent, false);

            return new MessageSentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == R.layout.row_sent){

            MessageSentViewHolder v_type = (MessageSentViewHolder) holder;

            v_type.row_sent_message.setText(items.get(position).getMessage());
            v_type.row_sent_name.setText(items.get(position).getSenderId());
        }
        else{
            MessageReceivedViewHolder v_type = (MessageReceivedViewHolder) holder;
            v_type.row_received_message.setText(items.get(position).getMessage());
            v_type.row_received_message.setText(items.get(position).getSenderId());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2 == 0)
        {
            return R.layout.row_recieved;
        }
        else {
            return R.layout.row_sent;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

class MessageSentViewHolder extends RecyclerView.ViewHolder{

    public MaterialTextView row_sent_name;
    public MaterialTextView row_sent_message;

    public MessageSentViewHolder(@NonNull View itemView) {
        super(itemView);
        row_sent_name = (MaterialTextView)itemView.findViewById(R.id.row_sent_name);
        row_sent_message = (MaterialTextView)itemView.findViewById(R.id.row_sent_message);
    }
}
class MessageReceivedViewHolder extends RecyclerView.ViewHolder {
    public MaterialTextView row_received_name;
    public MaterialTextView row_received_message;

    public MessageReceivedViewHolder(@NonNull View itemView) {
        super(itemView);
        row_received_name = (MaterialTextView) itemView.findViewById(R.id.row_received_name);
        row_received_message = (MaterialTextView) itemView.findViewById(R.id.row_received_message);
    }
}

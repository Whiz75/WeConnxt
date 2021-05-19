package com.example.weconnxt.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.weconnxt.R;
import com.example.weconnxt.adapters.ChatsAdapter;
import com.example.weconnxt.model.ChatsModel;

import java.util.ArrayList;

public class ChatFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View chatView = LayoutInflater.from(getContext()).inflate(R.layout.activity_chat_fragment,container,false);

        populateChats(chatView);
        return chatView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void populateChats(View view)
    {
        ChatsAdapter chatsAdapter;
        ArrayList<ChatsModel> chatsLists = new ArrayList<>();
        RecyclerView chatsRecyclerView = (RecyclerView) view.findViewById(R.id.chats_rv);

        try
        {
            for(int i=0; i<25;i++)
            {
                ChatsModel mdl = new ChatsModel("greatings", "Hello", null);
                chatsLists.add(mdl);
            }

            //ChatsAdapter adapter = new ChatsAdapter(chatsLists);
            chatsAdapter = new ChatsAdapter(chatsLists);
            chatsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            chatsRecyclerView.setAdapter(chatsAdapter);

        } catch (Exception e)
        {
            Toast.makeText(view.getContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}

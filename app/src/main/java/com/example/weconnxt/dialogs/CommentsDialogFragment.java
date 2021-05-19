package com.example.weconnxt.dialogs;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weconnxt.R;
import com.example.weconnxt.adapters.CommentsAdapter;
import com.example.weconnxt.model.CommentsModel;
import com.google.android.material.appbar.MaterialToolbar;
/*import com.weconnect.we_connect.R;
import com.weconnect.we_connect.adapters.CommentsAdapter;
import com.weconnect.we_connect.model.CommentsModel;*/

import java.util.ArrayList;

public class CommentsDialogFragment extends DialogFragment {


    String commentId;
    public CommentsDialogFragment(String commentId) {
        // Required empty public constructor
        this.commentId = commentId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments_dialog, container, false);
        ConnectViews(view);
        return view;
    }

    private final ArrayList<CommentsModel> Items = new ArrayList<>();
    private void ConnectViews(View view) {
        RecyclerView recycler = (RecyclerView)view.findViewById(R.id.RecyclerComments);
        MaterialToolbar toolbar = (MaterialToolbar)view.findViewById(R.id.toolbar_comments);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String [] cmnts = {"Supp", "Good", "yuo", "Cool", "Bingo", "Tap", "More", "options","from","your", "conversation", "list",
                "and", "select", "Messages", "for", "web"};
        for (int i = 0;i<cmnts.length;i++){

            Items.add(new CommentsModel(String.valueOf(i),cmnts[i],String.valueOf(i)));
        }
        CommentsAdapter adapter = new CommentsAdapter(Items);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
    }
}
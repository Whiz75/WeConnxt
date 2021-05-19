package com.example.weconnxt.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weconnxt.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ConnectView(view);
        return view;
    }

    private void ConnectView(View view) {

        TabLayout tabHost = (TabLayout) view.findViewById(R.id.TabHost);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.ProfileViewPager);
        PagerHolderAdapter adapter = new PagerHolderAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.LoadFragments(new ProfileTabFrag(), "Me");
        adapter.LoadFragments(new PostsTabFrag(), "POSTS");
        adapter.LoadFragments(new FriendsTabFrag(), "FRIENDS");



        tabHost.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPager.getAdapter().notifyDataSetChanged();
    }
}
class PagerHolderAdapter extends FragmentPagerAdapter{
    List<Fragment> frags = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    public PagerHolderAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    public void LoadFragments(Fragment f, String t){
        frags.add(f);
        titles.add(t);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return frags.size();
    }
}
package com.liaudev.dicodingbpaa.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.databinding.FragmentHomeBinding;
import com.liaudev.dicodingbpaa.ui.adapters.AdapterStory;
import com.liaudev.dicodingbpaa.ui.adapters.StoryLoadAdapter;
import com.liaudev.dicodingbpaa.ui.post.PostActivity;
import com.liaudev.dicodingbpaa.viewmodel.ViewModelFactory;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private long lastBackPressed;
    private  AdapterStory adapterStory;
    @SuppressLint("UnsafeOptInUsageError")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity());
        HomeViewModel homeViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) factory).get(HomeViewModel.class);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (lastBackPressed + 1000 > System.currentTimeMillis()) {
                    getActivity().finish();
                    return;
                }
                lastBackPressed = System.currentTimeMillis();
                Toast.makeText(getActivity(), R.string.exit_press_back_twice_message, Toast.LENGTH_SHORT).show();
            }
        };
    
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        adapterStory = new AdapterStory();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapterStory.withLoadStateFooter(new StoryLoadAdapter((v) -> {
            adapterStory.retry();
        })));
        
        binding.swipeRefresh.setOnRefreshListener(() -> {
            if(binding.layoutError.getVisibility() == View.VISIBLE){
                binding.layoutError.setVisibility(View.GONE);
            }
            adapterStory.refresh();
        });
        binding.fabPost.setOnClickListener((v)->{
            myActivityResultLauncher.launch(new Intent(getActivity(), PostActivity.class));
        });

        binding.btnRefresh.setOnClickListener((v)->{
            binding.swipeRefresh.setRefreshing(true);
            binding.layoutError.setVisibility(View.GONE);
            adapterStory.refresh();
        });

        homeViewModel.getStories().observe(getViewLifecycleOwner(),(storyEntityPagingData -> {
            if(binding.swipeRefresh.isRefreshing()){
                binding.swipeRefresh.setRefreshing(false);
            }
            adapterStory.submitData(getLifecycle(), storyEntityPagingData);
        }));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * refresh data when success from PostActivity
     */
    private final ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            adapterStory.refresh();
        }
    }));
}
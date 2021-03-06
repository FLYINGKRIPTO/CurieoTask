package com.example.curieotask.Fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.curieotask.NameAddEvent;
import com.example.curieotask.R;
import com.example.curieotask.Recordings;
import com.example.curieotask.RecordingsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AllRecordingsFragment extends Fragment {
    String pathsave = "";
    String FILENAME = "cureio";

    EventBus eventBus = EventBus.getDefault();

    File file;
    private List<Recordings> recordingsList =new ArrayList<>();
    private RecyclerView recyclerView;
    private RecordingsAdapter recordingsAdapter;
    private static final String TAG = "AllRecordingsFragment";

    public AllRecordingsFragment() {
        // Required empty public constructor
      
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            eventBus.register(this);

    }


    @Subscribe
    public  void onEvent(NameAddEvent event){
        Log.d(TAG, "onEvent: Name add event "+ event.newText);
        Recordings recordings = new Recordings(event.newText);
        fetchRecordings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: onCreateView is Called ");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_recordings, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recordingsAdapter = new RecordingsAdapter(recordingsList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recordingsAdapter);

       ArrayList<String> allRecordings =  fetchRecordings();
       Log.d(TAG, "onCreateView: All Recordings "+ allRecordings);



        return view;
    }

    private ArrayList<String> fetchRecordings() {
        recordingsList.clear();
        ArrayList<String> filename = new ArrayList<String>();
        pathsave = Environment.getExternalStorageDirectory() +File.separator +"curieo";
        File directory = new File(pathsave);
        File[] files = directory.listFiles();

        for(int i = 0; i<files.length; i++){
            String file_name = files[i].getName();
            filename.add(file_name);
            Recordings recordings = new Recordings(file_name);
            recordingsList.add(recordings);
            recordingsAdapter.notifyDataSetChanged();
        }

        return filename;
    }


}

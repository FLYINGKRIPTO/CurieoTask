package com.example.curieotask.Fragments;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.curieotask.NameAddEvent;
import com.example.curieotask.R;
import com.example.curieotask.Recordings;
import com.example.curieotask.RecordingsAdapter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;


public class RecordFragment extends Fragment {


    Button startbtn,stopbtn,play,pause;
    MediaRecorder mediaRecorder;
    TextView txt;
    MediaPlayer mediaPlayer;
    String pathsave = "";
    String FILENAME = "cureio";
    boolean pauseIsClicked = false;
    int pauseCurrentPosition ;
    File file;

    RecordingAdded mCallback;



    private List<Recordings> recordingsList =new ArrayList<>();
    private RecyclerView recyclerView;
    private RecordingsAdapter recordingsAdapter;

    public interface RecordingAdded{

       public void sendRecording(String recordName);

    }




    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (RecordingAdded) activity;

        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+ " must implement TextClicked");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: onCreateView is Called ");
        View view =  inflater.inflate(R.layout.fragment_record, container, false);

        startbtn =view.findViewById(R.id.start_record);
        stopbtn = view.findViewById(R.id.stop_record);
        play = view.findViewById(R.id.play);
        txt = view.findViewById(R.id.text);
        pause = view.findViewById(R.id.pause);
        recyclerView = view.findViewById(R.id.recycler_view);
        recordingsAdapter = new RecordingsAdapter(recordingsList,getContext());


      //  ArrayList<String> allRecordings =  fetchRecordings();
       // Log.d(TAG, "onCreateView: All Recordings "+ allRecordings);


        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathsave = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(pathsave +"/curieo");
                if(!myDir.exists()){
                    myDir.mkdirs();
                }
                Random generator = new Random();
                int n = 100;
                n = generator.nextInt(n);

                String fname = "Curieo"+n+".3gpp";
                 file = new File(myDir,fname);
                if(file.exists())
                    file.delete();
                try{

                    FileOutputStream fstream = new FileOutputStream(file);
                    fstream.write(fname.getBytes());
                    fstream.close();
                }
                catch (Exception e){
                      e.printStackTrace();
                }

                setUpMediaRecorder();
                try{
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    txt.setText("RECORDING......");
                }
                catch (IOException e){

                }
                startbtn.setEnabled(false);
                stopbtn.setEnabled(true);
                play.setEnabled(false);
                pause.setEnabled(false);
              //  fetchRecordings();

                EventBus bus = EventBus.getDefault();
                bus.post(new NameAddEvent(fname));


          //      Toast.makeText(this,"RECORDING....",Toast.LENGTH_SHORT).show();
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                mediaRecorder.release();
                txt.setText("RECORDING STOPPED");
                stopbtn.setEnabled(false);
                play.setEnabled(true);
                startbtn.setEnabled(true);
                pause.setEnabled(false);

                recordingsAdapter.notifyDataSetChanged();

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setEnabled(true);
                stopbtn.setEnabled(false);
                startbtn.setEnabled(false);

                if(pauseIsClicked){
                    mediaPlayer = new MediaPlayer();
                    try{
                        mediaPlayer.setDataSource(String.valueOf(file));
                        mediaPlayer.prepare();

                    }
                    catch (IOException e){

                    }
                    mediaPlayer.seekTo(pauseCurrentPosition);
                    mediaPlayer.start();
                    txt.setText("RECORDING PLAYING");



                    pauseIsClicked = false;
                }

                else {
                    mediaPlayer = new MediaPlayer();
                    try{
                        mediaPlayer.setDataSource(String.valueOf(file));
                        mediaPlayer.prepare();

                    }
                    catch (IOException e){

                    }
                    mediaPlayer.start();
                }


                txt.setText("RECORDING PLAYING");
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        pause.setEnabled(true);
                        stopbtn.setEnabled(true);
                        startbtn.setEnabled(true);
                    }
                });

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopbtn.setEnabled(false);
                startbtn.setEnabled(true);
                pause.setEnabled(false);
                play.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.getCurrentPosition();
                    pauseIsClicked = true;
                    pauseCurrentPosition = mediaPlayer.getCurrentPosition();
                    Log.d(TAG, "onClick: current position "+ mediaPlayer.getCurrentPosition());
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    txt.setText("RECORDING PAUSED");

                    setUpMediaRecorder();
                }
            }
        });


        return view;
    }

    private void setUpMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecorder.setOutputFile(file);
        }
        else
            mediaRecorder.setOutputFile(pathsave);
    }
    private ArrayList<String> fetchRecordings() {
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

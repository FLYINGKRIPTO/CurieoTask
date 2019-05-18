package com.example.curieotask;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.MyViewHolder> {

    private Context mContext;
   public List<Recordings> recordingsList;
    private static String mFileName = null;
    boolean pauseIsClicked = false;
    int pauseCurrentPos ;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recordingName;
        public Button play,pause,stop;

        private MediaPlayer mPlayer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recordingName = itemView.findViewById(R.id.recording_name);
            play = itemView.findViewById(R.id.play);
            pause = itemView.findViewById(R.id.pause);
            stop = itemView.findViewById(R.id.stop);





        }
    }

    public RecordingsAdapter(List<Recordings> recordings,Context mContext){
        this.recordingsList = recordings;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recording_list_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.mPlayer = new MediaPlayer();
        final Recordings recordings = recordingsList.get(i);
        myViewHolder.recordingName.setText(recordings.getName());
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/curieo";

        myViewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onBindViewHolder play Button clicked on position "+ i+ " name "+ recordings.getName());

                if(pauseIsClicked){
                    try
                    {
                        myViewHolder.mPlayer = new MediaPlayer();
                        myViewHolder.mPlayer.setDataSource(mFileName+"/"+recordings.getName());
                        Log.d(TAG, "onClick: location"+mFileName+"/"+recordings.getName());
                        myViewHolder.mPlayer.prepare();
                        myViewHolder.mPlayer.seekTo(pauseCurrentPos);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    myViewHolder.mPlayer.start();
                    pauseIsClicked = false;
                }
                else{
                    try
                    {
                        myViewHolder.mPlayer = new MediaPlayer();
                        myViewHolder.mPlayer.setDataSource(mFileName+"/"+recordings.getName());
                        Log.d(TAG, "onClick: location"+mFileName+"/"+recordings.getName());
                        myViewHolder.mPlayer.prepare();

                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }
                    myViewHolder.mPlayer.start();

                }


            }

        });
        myViewHolder.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewHolder.mPlayer != null){
                    myViewHolder.mPlayer.getCurrentPosition();
                    myViewHolder.mPlayer.stop();
                    myViewHolder.mPlayer.release();


                }
            }
        });

        myViewHolder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myViewHolder.mPlayer != null){
                    Log.d(TAG, "onClick: current position"+ myViewHolder.mPlayer.getCurrentPosition());
                    pauseIsClicked = true;
                    pauseCurrentPos = myViewHolder.mPlayer.getCurrentPosition();
                    myViewHolder.mPlayer.stop();
                    myViewHolder.mPlayer.release();

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return recordingsList.size();
    }

}

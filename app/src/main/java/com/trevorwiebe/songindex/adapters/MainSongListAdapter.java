package com.trevorwiebe.songindex.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trevorwiebe.songindex.R;
import com.trevorwiebe.songindex.objects.Song;

import java.util.ArrayList;

/**
 * Created by thisi on 10/28/2018.
 */

public class MainSongListAdapter extends RecyclerView.Adapter<MainSongListAdapter.MainListViewHolder> {

    private ArrayList<Song> mSongList;
    private Context mContext;

    public MainSongListAdapter(ArrayList<Song> songsList, Context context){
        mSongList = songsList;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        if(mSongList == null)return 0;
        return mSongList.size();
    }

    @NonNull
    @Override
    public MainListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_main_song_list, viewGroup, false);
        return new MainListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListViewHolder mainListViewHolder, int i) {

        Song song = mSongList.get(i);
        String songName = song.getName();
        String poetName = song.getPoet();
        String composerName = song.getComposer();

        mainListViewHolder.mMainSongView.setText(songName);
        mainListViewHolder.mMainPoet.setText(poetName);
        mainListViewHolder.mMainComposer.setText(composerName);

    }

    public void swapData(Context context, ArrayList<Song> songList){
        if(songList != null){
            mContext = context;
            mSongList = songList;
            notifyDataSetChanged();
        }
    }

    public class MainListViewHolder extends RecyclerView.ViewHolder {

        private TextView mMainSongView;
        private TextView mMainPoet;
        private TextView mMainComposer;

        public MainListViewHolder(View view){
            super(view);

            mMainSongView = view.findViewById(R.id.main_song_name);
            mMainPoet = view.findViewById(R.id.main_poet_name);
            mMainComposer = view.findViewById(R.id.main_composer_name);
        }

    }

}

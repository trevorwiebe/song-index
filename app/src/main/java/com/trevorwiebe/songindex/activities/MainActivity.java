package com.trevorwiebe.songindex.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trevorwiebe.songindex.R;
import com.trevorwiebe.songindex.adapters.MainSongListAdapter;
import com.trevorwiebe.songindex.objects.Song;
import com.trevorwiebe.songindex.utils.ItemClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mMainRef = FirebaseDatabase.getInstance().getReference("songs");
    private RecyclerView mMainSongList;
    private MainSongListAdapter mMainSongListAdapter;
    private ArrayList<Song> mMainSongArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainSongList = findViewById(R.id.main_song_list);
        mMainSongList.setLayoutManager(new LinearLayoutManager(this));
        mMainSongListAdapter = new MainSongListAdapter(null, this);
        mMainSongList.setAdapter(mMainSongListAdapter);

        mMainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Song song = snapshot.getValue(Song.class);
                    mMainSongArrayList.add(song);
                }
                mMainSongListAdapter.swapData(MainActivity.this, mMainSongArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMainSongList.addOnItemTouchListener(new ItemClickListener(this, mMainSongList, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_new_song_menu) {
            Intent add_new_song_intent = new Intent(MainActivity.this, AddSongActivity.class);
            startActivity(add_new_song_intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

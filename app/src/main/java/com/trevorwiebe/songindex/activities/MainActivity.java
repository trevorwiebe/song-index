    package com.trevorwiebe.songindex.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.trevorwiebe.songindex.R;

    public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            if(item.getItemId() == R.id.add_new_song_menu){
                Intent add_new_song_intent = new Intent(MainActivity.this, AddSongActivity.class);
                startActivity(add_new_song_intent);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

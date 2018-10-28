package com.trevorwiebe.songindex.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trevorwiebe.songindex.R;
import com.trevorwiebe.songindex.objects.Song;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

public class AddSongActivity extends AppCompatActivity {

    // Declare view variables
    private EditText mSongName;
    private EditText mPoetName;
    private EditText mComposerName;
    private CheckBox isSongCopyrighted;
    private ImageView mViewSongBeforeUploading;
    private Button mUploadSong;

    private static final int PICK_SONG_PIC_REQUEST_CODE = 1;
    private Uri mSelectedUri;
    private StorageReference mBaseRef = FirebaseStorage.getInstance().getReference();
    private StorageReference mStorageReference;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("songs");

    private static final String TAG = "AddSongActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        mSongName = findViewById(R.id.song_name);
        mPoetName = findViewById(R.id.poet_name);
        mComposerName = findViewById(R.id.composer_name);
        isSongCopyrighted = findViewById(R.id.is_copyrighted_text);
        mViewSongBeforeUploading = findViewById(R.id.view_song_before_uploading);
        mUploadSong = findViewById(R.id.upload_song_btn);
    }

    public void selectSongToUpload(View view){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , PICK_SONG_PIC_REQUEST_CODE);
    }

    public void uploadSong(View view){

        // Check if the fields are filled with data
        if(mSongName.length() == 0 || mPoetName.length() == 0 || mComposerName.length() == 0 || mViewSongBeforeUploading.getDrawable() == null){
            Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show uploading dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        final View pb = inflater.inflate(R.layout.uploading_song_dialog_loader, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Uploading song")
                .setCancelable(false)
                .setView(pb);
        dialog.show();

        // Deactivate upload button
        mUploadSong.setEnabled(false);
        mUploadSong.setBackgroundColor(getResources().getColor(R.color.colorAccentDeactivated));

        // Compress image into a byte array
        mViewSongBeforeUploading.setDrawingCacheEnabled(true);
        mViewSongBeforeUploading.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) mViewSongBeforeUploading.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Set the meta data
        StorageMetadata metaData = new StorageMetadata.Builder()
                .setContentType(mSongName.getText().toString())
                .build();

        // Initialize the storage reference
        mStorageReference = mBaseRef.child("songs/" + UUID.randomUUID().toString());

        // Begin the actual upload of the image
        final UploadTask uploadTask = mStorageReference.putBytes(data, metaData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSongActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                mStorageReference = null;
                mUploadSong.setEnabled(true);
                mUploadSong.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                AlertDialog alertDialog = dialog.create();
                alertDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                DatabaseReference pushRef = database.push();
                String songKey = pushRef.getKey();
                Song song = new Song(mSongName.getText().toString(), mPoetName.getText().toString(), mComposerName.getText().toString(), mSelectedUri.toString(), isSongCopyrighted.isChecked(), System.currentTimeMillis(), songKey);
                pushRef.setValue(song);

                Toast.makeText(AddSongActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                mStorageReference = null;
                finish();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int progressInt = (int) progress;
                    ProgressBar progressBar = pb.findViewById(R.id.uploading_song_pb);
                    progressBar.setProgress(progressInt, true);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == PICK_SONG_PIC_REQUEST_CODE && data != null){
            mSelectedUri = data.getData();
            mViewSongBeforeUploading.setImageURI(mSelectedUri);
        }else{
            Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
        }
    }

}

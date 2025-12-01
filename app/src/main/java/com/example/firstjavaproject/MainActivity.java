package com.example.firstjavaproject;

import static com.example.firstjavaproject.models.AlbumArtUtil.getAlbumArt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.Toast;
import com.example.firstjavaproject.adapters.SongAdapter;
import com.example.firstjavaproject.models.Song;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Song> songList;
    SongAdapter adapter;

    private static final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.songList);

        songList = new ArrayList<>();

        if (hasPermission()) {
            loadSongs();
        } else {
            requestPermission();
        }

    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_AUDIO},
                    PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                loadSongs();

            } else {
                Toast.makeText(this, "Permission required to fetch songs!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Load songs into ListView
    private void loadSongs() {
        songList = getAllAudio();  // 🔥 fetching phone songs

//        System.out.println("Kishan" + songList);

        adapter = new SongAdapter(this, songList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Song clickedSong = songList.get(position);

            Intent intent = new Intent(MainActivity.this, PlayScreenActivity.class);

            intent.putExtra("title",clickedSong.getSong_name());
            intent.putExtra("artist", clickedSong.getArtist_name());
            intent.putExtra("path", clickedSong.getPath());          // ADD THIS
            intent.putExtra("albumId", clickedSong.getAlbumId());    // ADD THIS
            intent.putExtra("songList",songList);
            intent.putExtra("position", position);   // send clicked index

            startActivity(intent);
        });
    }


    // This method fetches real songs from device
    public ArrayList<Song> getAllAudio() {
        ArrayList<Song> songs = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
        };

        Cursor cursor = getContentResolver().query(
                uri, projection, selection, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {


                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                String path = cursor.getString(2);
                String albumId = cursor.getString(3);

                Bitmap albumImage = getAlbumArt(this,albumId);

                // You may want to update your Song model to accept path + albumId
                songs.add(new Song(
                        title,
                        artist,
                        path,
                        albumId
                ));
            }
            cursor.close();
        }
        return songs;
    }


}
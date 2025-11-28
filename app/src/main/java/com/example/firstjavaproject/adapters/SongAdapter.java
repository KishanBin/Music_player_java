package com.example.firstjavaproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.firstjavaproject.models.Song;
import java.util.List;
import com.example.firstjavaproject.R;   // ← ADD THIS

public class SongAdapter extends ArrayAdapter<Song> {

    Context context;
    List<Song> songs;

    public SongAdapter(Context context,List<Song> songs){
        super(context, R.layout.item_listview, songs);
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate row layout
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_listview, parent, false);
        }

        // get view from layout
        ImageView img = convertView.findViewById(R.id.songImage);
        TextView title = convertView.findViewById(R.id.song_name);
        TextView artist = convertView.findViewById(R.id.artist_name);

        Song song = songs.get(position);

        if (song.getImage() != null) {
            img.setImageBitmap(song.getImage());
        } else {
            img.setImageResource(R.drawable.android_music_icon); // default icon
        }

        title.setText(song.getSong_name());
        artist.setText(song.getArtist_name());

        return convertView;

    }
}

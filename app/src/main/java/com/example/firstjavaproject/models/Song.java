package com.example.firstjavaproject.models;

import android.graphics.Bitmap;

public class Song {

    Bitmap image;
    String song_name;
    String artist_name;
    String path;
    String albumId;


    public Song(Bitmap  image,String song_name,String artist_name,String path,String albumId){
        this.image = image;
        this.song_name = song_name;
        this.artist_name = artist_name;
        this.path = path;
        this.albumId = albumId;
    }

    public Bitmap getImage() {
        return image;
    }
    public String getSong_name(){return  song_name;}
    public String getArtist_name(){return  artist_name;}

    public String getPath(){return path;}
    public String getAlbumId(){return albumId;}


}

package com.example.firstjavaproject.models;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class AlbumArtUtil {

    public static Bitmap getAlbumArt(Context context, String albumId) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e) {
            return null;
        }
    }
}


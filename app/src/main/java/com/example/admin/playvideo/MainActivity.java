package com.example.admin.playvideo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity
{
    VideoView videoView;
    Button button;
    TextView Name,path,width,height,size;
    String x;
    int width1,height1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Uri video = data.getData();
            videoView.setVideoURI(video);

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            mediaController.setMediaPlayer(videoView);

            videoView.setMediaController(mediaController);
            videoView.start();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(final MediaPlayer mp)
                {
                    width1 = mp.getVideoWidth();
                    height1 = mp.getVideoHeight();
                }
            });

            x = video.toString();
            size(x);
        }
    }

    private String size(String x)
    {
        String Getpath = getRealPathFromURI(Uri.parse(x));

        File file = new File(Getpath);
        String name = file.getName();
        String videoPath = file.getAbsolutePath();

        File file1 = new File(videoPath);
        long length = file1.length();
        length = length / 1024;
        length = length / 1024;

        Name.setText(name);
        path.setText(videoPath);
        width.setText(Integer.toString(width1));
        height.setText(Integer.toString(height1));
        size.setText(Long.toString(length)+"MB");

        return null;
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void init()
    {
        videoView = findViewById(R.id.videoView);
        button = findViewById(R.id.button);

        Name = findViewById(R.id.Name);
        path = findViewById(R.id.path);

        width = findViewById(R.id.width);
        height = findViewById(R.id.height);
        size = findViewById(R.id.Size);
    }
}

package tz.co.neelansoft.millennium;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tz.co.neelansoft.millennium.data.Comment;
import tz.co.neelansoft.millennium.data.CommentsAdapter;
import tz.co.neelansoft.millennium.data.Config;

public class LiveActivity extends AppCompatActivity {

    private VideoView mLiveView;
    private RecyclerView mRecyclerview;
    private ImageView mImageThumbUp;
    private ImageView mImageThumbDown;
    private TextView mTextTitle;
    private EditText mEditComment;
    private ImageView mImageSend;
    private ProgressBar mProgressBar;
    List<Comment> commentList = new ArrayList<>();
    CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live);

        mLiveView = findViewById(R.id.videoView);
        mImageSend = findViewById(R.id.ivSend);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerview = findViewById(R.id.rvComments);
        mEditComment = findViewById(R.id.etComment);

        mLiveView.setVideoURI(Uri.parse(Config.LIVE_STREAM_URL));

        mLiveView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.GONE);
                mLiveView.start();
                android.widget.MediaController mediaController = new android.widget.MediaController(LiveActivity.this);
                mediaController.setAnchorView(mLiveView);
                mLiveView.setMediaController(mediaController);
                mediaController.show();
                mediaController.requestFocus();
                mediaController.setEnabled(true);

            }
        });

        adapter = new CommentsAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(adapter);

        mImageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = adapter.getItemCount()+1;
                String body = mEditComment.getText().toString();
                Date date = new Date();
                Comment comment = new Comment(id,"anonymous",body,date);
                commentList.add(comment);
                adapter.setCommentList(commentList);
                mEditComment.setText("");
                mEditComment.clearFocus();
            }
        });
        //suggest to play next video
        mLiveView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });


    }

}

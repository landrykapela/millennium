package tz.co.neelansoft.millennium;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tz.co.neelansoft.millennium.data.Comment;
import tz.co.neelansoft.millennium.data.CommentsAdapter;
import tz.co.neelansoft.millennium.data.Config;
import tz.co.neelansoft.millennium.data.Connection;

public class LiveActivity extends AppCompatActivity {

    private static final String TAG = "LiveActivity";
    private VideoView mLiveView;
    private RecyclerView mRecyclerview;
    private ImageView mImageThumbUp;
    private ImageView mImageThumbDown;
    private TextView mTextTitle;
    private TextView mTextConnections;
    private EditText mEditComment;
    private ImageView mImageSend;
    private ProgressBar mProgressBar;
    private DatabaseReference mFirebaseDatabase;
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
        mTextConnections = findViewById(R.id.tvWatching);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DB_REFERENCE).child("comments");

        mLiveView.setVideoURI(Uri.parse(Config.LIVE_STREAM_URL));

        final DatabaseReference connections = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DB_REFERENCE).child("connections");
        final Connection connection1 = new Connection();
        connections.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Connection connection = dataSnapshot.getValue(Connection.class);
                connection1.setId(connection.getId());
                connection1.setCurrent(connection.getCurrent()+1);
                connection1.setTotal(connection.getTotal()+1);

                Log.e(TAG,"current: "+connection1.getCurrent());
                Log.e(TAG,"connections: "+connection1.getCurrent()+"/"+connection1.getTotal());
                connections.setValue(connection1);
                mTextConnections.setText(String.valueOf(connection1.getCurrent()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                hideKeyBoard();
                sendComment(comment);
            }
        });
        //suggest to play next video
        mLiveView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });


        loadComments();
    }
    private void loadComments(){
        Query sortedComments = mFirebaseDatabase.orderByChild("id");
        sortedComments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Comment comment = ds.getValue(Comment.class);
                    commentList.add(comment);
                }

                adapter.setCommentList(commentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendComment(Comment comment){
        String key = mFirebaseDatabase.push().getKey();
        mFirebaseDatabase.child(key).setValue(comment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void hideKeyBoard(){
        InputMethodManager inputManager =
                (InputMethodManager) LiveActivity.this.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

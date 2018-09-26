package tz.co.neelansoft.millennium;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView mImageLive;
    private ImageView mImageVod;
    private ImageView mImageLibrary;
    private ImageView mImageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //retrieving image buttons
        mImageLibrary  =  findViewById(R.id.iv_library);
        mImageProfile  =  findViewById(R.id.iv_profile);
        mImageLive     =  findViewById(R.id.iv_live);
        mImageVod      =  findViewById(R.id.iv_vod);

       //set click listener to images
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clicked_item_id = v.getId();
                String destination = null;
                switch (clicked_item_id){
                    case R.id.iv_library:
                        destination = "LibraryActivity";
                        break;
                    case R.id.iv_profile:
                        destination = "ProfileActivity";
                        break;
                    case R.id.iv_live:
                        destination = "LiveActivity";
                        break;
                    case R.id.iv_vod:
                        destination = "VodActivity";
                        break;
                    default:
                        destination = "LiveActivity";
                        break;

                }
                launchActivity(destination);
            }
        };

        mImageProfile.setOnClickListener(listener);
        mImageLibrary.setOnClickListener(listener);
        mImageVod.setOnClickListener(listener);
        mImageLive.setOnClickListener(listener);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
    }
    private void launchActivity(String activityName){
        try{
            Class<?> destination = Class.forName(getPackageName().concat(".").concat(activityName));
            startActivity(new Intent(MainActivity.this,destination));
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
            Log.e(TAG,"Could not find activity with name "+activityName);
        }
    }
}

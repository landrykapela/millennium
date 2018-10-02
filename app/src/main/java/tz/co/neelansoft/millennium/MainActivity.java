package tz.co.neelansoft.millennium;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tz.co.neelansoft.millennium.data.Config;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView mImageLive;
    private ImageView mImageVod;
    private ImageView mImageLibrary;
    private ImageView mImageProfile;
    private TextView mTextHead;

    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //retrieving image buttons
        mImageLibrary  =  findViewById(R.id.iv_library);
        mImageProfile  =  findViewById(R.id.iv_profile);
        mImageLive     =  findViewById(R.id.iv_live);
        mImageVod      =  findViewById(R.id.iv_vod);

        mTextHead = findViewById(R.id.tvHead);


        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser != null) {
            update(currentUser);

            if (!currentUser.isEmailVerified()) {
                String username = generate_username(currentUser);
                String welcome_message = getString(R.string.welcome_text).concat(" "+username).concat(" ").concat(getString(R.string.verify_email));
                mTextHead.setText(welcome_message);
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title_email_confirm))
                        .setMessage(R.string.confirm_email)
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm_now, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendVerificationEmail(currentUser);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                aBuilder.create().show();
            }
        }
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
        if(mFirebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
    }
    private void sendVerificationEmail(final FirebaseUser user){
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        //findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void onResume(){
        super.onResume();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        user.reload();
        if(user.isEmailVerified()){
            update(user);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int item_id = item.getItemId();
        switch (item_id){
            case R.id.action_signout:
                signout();
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_about:
                about_me();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void signout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.confirm_signout)
                .setMessage(R.string.confirm_signout_message)
                .setCancelable(true)
                .setPositiveButton(R.string.signout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userid = mFirebaseAuth.getCurrentUser().getUid();
                        DatabaseReference online = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DB_REFERENCE).child("onlineusers");
                        online.child(userid).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mFirebaseAuth.signOut();
                                        launchActivity("LoginActivity");
                                    }
                                });

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.create().show();
    }
    private void about_me(){
        launchActivity("AboutActivity");
    }
    private void share(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.sharelink));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }
    private String generate_username(FirebaseUser user){
        String username = user.getDisplayName();
        if (username == null || username.isEmpty() || username.equalsIgnoreCase("")) {
            username = user.getEmail().split("@")[0];
        }
        return username;
    }
    private void update(FirebaseUser user){
        DatabaseReference online = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DB_REFERENCE).child("onlineusers");
        online.child(user.getUid()).setValue(0);
        String username = generate_username(user);
        String welcome_message = getString(R.string.welcome_text).concat(" "+username);
        mTextHead.setText(welcome_message);
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

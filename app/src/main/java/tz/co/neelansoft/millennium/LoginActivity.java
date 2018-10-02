package tz.co.neelansoft.millennium;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tz.co.neelansoft.millennium.data.Config;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private final int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseAuth mFirebaseAuth;
    private Button mSigninWithEmailAndPassword;
    private SignInButton mGoogleSignInButton;
    private EditText mEmailText;
    private EditText mPasswordText;
    private TextView mErrorText;
    private TextView mSignupText;
    private ProgressBar mProgressBar;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        root = FirebaseDatabase.getInstance().getReference(Config.FIREBASE_DB_REFERENCE);

        //mGoogleSignInButton = findViewById(R.id.btnGoogleSignIn);
        mProgressBar = findViewById(R.id.progressBarSignIn);
        mErrorText = findViewById(R.id.tvSignInError);
        mSignupText = findViewById(R.id.btnSignupWithEmailAndPassword);
        mEmailText = findViewById(R.id.etSignInEmail);
        mPasswordText = findViewById(R.id.etSignInPassword);
        mSigninWithEmailAndPassword = findViewById(R.id.btnSignInWithEmailAndPassword);

        mSigninWithEmailAndPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString();
                String password = mPasswordText.getText().toString();
                signinWithEmailAndPassword(email,password);
            }
        });

        mSignupText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            Log.e(TAG, "Userid - "+auth.getCurrentUser().getUid());
        }
    }

    private void signinWithEmailAndPassword(String email, String password){
        showProgress();
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgress();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void signInWithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Config.WEB_CLIENT_KEY)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,gso);
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent,SIGN_IN_REQUEST_CODE);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount googleAccount){
        showProgress();
        AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            hideProgress();

                            updateUI(mFirebaseAuth.getCurrentUser());
                        }
                        else{
                            String error = "An error occurred";
                            if(task.getException().getMessage() != null){
                                error = task.getException().getMessage();
                            }
                            Log.e(TAG,error,task.getException());
                            showSignInError(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgressBar.setVisibility(View.VISIBLE);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            Log.e(TAG,"result code: "+resultCode);
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task){
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);

            mProgressBar.setVisibility(View.GONE);
            if(account != null){
                firebaseAuthWithGoogle(account);
            }
            else{
                showSignInError(getResources().getString(R.string.login_error_msg));
                // Toast.makeText(SigninActivity.this,getResources().getString(R.string.login_error_msg), Toast.LENGTH_SHORT).show();
            }
        }
        catch(ApiException e){
            e.printStackTrace();
            Log.e(TAG,e.getMessage(),e);
            String  msg = String.valueOf(e.getStatusCode());
            Toast.makeText(LoginActivity.this,msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress(){
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        mProgressBar.setVisibility(View.GONE);
    }
    private void updateUI(FirebaseUser user){


        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
    private void showSignInError(String error_message){
        mErrorText.setText(error_message);
    }
}

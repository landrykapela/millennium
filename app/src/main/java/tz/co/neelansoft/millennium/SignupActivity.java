package tz.co.neelansoft.millennium;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tz.co.neelansoft.millennium.data.Config;

public class SignupActivity extends AppCompatActivity {
 private static final String TAG = "SignupActivity";
    private TextView mTextSignIn;
    private EditText mTextEmail;
    private EditText mTextPassword;
    private Button mButtonCreateAccount;
    private CheckBox mCheckPassword;
    private ProgressBar mProgressBar;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        mTextSignIn = findViewById(R.id.tvSignIn);
        mTextEmail = findViewById(R.id.etEmail);
        mTextPassword = findViewById(R.id.etPassword);

        mButtonCreateAccount = findViewById(R.id.btnCreateAccount);
        mCheckPassword = findViewById(R.id.cbShowPassword);

        mProgressBar = findViewById(R.id.progressBarSignup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mTextSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLogin();
            }
        });

        mButtonCreateAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();

                createUserWithEmailAndPassword(email,password);
            }
        });

        mCheckPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showHidePassword(isChecked);
            }
        });
    }
    private void showHidePassword(boolean checked){
        if(checked){
            mTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            mTextPassword.setSelection(mTextPassword.getText().toString().length()-1);
        }
        else{
            mTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
            mTextPassword.setSelection(mTextPassword.getText().toString().length()-1);
        }
    }
    private void createUserWithEmailAndPassword(String email, String password){
        mProgressBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void openLogin(){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    private void updateUI(FirebaseUser user){

        startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("userid",user.getUid()));
        finish();
    }
}

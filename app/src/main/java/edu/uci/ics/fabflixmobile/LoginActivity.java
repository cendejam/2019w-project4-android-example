package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast; 

import com.cabam.marco.wilosfoodwindows.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText inputEmail, inputPassword;
    private Button login;
    private Button guest;
    private ProgressBar progressBar;

    private void LoadSignInActivity()
    {
        //now we set the page if we actually need it to login a customerUser
        System.out.println("Login");
        setContentView(R.layout.activity_login);
        Toast.makeText(LoginActivity.this, "Successfully Signed Out", Toast.LENGTH_SHORT);
        inputEmail = findViewById(R.id.emailInput);
        inputPassword = findViewById(R.id.passwordInput);
        login = findViewById(R.id.LoginButton);
        guest = findViewById(R.id.Guest_Button);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);//put loading bar so users can know that we are checking if they could sign in

                //if we get both info from customerUser then we can now check and authenticatethe customerUser
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);//now that we completed the authentication we could take off the progress bar

                        if (!task.isSuccessful())//if authentication was unsuccessful
                        {
                            if (password.length() < 6)//check if password was too short
                            {
                                inputPassword.setError("Password was too short, 6 character minimum");//if it was let them know
                            }
                            else//else just let them know it failed period
                                Toast.makeText(LoginActivity.this, "Login Failed! Invalid Email/Password Combination", Toast.LENGTH_LONG).show();
                        }
                        else//if authentication is successful
                        {
                            loadTypeOfApp();
                        }
                    }
                });

            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(LoginActivity.this, SignUpCustomer.class), 0);
            }
        });
    }

    private void loadTypeOfApp()
    {
        finish();
        startActivityForResult(new Intent(LoginActivity.this, typeOfAccountChooser.class), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        //get Firebase auth Instance
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null)
            loadTypeOfApp();
        else {
            System.out.println("Null");
            LoadSignInActivity();
        }
    }


}

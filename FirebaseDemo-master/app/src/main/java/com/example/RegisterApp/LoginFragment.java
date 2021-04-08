package com.example.RegisterApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    Toolbar toolbar;
    TextView emailTv, passwordTv , forgotpassTv;
    EditText emailEt, passwordEt;

    NavController navController;

    Button loginButton, gotoRegisterButton;

    FirebaseAuth fireAuth;
    FirebaseUser currentUser;





    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fireAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome! Login Here!!");
        emailTv = view.findViewById(R.id.email_main_tv);
        passwordTv= view.findViewById(R.id.pass_main_tv);
        passwordEt=view.findViewById(R.id.pass_main_et);
        emailEt = view.findViewById(R.id.email_main_et);

        loginButton=view.findViewById(R.id.login_main_button);
        gotoRegisterButton=view.findViewById(R.id.gotosignup_button);
        forgotpassTv=view.findViewById(R.id.forgetpass_tv);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        gotoRegisterButton.setOnClickListener(view1 -> {
            navController.navigate(R.id.registerFragment);
        });

        loginButton.setOnClickListener(view2 ->{

            if (!checkEmptyFields())
            {
                String email = emailEt.getText().toString();
                String pass = passwordEt.getText().toString();
                loginUser(email,pass);
            }

        });



        forgotpassTv.setOnClickListener(view3-> {

            if(TextUtils.isEmpty(emailEt.getText().toString())){

                emailEt.setError("Please! Enter Your Email here to reset the password");
                return;
            }

            else if(!isValidEmail(emailEt.getText().toString())){
                emailEt.setError("Invalid email, Please write correct email to get reset link");
                return;
            }


            fireAuth.sendPasswordResetEmail(emailEt.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Password link has been sent!!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


    });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LoginFragment","onStart Called!");

        currentUser = fireAuth.getCurrentUser();

        if (currentUser != null)
        {
            updateUI(currentUser);
            Toast.makeText(getActivity().getApplicationContext(),"User Already Signing",Toast.LENGTH_LONG).show();
        }
    }

    public void loginUser(String email, String pass)
    {
            fireAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(getActivity(), task -> {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"Login Success!", Toast.LENGTH_SHORT).show();
                            currentUser = fireAuth.getCurrentUser();
                            updateUI(currentUser);
                        }else {
                            Toast.makeText(getActivity().getApplicationContext(),"Authenticate Failed!", Toast.LENGTH_SHORT).show();
                        }

                    });
    }


    public void updateUI(FirebaseUser user)
    {
        requireActivity().finish();
        Intent myIntent = new Intent(requireActivity(), DashBoardandProfile.class);
        myIntent.putExtra("user", user);
        requireActivity().startActivity(myIntent);
    }

    private boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(emailEt.getText().toString()))
        {
            emailEt.setError("Email cannot be empty!");
            emailEt.requestFocus();
            return true;
        }
        else if (TextUtils.isEmpty(passwordEt.getText().toString()))
        {
            passwordEt.setError("Password cannot be empty!");
            passwordEt.requestFocus();
            return true;
        }
        else if(!isValidEmail(emailEt.getText().toString())){
            emailEt.setError("Invalid email");
            return true;
        }

        return false;
    }








}
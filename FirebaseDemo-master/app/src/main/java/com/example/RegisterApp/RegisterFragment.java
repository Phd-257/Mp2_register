package com.example.RegisterApp;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {

    EditText edt_email,edt_pass,edt_vpass,edt_name,edt_location,edt_birthdate;
    Button btn_register;
    //TextView signinTv,usernameTv,passTv,verifyPassTv,birthdateTv,genderTv,emailRTv,cityTv;
    Toolbar toolbar;
    String[] items;
    Spinner genderOfuser;
    ArrayAdapter adapter;
    DatePickerDialog picker;



    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private NavController navController;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_email = view.findViewById(R.id.emailR_et);
        edt_pass = view.findViewById(R.id.passR_et);
        edt_vpass = view.findViewById(R.id.verifyRpass_et);
        edt_name = view.findViewById(R.id.userNameR_et);
        edt_location = view.findViewById(R.id.location_et);
        edt_birthdate=view.findViewById(R.id.birthdate_et);
        genderOfuser = view.findViewById(R.id.gender_spiner);
        toolbar= view.findViewById(R.id.toolbar);
        toolbar.setTitle("Register Yourself here!!!");
        edt_birthdate.setInputType(InputType.TYPE_NULL);
        edt_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_birthdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        items = new String[]{"Select the gender--","Male","Female"};
        adapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,items);
        genderOfuser.setAdapter(adapter);

        btn_register = view.findViewById(R.id.signUpR_button);


        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        btn_register.setOnClickListener(view1 -> {

            if (!checkEmptyFields())
            {
                if (edt_pass.getText().length()<6)
                {
                    edt_pass.setError("Invalid Password, Password Should be at least 6 characters");
                    edt_pass.requestFocus();
                }else {
                    if (!edt_pass.getText().toString().equals(edt_vpass.getText().toString()))
                    {
                        edt_vpass.setError("Password not match!");
                        edt_vpass.requestFocus();
                    }else {
                        String email = edt_email.getText().toString();
                        String pass = edt_pass.getText().toString();
                        String name = edt_name.getText().toString();
                        String location = edt_location.getText().toString();
                        String userBirthDate = edt_birthdate.getText().toString();
                        String gender = genderOfuser.getSelectedItem().toString();
                        Person person = new Person(email, pass, name, location, gender, userBirthDate);
                        createUser(person);


                    }
                }

            }

        });

    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(edt_email.getText().toString()))
        {
            edt_email.setError("Email cannot be empty!");
            edt_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_pass.getText().toString()))
        {
            edt_pass.setError("Password cannot be empty!");
            edt_pass.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_vpass.getText().toString()))
        {
            edt_vpass.setError("Confirm Password cannot be empty!");
            edt_vpass.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_name.getText().toString()))
        {
            edt_name.setError("Name cannot be empty!");
            edt_name.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_location.getText().toString()))
        {
            edt_location.setError("Location cannot be empty!");
            edt_location.requestFocus();
            return true;
        }
        else if(TextUtils.isEmpty(edt_birthdate.getText().toString())){

            edt_birthdate.setError("BirthDate cannot be empty!");
            edt_birthdate.requestFocus();
            return true;
        }
        else if(genderOfuser.getSelectedItem().toString().equals("Select the gender--")){

            edt_location.setError("Please Select your gender");
            edt_location.requestFocus();
            return true;
        }

        return false;
    }

   public void createUser(Person person)
    {
        auth.createUserWithEmailAndPassword(person.getEmail(),person.getPassword())
                .addOnCompleteListener(getActivity(), task -> {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        writeFireStore(person, firebaseUser);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),"Registration Error!",Toast.LENGTH_LONG).show();
                    }

                });
    }

    public void writeFireStore(Person person, FirebaseUser firebaseUser)
    {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Email",person.getEmail());
        userMap.put("Name",person.getName());
        userMap.put("City",person.getLocation());
        userMap.put("Gender",person.getGender());
        userMap.put("BirthDate",person.getBirthDate());


        firestore.collection("UserDetails").document(firebaseUser.getUid())
                .set(userMap).addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Registration Success!",Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        navController.navigate(R.id.loginFragment);
                    }else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"FireStore Error!",Toast.LENGTH_LONG).show();
                    }
        });

    }


}
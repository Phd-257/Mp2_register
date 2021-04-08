package com.example.RegisterApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.navigation.ui.NavigationUI.*;


public class DashboardFragment extends Fragment {


    Button btn_signOut;
    FirebaseUser user;
    FirebaseFirestore fireStore;
    NavController navController;
    TextView txt_Name;



    public DrawerLayout drawerLayout;

    public NavigationView navigationView;



    public DashboardFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        user = getArguments().getParcelable("user");
//        fireStore = FirebaseFirestore.getInstance();
        Intent intent = requireActivity().getIntent();
        user = intent.getParcelableExtra("user");
        fireStore = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_signOut = view.findViewById(R.id.btn_signOut);
        txt_Name = view.findViewById(R.id.txt_welcome);
        navController = Navigation.findNavController(getActivity(),R.id.host_fragment1);


        readFireStore();

        btn_signOut.setOnClickListener(view1 -> {
//
//            FirebaseAuth.getInstance().signOut();
//           navController.navigate(R.id.loginFragment);
            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
            startActivity(new Intent(requireActivity(), MainActivity.class));

        });


    }

    public void readFireStore()
    {
        DocumentReference docRef = fireStore.collection("UserDetails").document(user.getUid());

        docRef.get().addOnCompleteListener(task -> {
             if (task.isSuccessful())
             {
                 DocumentSnapshot doc = task.getResult();

                 if (doc.exists())
                 {
                     Log.d("DashboardFragment",doc.getData().toString());

                     txt_Name.setText("Welcome "+doc.get("Name")+"!!");


                 }

             }
        });
    }


}
package com.example.shinchi_apetcareapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardGrooming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardGrooming extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StandardGrooming() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StandardGrooming.
     */
    // TODO: Rename and change types and number of parameters
    public static StandardGrooming newInstance(String param1, String param2) {
        StandardGrooming fragment = new StandardGrooming();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_standard_grooming, container, false);
    }
    package com.example.shinchi_apetcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

    public class StandardGrooming extends AppCompatActivity {

        private static final double STANDARD_GROOMING_PRICE = 2000.00; // As per your XML layout

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_standard_grooming);

            // Initialize the CartManager to ensure auth state is available
            CartManager.getInstance().initialize(this);

            TextView bookNowBtn = findViewById(R.id.book_now_btn);

            if (bookNowBtn != null) {
                bookNowBtn.setOnClickListener(v -> {
                    if (CartManager.getInstance().isUserLoggedIn()) {
                        // User is logged in, navigate to the payment page.
                        Intent intent = new Intent(StandardGrooming.this, PaymentPage.class);
                        intent.putExtra(PaymentPage.EXTRA_TOTAL_PRICE, STANDARD_GROOMING_PRICE);
                        startActivity(intent);
                    } else {
                        // User is not logged in, navigate to the sign-in page.
                        Intent intent = new Intent(StandardGrooming.this, SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

}

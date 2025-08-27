package com.example.shinchi_apetcareapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BasicGrooming extends Fragment {

    public BasicGrooming() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic_grooming, container, false);
    }

    @Override
    public void onViewCreated(@com.example.shinchi_apetcareapp.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView bookNowButton = view.findViewById(R.id.textView28);

        if (bookNowButton != null) {
            bookNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Book Now clicked!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


package com.example.shinchi_apetcareapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PremiumSpa extends Fragment {
    private static final double PREMIUM_SPA_PRICE = 2500.00;
    public PremiumSpa() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_premium_spa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CartManager.getInstance().initialize(requireContext());
        TextView bookNowButton = view.findViewById(R.id.book_now_btn);
        if (bookNowButton != null) {
            bookNowButton.setOnClickListener(v -> {
                if (CartManager.getInstance().isUserLoggedIn()) {
                    Intent intent = new Intent(getActivity(), PaymentPage.class);
                    intent.putExtra(PaymentPage.EXTRA_TOTAL_PRICE, PREMIUM_SPA_PRICE);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), SignIn.class);
                    startActivity(intent);
                }
            });
        }
    }
}
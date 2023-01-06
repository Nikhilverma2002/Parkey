package com.ontech.com.parkey;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class add_vehicle extends Fragment {

  View view;
  ImageView back;
  LinearLayout camera,voice;
  Context contextNullSafe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        camera = view.findViewById(R.id.camera);
        voice = view.findViewById(R.id.voice);
        back = view.findViewById(R.id.back_img);


        camera.setOnClickListener(v->{
            Fragment camfrag = new Camera_ocr();
            FragmentTransaction ft  = getFragmentManager().beginTransaction();
            ft.replace(R.id.add_vehi, camfrag);
            ft.addToBackStack(null);
            ft.commit();
        });
        back.setOnClickListener(v->{
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().remove(add_vehicle.this).commit();
        });
        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().remove(add_vehicle.this).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
        return view;
    }

    public Context getContextNullSafety() {
        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }
}
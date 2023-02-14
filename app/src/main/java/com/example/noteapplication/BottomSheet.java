package com.example.noteapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {

    private CallBackColor callBackColor;
    public BottomSheet() {
    }

    public BottomSheet(CallBackColor callBackColor) {
        this.callBackColor = callBackColor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_color_background, container, false);
        Button color1 = view.findViewById(R.id.color1_btn);
        Button color2 = view.findViewById(R.id.color2_btn);
        Button color3 = view.findViewById(R.id.color3_btn);
        Button color4 = view.findViewById(R.id.color4_btn);
        Button color5 = view.findViewById(R.id.color5_btn);

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackColor.setColorCode(getResources().getColor(R.color.color1, null));
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackColor.setColorCode(getResources().getColor(R.color.color2, null));
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackColor.setColorCode(getResources().getColor(R.color.color3, null));
            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackColor.setColorCode(getResources().getColor(R.color.color4, null));
            }
        });
        color5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackColor.setColorCode(getResources().getColor(R.color.color5, null));
            }
        });
        return view;
    }
}

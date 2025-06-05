package com.auto.calculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HexToBinaryFragment extends Fragment {

    private EditText inputHex;
    private TextView outputBinary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hex_to_binary, container, false);

        inputHex = view.findViewById(R.id.input_hex);
        outputBinary = view.findViewById(R.id.output_binary);

        inputHex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String hex = s.toString().trim();
                if (hex.isEmpty()) {
                    outputBinary.setText("二进制: ");
                    return;
                }

                try {
                    int decimal = Integer.parseInt(hex, 16);
                    String binary = Integer.toBinaryString(decimal); // 无前导 0
                    outputBinary.setText("二进制: " + binary);
                } catch (NumberFormatException e) {
                    outputBinary.setText("格式错误");
                }
            }
        });

        return view;
    }
}
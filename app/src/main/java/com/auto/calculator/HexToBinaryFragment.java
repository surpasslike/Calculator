package com.auto.calculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexToBinaryFragment extends Fragment {

    private EditText inputHex;
    private TextView outputBinary;
    private TextView outputCore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hex_to_binary, container, false);

        inputHex = view.findViewById(R.id.input_hex);
        outputBinary = view.findViewById(R.id.output_binary);
        outputCore = view.findViewById(R.id.output_core);

        inputHex.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String hex = s.toString().trim();
                if (hex.isEmpty()) {
                    outputBinary.setText("二进制: ");
                    outputCore.setText("抓包核心: ");
                    return;
                }

                try {
                    int decimal = Integer.parseInt(hex, 16);
                    String binary = Integer.toBinaryString(decimal);
                    outputBinary.setText("二进制: " + binary);

                    List<Integer> coreIds = new ArrayList<>();
                    for (int i = 0; i < binary.length(); i++) {
                        int bitIndex = binary.length() - 1 - i;
                        if (binary.charAt(i) == '1') {
                            coreIds.add(bitIndex);
                        }
                    }

                    if (coreIds.size() > 2) {
                        coreIds.remove(Integer.valueOf(Collections.min(coreIds)));
                        coreIds.remove(Integer.valueOf(Collections.max(coreIds)));
                        Collections.sort(coreIds); // 修正顺序
                    } else {
                        coreIds.clear();
                    }

                    outputCore.setText("抓包核心: " + (coreIds.isEmpty() ? "无中间核心" : coreIds.toString()));
                } catch (NumberFormatException e) {
                    outputBinary.setText("二进制: 格式错误");
                    outputCore.setText("抓包核心: 格式错误");
                }

            }
        });

        return view;
    }
}
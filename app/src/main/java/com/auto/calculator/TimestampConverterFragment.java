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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimestampConverterFragment extends Fragment {

    private EditText timestampInput;
    private TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timestamp_converter, container, false);

        timestampInput = view.findViewById(R.id.input_timestamp);
        resultText = view.findViewById(R.id.output_time);

        timestampInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.isEmpty()) {
                    resultText.setText("转换结果：");
                    return;
                }

                try {
                    long timestamp = Long.parseLong(input);
                    Date date = new Date(timestamp * 1000); // 秒转毫秒
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    resultText.setText("转换结果：" + sdf.format(date));
                } catch (Exception e) {
                    resultText.setText("格式错误");
                }
            }
        });

        return view;
    }
}
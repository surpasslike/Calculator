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

    private final EditText[] inputs = new EditText[7];
    private TextView outputAll;

    private final String[] labels = new String[]{"analysis timesec","last   submit", "recognize done", "analysis  done ", "merge  done ", "compress done", "storage done"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timestamp_converter, container, false);

        // 绑定7个 EditText
        for (int i = 0; i < 7; i++) {
            int resId = getResources().getIdentifier("input_" + i, "id", requireContext().getPackageName());
            inputs[i] = view.findViewById(resId);
            inputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    updateAllResults();
                }
            });
        }

        // 绑定输出 TextView
        outputAll = view.findViewById(R.id.output_all_result);
        updateAllResults(); // 初始为空

        return view;
    }

    /**
     * 根据所有输入框更新输出内容
     */
    private void updateAllResults() {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            String label = labels[i];
            String inputText = inputs[i].getText().toString().trim();
            String timeStr;

            if (inputText.isEmpty()) {
                timeStr = "";
            } else {
                try {
                    long timestamp = Long.parseLong(inputText);
                    Date date = new Date(timestamp * 1000); // 秒转毫秒
                    timeStr = sdf.format(date);
                } catch (Exception e) {
                    timeStr = "格式错误";
                }
            }

            builder.append(String.format(Locale.getDefault(), "%-22s %s\n", label, timeStr));
        }

        outputAll.setText(builder.toString());
    }
}
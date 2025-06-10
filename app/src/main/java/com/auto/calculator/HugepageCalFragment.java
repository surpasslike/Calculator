package com.auto.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HugepageCalFragment extends Fragment {
    private EditText etTotalMemory;
    private Button btnCalculate;
    private TextView tvRecommendedLargePage;
    private TextView tvStorageBlocks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hugepage_cal, container, false);

        etTotalMemory = view.findViewById(R.id.et_total_memory_hugepage);
        btnCalculate = view.findViewById(R.id.btn_calculate_hugepage);
        tvRecommendedLargePage = view.findViewById(R.id.tv_recommended_large_page_hugepage);
        tvStorageBlocks = view.findViewById(R.id.tv_storage_blocks_hugepage);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etTotalMemory.getText().toString().trim();
                if (!TextUtils.isEmpty(input)) {
                    long totalMemorySize = Long.parseLong(input);
                    calculateAndDisplay(totalMemorySize);
                }
            }
        });

        return view;
    }

    private void calculateAndDisplay(long totalMemorySize) {
        long recommendedLargePageSize = totalMemorySize / 8;
        if (recommendedLargePageSize < 1) {
            recommendedLargePageSize = 1;
        }
        long storageBlocks = (long) (recommendedLargePageSize * 1000 / 4);
        if (storageBlocks < 1) {
            storageBlocks = 1;
        }

        tvRecommendedLargePage.setText("推荐大页大小：" + recommendedLargePageSize + "G");
        tvStorageBlocks.setText("存储块数：" + storageBlocks);
    }
}
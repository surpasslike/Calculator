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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class NetworkCalculatorFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_calculator, container, false);

        tabLayout = view.findViewById(R.id.tab_layout_network);
        viewPager = view.findViewById(R.id.view_pager_network);

        viewPager.setAdapter(new NetworkCalculatorPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("CIDR 计算");
            } else {
                tab.setText("网络/节点计算");
            }
        }).attach();

        return view;
    }

    private static class NetworkCalculatorPagerAdapter extends FragmentStateAdapter {
        public NetworkCalculatorPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new CIDRCalculatorSubFragment();
            } else {
                return new NetworkNodeCalculatorSubFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    // CIDR 计算子 Fragment
    public static class CIDRCalculatorSubFragment extends Fragment {
        private EditText etCidr;
        private Button btnCalculate;
        private TextView tvNetworkAddress;
        private TextView tvBroadcastAddress;
        private TextView tvSubnetMask;
        private TextView tvHostsCount;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_network_calculator_cidr, container, false);

            etCidr = view.findViewById(R.id.et_cidr);
            btnCalculate = view.findViewById(R.id.btn_calculate_network_cidr);
            tvNetworkAddress = view.findViewById(R.id.tv_network_address_cidr);
            tvBroadcastAddress = view.findViewById(R.id.tv_broadcast_address_cidr);
            tvSubnetMask = view.findViewById(R.id.tv_subnet_mask_cidr);
            tvHostsCount = view.findViewById(R.id.tv_hosts_count_cidr);

            btnCalculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cidr = etCidr.getText().toString().trim();
                    if (!TextUtils.isEmpty(cidr)) {
                        parseAndCalculateCIDR(cidr);
                    }
                }
            });

            return view;
        }

        private void parseAndCalculateCIDR(String cidr) {
            try {
                String[] parts = cidr.split("/");
                String ipStr = parts[0];
                int prefixLength = Integer.parseInt(parts[1]);

                InetAddress inetAddress = Inet4Address.getByName(ipStr);
                byte[] ipBytes = inetAddress.getAddress();

                byte[] subnetMaskBytes = new byte[4];
                for (int i = 0; i < prefixLength / 8; i++) {
                    subnetMaskBytes[i] = (byte) 0xFF;
                }
                int remainingBits = prefixLength % 8;
                if (remainingBits > 0) {
                    subnetMaskBytes[prefixLength / 8] = (byte) (0xFF << (8 - remainingBits));
                }

                byte[] networkAddressBytes = new byte[4];
                for (int i = 0; i < 4; i++) {
                    networkAddressBytes[i] = (byte) (ipBytes[i] & subnetMaskBytes[i]);
                }
                InetAddress networkAddress = Inet4Address.getByAddress(networkAddressBytes);

                byte[] broadcastAddressBytes = new byte[4];
                for (int i = 0; i < 4; i++) {
                    broadcastAddressBytes[i] = (byte) (networkAddressBytes[i] | ~subnetMaskBytes[i]);
                }
                InetAddress broadcastAddress = Inet4Address.getByAddress(broadcastAddressBytes);

                int hostsCount = (int) Math.pow(2, 32 - prefixLength) - 2;

                tvNetworkAddress.setText("网络地址：" + networkAddress.getHostAddress());
                tvBroadcastAddress.setText("广播地址：" + broadcastAddress.getHostAddress());
                tvSubnetMask.setText("子网掩码：" + Arrays.toString(subnetMaskBytes));
                tvHostsCount.setText("可用主机数：" + hostsCount);

            } catch (UnknownHostException | NumberFormatException e) {
                e.printStackTrace();
                tvNetworkAddress.setText("网络地址：解析错误");
                tvBroadcastAddress.setText("广播地址：解析错误");
                tvSubnetMask.setText("子网掩码：解析错误");
                tvHostsCount.setText("可用主机数：解析错误");
            }
        }
    }

    // 网络/节点计算子 Fragment
    public static class NetworkNodeCalculatorSubFragment extends Fragment {
        private EditText etIpAddress;
        private EditText etSubnetMask;
        private Button btnCalculate;
        private TextView tvNetworkAddress;
        private TextView tvBroadcastAddress;
        private TextView tvHostsRange;
        private TextView tvSubnetsCount;
        private TextView tvNodesPerSubnet;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_network_calculator_node, container, false);

            etIpAddress = view.findViewById(R.id.et_ip_address_node);
            etSubnetMask = view.findViewById(R.id.et_subnet_mask_node);
            btnCalculate = view.findViewById(R.id.btn_calculate_network_node);
            tvNetworkAddress = view.findViewById(R.id.tv_network_address_node_tab);
            tvBroadcastAddress = view.findViewById(R.id.tv_broadcast_address_node_tab);
            tvHostsRange = view.findViewById(R.id.tv_hosts_range_tab);
            tvSubnetsCount = view.findViewById(R.id.tv_subnets_count_tab);
            tvNodesPerSubnet = view.findViewById(R.id.tv_nodes_per_subnet_tab);

            btnCalculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ipStr = etIpAddress.getText().toString().trim();
                    String subnetMaskStr = etSubnetMask.getText().toString().trim();
                    if (!TextUtils.isEmpty(ipStr) &&!TextUtils.isEmpty(subnetMaskStr)) {
                        parseAndCalculateNetworkNode(ipStr, subnetMaskStr);
                    }
                }
            });

            return view;
        }

        private void parseAndCalculateNetworkNode(String ipStr, String subnetMaskStr) {
            try {
                InetAddress ipAddress = Inet4Address.getByName(ipStr);
                byte[] ipBytes = ipAddress.getAddress();

                InetAddress subnetMaskAddress = Inet4Address.getByName(subnetMaskStr);
                byte[] subnetMaskBytes = subnetMaskAddress.getAddress();

                byte[] networkAddressBytes = new byte[4];
                for (int i = 0; i < 4; i++) {
                    networkAddressBytes[i] = (byte) (ipBytes[i] & subnetMaskBytes[i]);
                }
                InetAddress networkAddress = Inet4Address.getByAddress(networkAddressBytes);

                byte[] broadcastAddressBytes = new byte[4];
                for (int i = 0; i < 4; i++) {
                    broadcastAddressBytes[i] = (byte) (networkAddressBytes[i] | ~subnetMaskBytes[i]);
                }
                InetAddress broadcastAddress = Inet4Address.getByAddress(broadcastAddressBytes);

                byte[] firstHostBytes = Arrays.copyOf(networkAddressBytes, 4);
                firstHostBytes[3]++;
                InetAddress firstHost = Inet4Address.getByAddress(firstHostBytes);

                byte[] lastHostBytes = Arrays.copyOf(broadcastAddressBytes, 4);
                lastHostBytes[3]--;
                InetAddress lastHost = Inet4Address.getByAddress(lastHostBytes);

                int subnetBits = 0;
                for (byte b : subnetMaskBytes) {
                    subnetBits += Integer.bitCount(b & 0xFF);
                }
                int subnetsCount = (int) Math.pow(2, (32 - subnetBits));

                int nodesPerSubnet = (int) Math.pow(2, (32 - subnetBits)) - 2;

                tvNetworkAddress.setText("网络地址：" + networkAddress.getHostAddress());
                tvBroadcastAddress.setText("广播地址：" + broadcastAddress.getHostAddress());
                tvHostsRange.setText("可用主机范围：" + firstHost.getHostAddress() + " - " + lastHost.getHostAddress());
                tvSubnetsCount.setText("子网数量：" + subnetsCount);
                tvNodesPerSubnet.setText("每个子网节点数：" + nodesPerSubnet);

            } catch (UnknownHostException e) {
                e.printStackTrace();
                tvNetworkAddress.setText("网络地址：解析错误");
                tvBroadcastAddress.setText("广播地址：解析错误");
                tvHostsRange.setText("可用主机范围：解析错误");
                tvSubnetsCount.setText("子网数量：解析错误");
                tvNodesPerSubnet.setText("每个子网节点数：解析错误");
            }
        }
    }
}
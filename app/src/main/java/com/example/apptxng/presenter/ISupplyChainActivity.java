package com.example.apptxng.presenter;

import com.example.apptxng.model.Factory;
import com.example.apptxng.model.SupplyChain;

import java.util.List;

public interface ISupplyChainActivity {
    void supplyChains(List<SupplyChain> supplyChainList);
    void exception (String message);
    void infoFactory (Factory factory);
}

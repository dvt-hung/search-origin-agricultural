package com.example.apptxng.presenter;

import com.example.apptxng.model.Balance;

import java.util.List;

public interface IBalanceAdmin {
    void getBalance(List<Balance> list);
    void Exception(String message);
    void addBalanceMessage(String message);
    void updateBalanceMessage(String message);
    void deleteBalanceMessage(String message);
}

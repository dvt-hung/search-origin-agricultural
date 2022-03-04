package com.example.apptxng.presenter;

public interface ISettingAdmin {

    void inCorrectPassOld();
    void inCorrectPassConfirm();
    void inCorrectPassLength();
    void resultChangePass(String message);
    void Exception(String message);
}

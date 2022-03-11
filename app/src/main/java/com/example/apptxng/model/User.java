package com.example.apptxng.model;

import com.example.apptxng.R;

public class User {
    private int idUser;
    private String email;
    private String image;
    private String phone;
    private String passWord;
    private String nameFarm;
    private String address;
    private int accept;
    private int idRole;
    private String name;
    private ResponsePOST response;
    public User() {
    }

    public User(String name, String email, String passWord, int accept, int idRole) {
        this.name = name;
        this.email = email;
        this.passWord = passWord;
        this.accept = accept;
        this.idRole = idRole;
    }

    // Check length password
    public boolean checkLengthPassword()
    {
        return this.passWord.length() > 6;
    }

    // Check confirm password
    public boolean checkConfirmPassword(String confirmPass)
    {
        return this.passWord.equals(confirmPass);
    }

    // Check pass word old
    public boolean checkPasswordOld(String passOld)
    {
        return this.getPassWord().equals(passOld);
    }

    // Check check and display value
    public String displayInfoValueString(String value)
    {
        if (value == null)
        {
            return "Đang cập nhật";
        }
        return value;
    }

    // Getter, Setter

    public ResponsePOST getResponse() {
        return response;
    }

    public void setResponse(ResponsePOST response) {
        this.response = response;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNameFarm() {
        return nameFarm;
    }

    public void setNameFarm(String nameFarm) {
        this.nameFarm = nameFarm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int isAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
}

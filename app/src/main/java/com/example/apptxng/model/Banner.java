package com.example.apptxng.model;

import java.io.Serializable;

public class Banner implements Serializable {
    private int idBanner;
    private String image_Banner;

    public Banner() {
    }

    public int getIdBanner() {
        return idBanner;
    }

    public void setIdBanner(int idBanner) {
        this.idBanner = idBanner;
    }

    public String getImage_Banner() {
        return image_Banner;
    }

    public void setImage_Banner(String image_Banner) {
        this.image_Banner = image_Banner;
    }
}

package com.example.checked;
public class UserData {
    private String id;
    private String qrData;

    public UserData() {}

    public UserData(String id, String qrData) {
        this.id = id;
        this.qrData = qrData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }
}

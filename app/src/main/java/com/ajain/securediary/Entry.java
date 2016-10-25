package com.ajain.securediary;

class Entry {
    private String title;
    private String password;
    private String data;

    Entry(String title, String password, String data) {
        this.title = title;
        this.password = password;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    String getPassword() {
        return password;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public void setData(String data) {
        this.data = data;
    }
}

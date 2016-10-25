package com.ajain.securediary;

import java.util.ArrayList;

class PasswordCard {
    private ArrayList<Entry> dataEntries;
    private static PasswordCard ourInstance = new PasswordCard();

    static PasswordCard getInstance() {
        return ourInstance;
    }

    private PasswordCard() {
        dataEntries = new ArrayList<>();
    }

    void addNewEntry(String title, String data, String password) {
        Entry entry = new Entry(title, password, data);
        if (dataEntries == null)
            dataEntries = new ArrayList<>();
        dataEntries.add(entry);
    }

    boolean deleteEntry(Entry e) {
        if (dataEntries.contains(e)) {
            dataEntries.remove(e);
            return true;
        }
        return false;
    }

    void editEntry(String oldTitle, String oldData, String newTitle, String newData, String password) {
        for (Entry e : dataEntries) {
            if (e.getTitle().equals(oldTitle) && e.getData().equals(oldData)) {
                e.setData(newData);
                e.setTitle(newTitle);
                e.setPassword(password);
                return;
            }
        }
    }

    ArrayList<Entry> getDataEntries() {
        return dataEntries;
    }

    void setDataEntries(ArrayList<Entry> dataEntries) {
        this.dataEntries = dataEntries;
    }
}
package com.example.carendlessgame.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Comparator;

public class Records {

    private final ArrayList<Record> records = new ArrayList<>();

    public Records() {

    }

    public void add(Record record) {
        records.add(record);
        records.sort(Comparator.comparingInt(Record::getScore).reversed());
        if (records.size() > 10) {
            records.subList(10, records.size()).clear();
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public ArrayList<Record> getRecords() {
        return records;
    }
}

package com.example.carendlessgame.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Comparator;

public class Records {

    private ArrayList<Record> records = new ArrayList<>();
//    private static Records myDB;

    public Records() {

    }
//    public static Records initMyDB() {
//        if (myDB == null) {
//            myDB = new Records();
//        }
//        return myDB;
//    }

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

package com.example.carendlessgame.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.carendlessgame.Interfaces.CallBack_SendClick;
import com.example.carendlessgame.R;
import com.example.carendlessgame.Utilities.MySPv3;
import com.example.carendlessgame.models.Records;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private CallBack_SendClick callBack_sendClick;
    private ListView list_LST_records;

    public void setCallBack_sendClick(CallBack_SendClick callBack_sendClick) {
        this.callBack_sendClick = callBack_sendClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        String fromSP = MySPv3.getInstance().getString("records","");
        Records recordsFromJson = new Gson().fromJson(fromSP, Records.class);
        List<String> itemList = new ArrayList<>();

        if (recordsFromJson != null) {
            for (int i = 0; i < recordsFromJson.getRecords().size(); i++) {
                itemList.add(recordsFromJson.getRecords().get(i).getScore() + "");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                    android.R.layout.simple_list_item_1, itemList);
            list_LST_records.setAdapter(adapter);
            list_LST_records.setOnItemClickListener((parent, view1, position, id) -> callBack_sendClick.mark(recordsFromJson.getRecords().get(position).getLat(),recordsFromJson.getRecords().get(position).getLon()));
        }
        else {
            return view;
        }

        return view;
    }


    private void findViews(View view) {
        list_LST_records = view.findViewById(R.id.list_LST_records);
    }

}
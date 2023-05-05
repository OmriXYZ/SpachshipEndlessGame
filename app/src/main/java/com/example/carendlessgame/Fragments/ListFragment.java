package com.example.carendlessgame.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.example.carendlessgame.Interfaces.CallBack_SendClick;
import com.example.carendlessgame.R;
import com.google.android.material.button.MaterialButton;

public class ListFragment extends Fragment {

    private AppCompatEditText list_ET_name;
    private MaterialButton list_BTN_send;

    private CallBack_SendClick callBack_sendClick;

    public void setCallBack_sendClick(CallBack_SendClick callBack_sendClick) {
        this.callBack_sendClick = callBack_sendClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        list_BTN_send.setOnClickListener(v -> sendClicked());
        return view;
    }

    private void sendClicked() {
        if (callBack_sendClick != null)
            callBack_sendClick.userNameChosen(list_ET_name.getText().toString());
    }

    private void findViews(View view) {
        list_ET_name = view.findViewById(R.id.list_ET_name);
        list_BTN_send = view.findViewById(R.id.list_BTN_send);
    }
}
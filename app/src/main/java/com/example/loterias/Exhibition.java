package com.example.loterias;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.LinkedList;

public class Exhibition extends AppCompatActivity {

    class LoteryAdpater extends ArrayAdapter<LinkedList<int[]>>{
        public LoteryAdpater(Context ctx){
            super(ctx, 0, lotery);
        }

        @Override
        public View getView(int pos, View recicle, ViewGroup parent){
            if (recicle == null) {
                recicle = getLayoutInflater().inflate(R.layout.bet_list_item, null);
            }
            TextView loteryNumbers = (TextView) recicle.findViewById(R.id.lotery_numbers);
            CheckBox chkLotery = (CheckBox) recicle.findViewById(R.id.chk_lotery);

            LinkedList<int[]> bet = lotery.get(pos);
            loteryNumbers.setText(String.valueOf(bet));

            return recicle;
        }
    }

    ArrayList<LinkedList<int[]>> lotery;
    ListView list;
    LoteryAdpater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bets);

        adapter = new LoteryAdpater(this);
        list = (ListView) findViewById(R.id.list_invest);
        list.setAdapter(adapter);
        list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}

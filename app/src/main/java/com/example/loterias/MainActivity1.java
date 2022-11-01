package com.example.loterias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity1 extends AppCompatActivity {
    int[] numbers;
    EditText edInitialNumber, edFinalNumber, edQtdNum, edQtdBet;
    int z;
    boolean start;

    class NumbersReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context ctx, Intent intent){
            if(intent.getAction().equals(CounterService1.ACTION_NUMEROS_PRONTOS)){
                start = intent.getBooleanExtra("acabou", false);
                if (start){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setMessage("Foram geradas apostas")
                            .setPositiveButton("Ver", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent it = new Intent(getApplicationContext(), Exhibition.class);
                                    it.putExtra("apostas", intent.getParcelableArrayListExtra("apostas"));
                                    startActivity(it);
                                }
                            });
                    builder.setNeutralButton("Ignorar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });builder.create().show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        edInitialNumber = (EditText) findViewById(R.id.ed_initial_number);
        edFinalNumber = (EditText) findViewById(R.id.ed_final_number);
        edQtdNum = (EditText) findViewById(R.id.ed_qtd_num);
        edQtdBet = (EditText) findViewById(R.id.ed_qtd_bet);
        NumbersReceiver rcvr = new NumbersReceiver();
        registerReceiver(rcvr, new IntentFilter(CounterService1.ACTION_NUMEROS_PRONTOS));
    }

    public void generate(View v) {
        z = Integer.parseInt(edQtdNum.getText().toString());
        numbers = new int[z];
        Intent it = new Intent(this, CounterService1.class);
        it.setAction( CounterService1.ACTION_INICIAR );
        it.putExtra(CounterService1.EXTRA_QUANTIDADE_NUMEROS, Integer.parseInt(edQtdNum.getText().toString()));
        it.putExtra(CounterService1.EXTRA_MINIMO, Integer.parseInt(edInitialNumber.getText().toString()));
        it.putExtra(CounterService1.EXTRA_MAXIMO, Integer.parseInt(edFinalNumber.getText().toString()));
        it.putExtra(CounterService1.EXTRA_INTERVALO, 2000);
        it.putExtra(CounterService1.EXTRA_NUMEROS, numbers);
        it.putExtra(CounterService1.EXTRA_QUANTIDADE_APOSTAS, Integer.parseInt(edQtdBet.getText().toString()));
        startService( it );
    }

    public void clean(View v){
        edInitialNumber.setText("");
        edFinalNumber.setText("");
        edQtdNum.setText("");
        edQtdBet.setText("");
        numbers = null;
    }
}

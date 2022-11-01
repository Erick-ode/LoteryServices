package com.example.loterias;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class CounterService1 extends IntentService{

    public static final String ACTION_NUMEROS_PRONTOS = "com.example.loterias.action.NUMEROS_PRONTOS";
    public static final String ACTION_INICIAR = "com.example.loterias.action.INICIAR";

    public static final String EXTRA_QUANTIDADE_NUMEROS = "com.example.loterias.extra.QUANTIDADE_NUMEROS";
    public static final String EXTRA_MINIMO = "com.example.loterias.extra.MINIMO";
    public static final String EXTRA_MAXIMO = "com.example.loterias.extra.MAXIMO";
    public static final String EXTRA_INTERVALO = "com.example.loterias.extra.INTERVALO";
    //public static final String EXTRA_APOSTAS = "com.example.loterias.extra.APOSTAS";
    public static final String EXTRA_NUMEROS = "com.example.loterias.extra.NUMEROS";
    public static final String EXTRA_QUANTIDADE_APOSTAS = "com.example.loterias.extra.QUANTIDADE_APOSTAS";

    public CounterService1() {
        super("CounterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INICIAR.equals(action)) {
                int qtde = intent.getIntExtra(EXTRA_QUANTIDADE_NUMEROS, 6);
                int min = intent.getIntExtra(EXTRA_MINIMO, 0);
                int max = intent.getIntExtra(EXTRA_MAXIMO, 60);
                int intervalo = intent.getIntExtra(EXTRA_INTERVALO, 2000);
                int qtdeApostas = intent.getIntExtra(EXTRA_QUANTIDADE_APOSTAS, 1);
                Log.d("SERVICO: ", "INICIANDO...." + this);
                Random r = new Random();
                int n = 0;
                int x = n;
                int faixa = max - min;
                int nums[] = intent.getIntArrayExtra(EXTRA_NUMEROS);
                LinkedList<int[]> bets = new LinkedList<>();
                while (x < qtdeApostas) {
                    while (n < qtde) {
                        nums[n] = min + r.nextInt(faixa);
                        Log.d("SERVICO: ", String.valueOf(nums[n]));
                        try {
                            Thread.sleep(intervalo);
                        } catch (InterruptedException ie) {
                        }
                        n++;
                    }
                    bets.add(nums);
                    n = 0;
                    nums = intent.getIntArrayExtra(EXTRA_NUMEROS);
                    x ++;
                    Log.d("SERVICO: ", Arrays.toString(bets.get(x-1)));
                }
                boolean end = true;
                Intent it = new Intent(ACTION_NUMEROS_PRONTOS);
                it.putExtra("apostas", bets);
                it.putExtra("acabou", end);
                sendBroadcast(it);

                //Log.d("SERVICO: ", "TERMINOU!");
            }
        }
    }
}
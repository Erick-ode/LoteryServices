package com.example.loterias;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.Random;

public class CounterService extends IntentService {

    public static final String ACTION_NUMEROS_PRONTOS = "com.example.loterias.action.NUMEROS_PRONTOS";
    public static final String ACTION_INICIAR = "com.example.loterias.action.INICIAR";

    public static final String EXTRA_QUANTIDADE = "com.example.loterias.extra.QUANTIDADE";
    public static final String EXTRA_MINIMO = "com.example.loterias.extra.MINIMO";
    public static final String EXTRA_MAXIMO = "com.example.loterias.extra.MAXIMO";
    public static final String EXTRA_INTERVALO = "com.example.loterias.extra.INTERVALO";

    public CounterService() {
        super("CounterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INICIAR.equals(action)) {
                int qtde = intent.getIntExtra(EXTRA_QUANTIDADE,10);
                int min = intent.getIntExtra(EXTRA_MINIMO, 0);
                int max = intent.getIntExtra(EXTRA_MAXIMO, 1000);
                int intervalo = intent.getIntExtra(EXTRA_INTERVALO, 1000);
                Log.d("SERVICO: ","INICIANDO...."+this);
                Random r = new Random();
                int n = 0;
                int faixa = max - min;
                int nums[] = new int[ qtde ];
                while (n < qtde) {
                    nums[n] = min + r.nextInt(faixa);
                    Log.d("SERVICO: ", String.valueOf(nums[n]));
                    try {
                        Thread.sleep( intervalo );
                    } catch (InterruptedException ie) { }
                    n++;
                }
                Intent it = new Intent(ACTION_NUMEROS_PRONTOS);
                it.putExtra("numeros", nums);
                sendBroadcast( it );
                Log.d("SERVICO: ", "TERMINOU!");
            }
        }
    }
}
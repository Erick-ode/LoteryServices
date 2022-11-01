package com.example.loterias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import java.util.zip.CheckedOutputStream;

public class MainActivity extends AppCompatActivity {

    Gerador gerador;
    boolean conectado = false;

    private ServiceConnection conexao = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Gerador.GeradorBinder binder = (Gerador.GeradorBinder) service;
            gerador = binder.getService();
            conectado = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            conectado = false;
        }
    };


    class NumerosReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CounterService.ACTION_NUMEROS_PRONTOS)) {
                int numeros[] = (int[]) intent.getIntArrayExtra("numeros");
                if (numeros != null && numeros.length > 0) {
                    TextView lista = (TextView) findViewById(R.id.numeros);
                    StringBuilder bld = new StringBuilder();
                    for (int n : numeros) {
                        bld.append(n).append("\n");
                    }
                    lista.append(bld.toString());
                }
            } else if (intent.getAction().equals(Gerador.ACTION_NUMERO_GERADO)) {
                int numero = intent.getIntExtra("numero", -1);
                TextView edNum = findViewById(R.id.edNumero);
                edNum.setText(String.valueOf(numero));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NumerosReceiver rcvr = new NumerosReceiver();
        registerReceiver(rcvr, new IntentFilter(CounterService.ACTION_NUMEROS_PRONTOS));
        registerReceiver(rcvr, new IntentFilter(Gerador.ACTION_NUMERO_GERADO));
    }

    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, Gerador.class);
        bindService(intent, conexao, Context.BIND_AUTO_CREATE);
    }

    protected void onStop() {
        if (conectado) {
            gerador.parar();
        }
        unbindService( conexao );
        super.onStop();
    }

    public void iniciar(View v) {
        Intent it = new Intent(this, CounterService.class);
        it.setAction( CounterService.ACTION_INICIAR );
        it.putExtra(CounterService.EXTRA_QUANTIDADE, 10);
        it.putExtra(CounterService.EXTRA_MINIMO, 1000);
        it.putExtra(CounterService.EXTRA_MAXIMO, 10000);
        it.putExtra(CounterService.EXTRA_INTERVALO, 1000);
        startService( it );
    }

    public void gerar(View v) {
        if (conectado) {
            gerador.iniciar();
        }
    }

    public void pausar(View v) {
        if (conectado) {
            gerador.pausar();
        }
    }
}
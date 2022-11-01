package com.example.loterias;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class Gerador extends Service {

    public static final String ACTION_NUMERO_GERADO = "com.example.loterias.NUMERO_GERADO";

    private final IBinder binder = new GeradorBinder();

    private Randomizer randomizer;

    public class GeradorBinder extends Binder {
        Gerador getService() {
            return Gerador.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Gerador() {
        randomizer = new Randomizer();
        randomizer.start();
    }

    public void iniciar() {
        randomizer.setRunning(true);
    }

    public void pausar() {
        randomizer.setRunning(false);
    }

    public void parar() {
        randomizer.setActive(false);
        randomizer = null;
    }

    class Randomizer extends Thread {
        private boolean running = false;
        private boolean active = true;
        private Random rand = new Random();

        public void run() {
            while (active) {
                if (running) {
                    int x = 1000 + rand.nextInt(5000);
                    Intent it = new Intent(ACTION_NUMERO_GERADO);
                    it.putExtra("numero", x);
                    Log.d("GERADOR", String.valueOf( x ));
                    sendBroadcast(it);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
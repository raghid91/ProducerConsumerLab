package com.example.acer.producerconsumerlab;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends Activity {

    static final String welcome = "Hello, World!";
    static final String goodbye = "Goodbye, World!";
    Runnable rp1, rp2, rc;
    Thread tp1, tp2, tc;
    MyQueue queue;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);

        queue = new MyQueue(20);
        rp1 = new Producer(queue, welcome);
        rp2 = new Producer(queue, goodbye);
        rc = new Consumer(queue, hello);

        tp1 = new Thread(rp1);
        tp2 = new Thread(rp2);
        tc = new Thread(rc);

        tp1.start();
        tp2.start();
        tc.start();
    }

    Handler hello = new Handler(){
        public void handleMessage(android.os.Message message){
            text.setText(text.getText() + "\n" + queue.dequeue());
        }
    };
}

class MyQueue extends LinkedBlockingQueue<String>{

    private int max;

    public MyQueue(int i) {
        max = i;
    }

    public void enqueue(String s){
        this.offer(s);
    }

    public String dequeue(){
        return this.poll();
    }

    public boolean isEmpty(){
        return remainingCapacity()==size();
    }

    public boolean isFull(){
        return remainingCapacity()==0;
    }
}

class Producer implements Runnable{

    private String greeting;
    private MyQueue queue;
    private static final int greetingCount = 10;
    private static final int delay = 100;

    public Producer(MyQueue q, String s){
        greeting = s;
        queue = q;
    }

    public void run(){
        try{
            for(int i=1; i<= greetingCount; i++){
                if(!queue.isFull()){
                    queue.enqueue(i + ": " + greeting);
                }
                Thread.sleep((int) (Math.random()*delay));
            }
        }
        catch (InterruptedException exception){

        }
    }
}

class Consumer implements Runnable{

    Handler hi;
    private MyQueue queue;
    private static final int greetingCount = 20;
    private static final int delay = 100;

    public Consumer(MyQueue q, Handler hello){
        queue = q;
        hi = hello;
    }

    public void run(){
        try{
            for(int i=1; i<=greetingCount; i++){
                if(!queue.isFull()){
                    hi.sendEmptyMessage(0);
                }
                Thread.sleep((int) (Math.random()*delay));
            }
        }
        catch (InterruptedException exception){

        }
    }

}
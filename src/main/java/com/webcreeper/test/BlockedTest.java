package com.webcreeper.test;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockedTest {

    public static void main(String[] args) {
        
    }

    public void excuteTask(List<Task> tasks){
        ExecutorService service = Executors.newFixedThreadPool(3);
        BlockingDeque<Task> deque = new LinkedBlockingDeque<>(); 

    }
    
}

class Task implements Runnable {

    private int val;

    Task(int val){
        this.val = val;
    }

    void resetVal(int val){
        this.val = val;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        try {
            if(0 == val % 3){
                throw new RuntimeException("is three times");
            }
            System.out.println("the val is:" + this.val);

        } catch (RuntimeException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
    
    

}
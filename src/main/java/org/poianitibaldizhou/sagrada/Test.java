package org.poianitibaldizhou.sagrada;

import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Test {

    public static void main(String[] args) {
        DraftPool draftPool = new DraftPool();
        draftPool.addDice(new Dice(5, Color.BLUE));
        draftPool.addDice(new Dice(2, Color.GREEN));
        System.out.println(draftPool.toJSON());

        HashMap<String, String> json = new HashMap<>();
        json.putIfAbsent("error", "test");
        System.out.println(JSONObject.toJSONString(json));
    }

    private static void testPool() {


        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // TODO try remove and clear and something like that

        List<Runnable> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Runnable temp = () -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        Thread.sleep(2000);
                        System.out.println("Thread " + finalI);
                    }
                } catch (InterruptedException e) {

                }
            };

            threads.add(temp);
        }
        for (int i = 0; i < 10; i++) {
            scheduledExecutorService.submit(threads.get(i));
            System.out.println("Thread" + i + " added");
        }

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduledExecutorService.shutdownNow();
        //scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable newRunnable = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Theadnew");
            }
        };

        scheduledExecutorService.submit(newRunnable);
        scheduledExecutorService.shutdown();
    }
}




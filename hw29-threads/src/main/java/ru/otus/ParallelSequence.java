package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParallelSequence {
    private static final Logger logger = LoggerFactory.getLogger(ParallelSequence.class);
    private int lastId = 1;
    private final List<Integer> sequence = new ArrayList<>();

    public ParallelSequence() {
        var s = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        sequence.addAll(s);
        Collections.reverse(s);
        sequence.addAll(s);
    }

    public void run() {
        new Thread(() -> print_sequence(0)).start();
        new Thread(() -> print_sequence(1)).start();
    }

    private synchronized void print_sequence(int id) {
        for (int item : sequence) {
            try {
                while (lastId == id) {
                    this.wait();
                }

                logger.info("{}", item);
                lastId = id;
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

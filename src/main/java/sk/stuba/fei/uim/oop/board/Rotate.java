package sk.stuba.fei.uim.oop.board;

import java.util.Random;

public enum Rotate {
    FIRST_POSITION,
    SECOND_POSITION,
    THIRD_POSITION,
    FOURTH_POSITION;

    private final Random random;

    Rotate() {
        this.random = new Random();
    }

    public Rotate getRandomRotate() {
        int index = random.nextInt(Rotate.values().length);
        return Rotate.values()[index];
    }

    public Rotate moveForward() {
        int currentIndex = this.ordinal();
        int nextIndex = (currentIndex + 1) % Rotate.values().length;
        return Rotate.values()[nextIndex];
    }
}

package fr.depix.bulb_manager.bulb.domain;

public class Bulb {

    private static final int MAX_COUNTER = 3;
    private boolean isTurnOn;
    private int counter = 0;

    public Bulb() {
        isTurnOn = false;
    }

    public Bulb(boolean state) {
        this.isTurnOn = state;
    }

    public void switchOn() {
        isTurnOn = true;
        counter++;
    }

    public boolean isTurnOn() {
        return counter < MAX_COUNTER && isTurnOn;
    }

    public void switchOff() {
        isTurnOn = false;
    }
}

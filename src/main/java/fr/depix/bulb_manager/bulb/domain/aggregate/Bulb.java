package fr.depix.bulb_manager.bulb.domain.aggregate;

public record Bulb(
        boolean isTurnOn,
        int counter
) {

    private static final int MAX_COUNTER = 3;

    public Bulb() {
        this(false, 0);
    }

    public Bulb switchOn() {
        return new Bulb(true, counter + 1);
    }

    public boolean isTurnOn() {
        return counter < MAX_COUNTER && isTurnOn;
    }

    public Bulb switchOff() {
        return new Bulb(false, counter);
    }

}

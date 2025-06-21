package fr.depix.bulb_manager.bulb.domain.aggregate;

public record Bulb(
        boolean isTurnOn,
        int count
) {

    public Bulb() {
        this(false, 0);
    }

}

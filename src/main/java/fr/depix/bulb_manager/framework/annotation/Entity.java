package fr.depix.bulb_manager.framework.annotation;

import org.jmolecules.ddd.annotation.Identity;

@org.jmolecules.ddd.annotation.Entity
public interface Entity<I extends Identifier> {

    @Identity
    I id();

}

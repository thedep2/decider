package fr.depix.bulb_manager;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.archunit.JMoleculesArchitectureRules;
import org.jmolecules.archunit.JMoleculesDddRules;

@AnalyzeClasses(packages = "fr.depix.bulb_manager") // (1)
class JMoleculesRulesUnitTest {

    @ArchTest
    ArchRule ignored1 = JMoleculesDddRules.all(); // (2)

    @ArchTest
    ArchRule ignored2 = JMoleculesArchitectureRules.ensureHexagonal();

}

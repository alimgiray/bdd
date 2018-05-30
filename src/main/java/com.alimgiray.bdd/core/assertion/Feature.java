package com.alimgiray.bdd.core.assertion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aaytar
 * @since 30.05.2018 10:23
 */
public class Feature {

    private List<Scenario> scenarios;

    public Feature() {
        scenarios = new ArrayList<>();
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }
}

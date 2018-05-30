package com.alimgiray.bdd.core.assertion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aaytar
 * @since 30.05.2018 10:27
 */
public class Scenario {

    private List<Step> steps;

    public Scenario() {
        steps = new ArrayList<>();
    }

    public Scenario given(Step step) {
        steps.add(step);
        return this;
    }

    public Scenario when(Step step) {
        steps.add(step);
        return this;
    }

    public Scenario then(Step step) {
        steps.add(step);
        return this;
    }

}

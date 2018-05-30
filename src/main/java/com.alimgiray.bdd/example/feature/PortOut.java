package com.alimgiray.bdd.example.feature;

import com.alimgiray.bdd.core.assertion.Feature;
import com.alimgiray.bdd.example.scenario.NumaraTaşıma;

/**
 * @author aaytar
 * @since 30.05.2018 10:59
 */
public class PortOut extends Feature {

    public PortOut() {

        addScenario(new NumaraTaşıma());


    }

}

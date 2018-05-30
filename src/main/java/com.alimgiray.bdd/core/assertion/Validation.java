package com.alimgiray.bdd.core.assertion;

import com.alimgiray.bdd.core.database.Query;

/**
 * @author aaytar
 * @since 30.05.2018 10:38
 */
public class Validation implements Step {

    private boolean result;

    private Query dbField;
    private String field;

    public Validation(Query dbField, String field) {
        this.dbField = dbField;
        this.field = field;
    }

    @Override
    public void run() {
        result = dbField.getResult().equals(field);
    }

}

package com.opencastsoftware.energysourcechecker.models;

public enum EndDateValidation {
    VALID,
    ERROR_FUTURE_DATE,
    ERROR_BEFORE_START_DATE,
    ERROR_MORE_THAN_14_DAYS
}

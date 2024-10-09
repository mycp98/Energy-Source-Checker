package com.opencastsoftware.energysourcechecker.exceptions;

import org.springframework.web.client.RestClientException;

public class CarbonIntensityClientException extends RestClientException {
    public CarbonIntensityClientException(String message) {super(message);}
}

package com.iptiq.model;

import java.util.Random;
import java.util.logging.Logger;

public class Provider {

    private final String identifier;
    private final Logger logger;
    private Boolean isHealthy;

    public Provider() {
        this.identifier = String.valueOf(new Random().nextInt());
        this.logger = Logger.getGlobal();
    }

    public Provider(String providerIdentifier) {
        this.identifier = providerIdentifier;
        this.logger = Logger.getGlobal();
    }

    public String get() {
        return this.identifier;
    }

    /**
     * This method is responsible for checking whether a provider is healthy or not. It is beyond the scope of the
     * assignment to identify when a Provider is classified as unhealthy. Thus, I used a random boolean generator to
     * decide this.
     *
     * @return health state of a Provider
     */
    public boolean check() {
        this.isHealthy = new Random().nextBoolean();
        logger.info(Thread.currentThread().getName() + " runs Provider- " + this.identifier +
                " which is Healthy?- " + this.isHealthy);
        return this.isHealthy;
    }
}

package com.iptiq.allocation.impl;


import com.iptiq.allocation.LoadBalancerAllocation;

import java.util.logging.Logger;

public class RoundRobinAllocation extends LoadBalancerAllocation {

    private final Logger logger;
    private int lastProviderIndex = -1;

    public RoundRobinAllocation() {
        this.logger = Logger.getGlobal();
    }

    @Override
    public int nextProviderIndex(int registeredProvidersSize) {
        if (registeredProvidersSize == 0) {
            logger.info(Thread.currentThread().getName() + ": No registered providers.");
            //no registered providers -> returning -1
            return -1;
        } else {
            lastProviderIndex++;
            if (lastProviderIndex == registeredProvidersSize) {
                logger.info(Thread.currentThread().getName() + ": Need to restart the rounds.");
                lastProviderIndex = 0;
                return lastProviderIndex;
            }
            return lastProviderIndex;
        }
    }
}

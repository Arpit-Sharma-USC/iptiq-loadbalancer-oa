package com.iptiq.model;


import com.iptiq.allocation.LoadBalancerAllocation;
import com.iptiq.exceptions.MaxCapacityReachedException;
import com.iptiq.exceptions.UnavailableProviderException;
import com.iptiq.heartbeat.HeartBeatChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static com.iptiq.constants.Constants.MAX_NUM_OF_SUPPORTED_PROVIDERS;
import static com.iptiq.constants.Constants.MAX_PARALLEL_REQUESTS_PER_PROVIDER;

public class LoadBalancer {

    private final LoadBalancerAllocation loadBalancerAllocation;
    private final List<Provider> registeredProviders = new ArrayList<>();
    private final AtomicInteger liveRequestCount = new AtomicInteger(0);
    private final HeartBeatChecker healthChecker;
    private final Logger logger;

    public LoadBalancer(LoadBalancerAllocation loadBalancerAllocation) {
        this.loadBalancerAllocation = loadBalancerAllocation;
        this.healthChecker = new HeartBeatChecker(registeredProviders);
        this.logger = Logger.getGlobal();
        this.startHealthCheck();
    }

    public List<Provider> getRegisteredProviders() {
        return registeredProviders;
    }

    /**
     * Get method to fetch a provider to serve the request.
     *
     * @return Identifier of a provider selected based on an allocation technique.
     * @throws MaxCapacityReachedException when a maximum threshold of providers is breached
     */
    public String get() {
        incrementLiveRequestCount();
        try {
            return getHealthyProviders(healthChecker.getHealthyProviders());
        } catch (UnavailableProviderException | MaxCapacityReachedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * This method registers a new Provider to the Load Balancer.
     *
     * @param provider - a Provider object
     */
    public synchronized void register(Provider provider) {
        if (isProviderSizeUnderMaxThreshold()) {
            registerHelper(provider);
        } else {
            logger.info(Thread.currentThread().getName() + " has reached Max number of Providers. " +
                    "Please try again later!");
        }
    }

    /**
     * This method unregisters a Provider from the Load Balancer.
     *
     * @param providerIdentifier - unique identifier of the Provider to unregister
     */
    public synchronized void unregister(String providerIdentifier) {
        Optional<Provider> providerToBeUnregistered = registeredProviders.stream()
                .filter(provider -> Objects.equals(provider.get(), providerIdentifier))
                .findFirst();

        if (providerToBeUnregistered.isEmpty()) {
            logger.warning(Thread.currentThread().getName() + ": Provider does not exist!");
        } else if (registeredProviders.remove(providerToBeUnregistered.get())) {
            logger.info(Thread.currentThread().getName() + ": Provider with identifier: " +
                    providerIdentifier + " unregistered");
        }
    }

    private void registerHelper(Provider provider) {
        if (isAlreadyRegistered(provider)) {
            logger.info(Thread.currentThread().getName() + ": Provider with identifier: " + provider.get() +
                    " is already registered");
        } else {
            registeredProviders.add(provider);
            logger.info(Thread.currentThread().getName() + ": Provider " + provider.get() + " registered");
        }
    }

    private boolean isAlreadyRegistered(Provider provider) {
        return registeredProviders.contains(provider);
    }

    private boolean isProviderSizeUnderMaxThreshold() {
        return registeredProviders.size() < MAX_NUM_OF_SUPPORTED_PROVIDERS;
    }

    /**
     * This method begins the health check for a Load balancer as soon as it is instantiated. See the constructor.
     */
    private void startHealthCheck() {
        healthChecker.start();
    }

    private String getHealthyProviders(List<Provider> healthyProviders) throws MaxCapacityReachedException,
            UnavailableProviderException {
        //does the cluster have capacity?
        if (isClusterCapacityUnderThreshold(healthyProviders)) {
            int providerIndex = loadBalancerAllocation.nextProviderIndex(healthyProviders.size());
            liveRequestCount.decrementAndGet();
            return getProviderByIndex(healthyProviders, providerIndex);
        } else {
            liveRequestCount.decrementAndGet();
            throw new MaxCapacityReachedException(Thread.currentThread().getName() + ": Max cluster capacity reached!");
        }
    }

    private void incrementLiveRequestCount() {
        logger.info(Thread.currentThread().getName() + ": live requests: " + liveRequestCount.get());
        liveRequestCount.incrementAndGet();
    }

    /**
     * This method fetches a provider by an index passed as parameter.
     * If the there are no providers available it throws an UnavailableProviderException exception.
     *
     * @param healthyProviders - A list of all the healthy providers available
     * @param providerIndex    - Index of the provider to get
     * @return String - Identifier of the Provider selected based on the allocation technique.
     * @throws UnavailableProviderException when there are no providers available.
     */
    private String getProviderByIndex(List<Provider> healthyProviders, int providerIndex)
            throws UnavailableProviderException {
        if (isValidProviderIndex(providerIndex)) {
            return healthyProviders.get(providerIndex).get();
        } else {
            throw new UnavailableProviderException(Thread.currentThread().getName() + ": 0 providers available");
        }
    }

    private boolean isValidProviderIndex(int providerIndex) {
        return providerIndex != -1;
    }

    private boolean isClusterCapacityUnderThreshold(List<Provider> healthyProviders) {
        return liveRequestCount.get() <= healthyProviders.size() * MAX_PARALLEL_REQUESTS_PER_PROVIDER;
    }
}

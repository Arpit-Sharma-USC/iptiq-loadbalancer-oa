package com.iptiq.heartbeat;


import com.iptiq.model.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.iptiq.constants.Constants.HEALTH_CHECK_FREQUENCY_SECONDS;
import static com.iptiq.constants.Constants.HEALTH_CHECK_INITIAL_DELAY_SECONDS;


public class HeartBeatChecker implements Runnable {

    private final List<Provider> registeredProviders;
    private final Map<Provider, Integer> healthCheckResults = new HashMap<>();
    private final Logger logger;
    private List<Provider> healthyProviders;

    public HeartBeatChecker(List<Provider> registeredProviders) {
        this.registeredProviders = registeredProviders;
        this.logger = Logger.getGlobal();
    }

    @Override
    public void run() {
        //check for recovery
        logger.info(Thread.currentThread().getName() + ": Checking " + registeredProviders.size() +
                " providers. ");
        registeredProviders.forEach(provider -> {
            if (provider.check()) {
                healthCheckResults.put(provider, healthCheckResults.get(provider) + 1);
            } else {
                healthCheckResults.put(provider, 0);
            }
        });

        this.healthyProviders = healthCheckResults.keySet()
                .stream()
                .filter(provider -> healthCheckResults.get(provider) >= 2)
                .collect(Collectors.toList());

        logger.info(Thread.currentThread().getName() + ": Remaining healthy " + healthyProviders.size() +
                " providers. ");
    }

    public void start() {
        registeredProviders.forEach(provider -> healthCheckResults.put(provider, 2));
        ScheduledExecutorService executorService;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this,
                HEALTH_CHECK_INITIAL_DELAY_SECONDS,
                HEALTH_CHECK_FREQUENCY_SECONDS,
                TimeUnit.SECONDS);
    }

    public List<Provider> getHealthyProviders() {
        return this.healthyProviders;
    }
}

/*
Copyright 2019 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ch.fhnw.imvs.hwgstereader.reader;

import ch.fhnw.imvs.hwgstereader.api.HwgSteFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Periodic reader for HWg-STE sensor nodes.
 *
 * @author mluppi
 */
public class HwgStePeriodicReader extends HwgSteReader {

    private static final Logger logger = LoggerFactory.getLogger(HwgStePeriodicReader.class);

    private final int pollDelay;
    private HwgSteMeasurementCallback callback;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future = null;

    /**
     * Constructs a {@link HwgStePeriodicReader} with a callback and poll delay.
     *
     * @param callback the callback to be run after the data is fetched
     * @param pollDelay the delay between periodic calls in seconds
     */
    public HwgStePeriodicReader(final HwgSteMeasurementCallback callback, final int pollDelay) {
        this.pollDelay = pollDelay;
        this.callback = callback;
        registerShutdownHook();
    }

    /**
     * Constructs a {@link HwgStePeriodicReader} with a custom fetcher (for testing purposes), callback and poll delay.
     *
     * @param fetcher a custom fetcher to be used internally
     * @param callback the callback to be run after the data is fetched
     * @param pollDelay the delay between periodic calls in seconds
     */
    HwgStePeriodicReader(final HwgSteFetcher fetcher, final HwgSteMeasurementCallback callback, final int pollDelay) {
        super(fetcher);
        this.pollDelay = pollDelay;
        this.callback = callback;
        registerShutdownHook();
    }

    /**
     * Starts reading the data from the attached nodes periodically and calls the callback each time.
     *
     * @throws RejectedStartException if the reader cannot be started due to a previous shutdown
     */
    public synchronized void start() {
        if (executor == null) {
            throw new RejectedStartException();
        }
        if (future == null) {
            future = executor.scheduleWithFixedDelay(() -> callback.handle(read()), 0, pollDelay, TimeUnit.SECONDS);
            logger.info("Started periodic reading with delay={} seconds after last cycle", this.pollDelay);
        } else {
            logger.warn("Polling should be started but is already running");
        }
    }

    /**
     * Stops reading the data from the attached nodes periodically and completes the currently running cycle.
     */
    public synchronized void stop() {
        if (future != null) {
            future.cancel(false);
            future = null;
            logger.info("Stopped periodic reading");
        } else {
            logger.warn("Polling should be stopped but is not running");
        }
    }

    /**
     * Shuts down the internal executor and allows reclamation of unused resources.
     * After calling this method, the reader cannot be used anymore.
     */
    public synchronized void shutdown() {
        shutdownInternal();
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Running shutdown hook to terminate polling and executor");
            shutdownInternal();
        }));
    }

    private void shutdownInternal() {
        if (future != null) {
            future.cancel(false);
            future = null;
        }
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(20, TimeUnit.SECONDS)) {
                    logger.warn("Executor did not shutdown in the specified time, forcing shutdown now");
                    executor.shutdownNow();
                }
                logger.info("Shutdown hook completed");
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            executor = null;
        }
        callback = null;
    }
}

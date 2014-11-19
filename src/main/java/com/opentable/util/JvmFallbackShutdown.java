/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opentable.util;

import java.lang.Thread.State;
import java.time.Duration;

import com.google.common.base.Joiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Utility class to log orphaned threads that do not shut down cleanly.
 */
public class JvmFallbackShutdown {
    private static final Logger LOG = LoggerFactory.getLogger(JvmFallbackShutdown.class);

    /**
     * Spawn a background thread which will wait for the specified time, then print out any
     * remaining threads and forcibly kill the JVM.  Intended to be used before an action that <i>should</i>
     * shut down the JVM but is not guaranteed to - will scream for help if anything goes wrong with shutdown.
     * @param waitTime how long to wait
     */
    public static void fallbackTerminate(Duration waitTime) {
        Thread fallbackTerminateThread = new Thread(() -> fallbackKill(waitTime));
        fallbackTerminateThread.setName("T-1000");
        fallbackTerminateThread.setDaemon(true);
        fallbackTerminateThread.start();
    }

    @SuppressFBWarnings("DM_EXIT")
    private static void fallbackKill(Duration waitTime) {
        LOG.info("Service problem detected, fallback kill in {}...", waitTime.toString().substring(2));
        try {
            Thread.sleep(waitTime.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("in terminate thread", e);
        }
        Thread.getAllStackTraces().entrySet().stream()
            .filter(e -> !e.getKey().isDaemon())
            .filter(e -> e.getKey().getState() != State.TERMINATED)
            .forEach(e -> {
                final Thread t = e.getKey();
                LOG.error("Thread {} {} '{}': \n{}\n", t.getId(), t.getState(), t.getName(), Joiner.on('\n').join(e.getValue()));
        });
        LOG.error("===UNCLEAN SHUTDOWN===");
        System.exit(254);
    }
}

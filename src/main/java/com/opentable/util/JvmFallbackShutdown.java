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
import java.util.Arrays;

import com.google.common.base.Joiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Utility class to log orphaned threads that do not shut down cleanly.
 */
public final class JvmFallbackShutdown {
    private static final Logger LOG = LoggerFactory.getLogger(JvmFallbackShutdown.class);

    private JvmFallbackShutdown() { }

    /**
     * Spawn a background thread which will wait for the specified time, then print out any
     * remaining threads and forcibly kill the JVM.  Intended to be used before an action that <i>should</i>
     * shut down the JVM but is not guaranteed to - will scream for help if anything goes wrong with shutdown.
     * @param waitTime how long to wait
     */
    public static void fallbackTerminate(Duration waitTime) {
        Throwable source = new Throwable();
        source.fillInStackTrace();

        if (inTests(source)) {
            LOG.warn("Asked to register in a test environment.  You probably don't want this.");
            return;
        }

        Thread fallbackTerminateThread = new Thread(() -> fallbackKill(waitTime, source));
        fallbackTerminateThread.setName("T-1000");
        fallbackTerminateThread.setDaemon(true);
        fallbackTerminateThread.start();
    }

    @SuppressFBWarnings("DM_EXIT")
    @SuppressWarnings("PMD.DoNotCallSystemExit")
    private static void fallbackKill(Duration waitTime, Throwable source) {
        LOG.info("Service problem detected, fallback kill in {}...", waitTime.toString().substring(2));
        try {
            Thread.sleep(waitTime.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("in terminate thread", e);
        }

        LOG.error("Fallback kill timeout expired.  Logging termination source followed by all thread stacks", source);

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

    /**
     * Determine if this is being run as part of a test execution.
     * This is done by seeing if surefire or junit is in the stacktrace
     * @param source the exception to use to check if this is part of a test execution
     * @return true if this is probably part of a test execution
     */
    static boolean inTests(Throwable source) {
        return Arrays.stream(source.getStackTrace())
            .anyMatch(e -> e.getClassName().contains("surefire.booter.ForkedBooter") ||
                           e.getClassName().contains("org.junit"));
    }
}

package com.opentable.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transformations for various functional interfaces to modify exceptional behavior.
 */
public class ExceptionSwallower
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionSwallower.class);

    /**
     * Transform a Runnable to log and then swallow any thrown exceptions.
     */
    public static Runnable swallowExceptions(Runnable in)
    {
        return () -> {
            try {
                in.run();
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
                if (t instanceof Error) {
                    throw t;
                }
            }
        };
    }
}

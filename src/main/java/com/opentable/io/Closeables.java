package com.opentable.io;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Closeables
{
    private static final Logger LOG = LoggerFactory.getLogger(Closeables.class);

    public static Runnable closeAsRunnable(Closeable c)
    {
        return () -> {
            try {
                c.close();
            } catch (IOException e) {
                LOG.error("While closing " + c, e);
            }
        };
    }
}

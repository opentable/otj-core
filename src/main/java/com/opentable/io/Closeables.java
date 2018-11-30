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
package com.opentable.io;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for dealing with {@link Closeable}s
 */
public final class Closeables
{
    private static final Logger LOG = LoggerFactory.getLogger(Closeables.class);

    private Closeables() {
        /* utility class */
    }
    /**
     * Create a {@link Runnable} that when run closes the provided {@link Closeable}.
     * If an {@link IOException} is thrown while closing, it'll be caught and logged.
     * @param c the closeable to close when the runnable is run
     * @return a runnable that when run closes the closable
     */
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

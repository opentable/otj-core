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
package com.opentable.callback;


/**
 * Thrown by {@link com.opentable.callback.Callback#call(Object)} to signal that the caller should stop
 * processing data (and therefore calling the callback method).
 */
public class CallbackRefusedException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * Create a CallbackRefusedException to signal that the caller should stop
     * processing data (and therefore calling the callback method).
     */
    public CallbackRefusedException()
    {
    }

    /**
     * Create a CallbackRefusedException to signal that the caller should stop
     * processing data (and therefore calling the callback method).
     * @param e the underlying issue that is causing is stop processing data
     */
    public CallbackRefusedException(final Exception e)
    {
        super(e);
    }

    /**
     * Create a CallbackRefusedException to signal that the caller should stop
     * processing data (and therefore calling the callback method).
     * @param message the error message template string, formatted by passing it and args to {@link String#format(String, Object...)}
     * @param args the args to pass to {@link String#format(String, Object...)} together with the message template to create the error message
     */
    public CallbackRefusedException(final String message, final Object [] args)
    {
        super(String.format(message, args));
    }

    /**
     * Create a CallbackRefusedException to signal that the caller should stop
     * processing data (and therefore calling the callback method).
     * @param e the underlying issue that is causing is stop processing data
     * @param message the error message template string, formatted by passing it and args to {@link String#format(String, Object...)}
     * @param args the args to pass to {@link String#format(String, Object...)} together with the message template to create the error message
     */
    public CallbackRefusedException(final Exception e, final String message, final Object [] args)
    {
        super(String.format(message, args), e);
    }
}

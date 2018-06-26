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
package com.opentable.uuid;

import java.util.UUID;

/**
 * Historically, a class that provides an alternate implementation of {@link
 * UUID#fromString(String)} and {@link UUID#toString()}.
 *
 * Previously worked around bug
 * https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8006627
 * but as of jdk9 no longer needed.
 *
 * <p> The version in the JDK uses {@link String#split(String)}
 * which does not compile the regular expression that is used for splitting
 * the UUID string and results in the allocation of multiple strings in a
 * string array. We decided to write {@link FastUUID} when we ran into
 * performance issues with the garbage produced by the JDK class.
 *
 * @deprecated replace with vanilla {@link UUID}
 */
@Deprecated
public final class FastUUID {
    private FastUUID() {}

    public static UUID fromString(String str) {
        return UUID.fromString(str);
    }

    public static String toString(UUID uuid)
    {
        return uuid.toString();
    }
}

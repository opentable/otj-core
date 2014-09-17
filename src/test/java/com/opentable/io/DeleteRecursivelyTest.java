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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DeleteRecursivelyTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testDeleteRecursively() throws Exception
    {
        final File root = folder.getRoot();
        final File test1 = folder.newFile("test1");
        final File dir1 = folder.newFolder("dir1");
        final File test2 = new File(dir1, "test2");
        test2.createNewFile();

        assertTrue(root.exists());
        assertTrue(test1.exists());
        assertTrue(dir1.exists());
        assertTrue(test2.exists());

        Files.walkFileTree(root.toPath(), DeleteRecursively.INSTANCE);

        assertFalse(root.exists());
        assertFalse(test1.exists());
        assertFalse(dir1.exists());
        assertFalse(test2.exists());
    }
}

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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A {@link FileVisitor} that deletes all files and directories recursively from it's starting point.
 * To use it call {@link Files#walkFileTree(Path, FileVisitor)} with a {@link Path} to start deleting from.
 */
public final class DeleteRecursively extends SimpleFileVisitor<Path>
{
    /**
     * Singleton instance of a {@link DeleteRecursively}.
     */
    public static final FileVisitor<Path> INSTANCE = new DeleteRecursively();

    private DeleteRecursively() { }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
    {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
    {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
    }
}

OpenTable Core Types
====================

[![Build Status](https://travis-ci.org/opentable/otj-core.svg)](https://travis-ci.org/opentable/otj-core)

Component Charter
-----------------

* Hold core types that are not specific to any one service
* May only hold types that could reasonably fit in a "core Java library" e.g. Apache commons, JDK, or Guava

----

Callbacks
---------

The Java core library does not supply a general purpose `Callback<T>` type.  With Java 8, it includes `Consumer<T>`, but
this functional interface is geared more towards consumption of single items.  In particular, there is no way to signal
termination of the callback.

[Callback](https://github.com/opentable/otj-core/blob/master/src/main/java/com/opentable/callback/Callback.java)
fills this void.  A few [utilities](https://github.com/opentable/otj-core/tree/master/src/main/java/com/opentable/callback)
are provided to go along.

UUIDs
-----

The JDK `UUID` class has horribly slow `toString` and `fromString` implementations.  This has been filed as a
bug, but in the interim there is a [FastUUID](https://github.com/opentable/otj-core/blob/master/src/main/java/com/opentable/uuid/FastUUID.java)
class.

File Handling
-------------

We provide a `FileVisitor` to take a directory and
[DeleteRecursively](https://github.com/opentable/otj-core/blob/master/src/main/java/com/opentable/io/DeleteRecursively.java).

Display Formatting
------------------

Services are much nicer when byte counts or throughput rates are
[pretty-printed](https://github.com/opentable/otj-core/blob/master/src/main/java/com/opentable/util/Sizes.java).

----

Component Restriction
---------------------

* All changes *must* be approved by a senior backend engineer or architect who is not the contributor.

This library must remain extremely small.  There is a very high bar for inclusion into this library.

Component Level
---------------

*Foundation component*

* Must never depend on any other component.
* Should minimize its dependency footprint.

----
Copyright (C) 2014 OpenTable, Inc.

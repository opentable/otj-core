package com.opentable.function;

import java.io.IOException;

@FunctionalInterface
public interface IOFunction<In, Out>
{
    Out apply(In in) throws IOException;
}

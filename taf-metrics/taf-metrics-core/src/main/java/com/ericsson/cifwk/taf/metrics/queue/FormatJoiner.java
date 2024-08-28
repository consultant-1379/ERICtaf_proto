package com.ericsson.cifwk.taf.metrics.queue;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class FormatJoiner {

    private final Joiner joiner;
    private final String format;

    private FormatJoiner(Joiner joiner, String format) {
        this.joiner = joiner;
        this.format = format;
    }

    public static FormatJoiner with(String separator, String format) {
        Joiner joiner = Joiner.on(separator)
                .skipNulls();
        return new FormatJoiner(joiner, format);
    }

    public final String join(Iterable<?> parts) {
        return appendTo(new StringBuilder(), parts).toString();
    }

    public StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
        return joiner.appendTo(builder, mapFormat(parts));
    }

    private Iterable<String> mapFormat(Iterable<?> parts) {
        return Iterables.transform(parts, new Function<Object, String>() {
            @Override
            public String apply(Object input) {
                return String.format(format, input);
            }
        });
    }


}

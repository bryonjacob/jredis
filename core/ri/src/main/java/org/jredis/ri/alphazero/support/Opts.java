package org.jredis.ri.alphazero.support;

import org.jredis.ClientRuntimeException;
import org.jredis.Opt;
import org.jredis.protocol.Command;

public class Opts {
    private static final Opt WITHSCORES_OPT = noargs(Command.Options.WITHSCORES);
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final String SPACE = " ";

    public static Opt WITHSCORES() { return WITHSCORES_OPT; }

    public static Opt LIMIT(final long offset, final long count) {
        return new Opt() {
            public Command.Options getOption() {
                return Command.Options.LIMIT;
            }

            public String getSpec() {
                Assert.inRange(count, 0, Long.MAX_VALUE,
                        "offset in LIMIT clause", ClientRuntimeException.class);
                Assert.inRange(count, offset, Long.MAX_VALUE,
                        "count in LIMIT clause (when offset=" + offset + ")", ClientRuntimeException.class);
                return Command.Options.LIMIT.name() + SPACE + offset + SPACE + count;
            }
        };
    }

    public static Opt noargs(final Command.Options option) {
        return new Opt() {
            public Command.Options getOption() {
                return option;
            }

            public String getSpec() {
                return option.name();
            }
        };
    }

    public static byte[] toSpecBytes(Command command, Opt... opts) {
        if (opts.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        StringBuilder builder = new StringBuilder();
        for (Opt opt : opts) {
            if (!command.allowedOptions.contains(opt.getOption())) {
                throw new ClientRuntimeException(
                        String.format("command %s does not support option %s", command, opt.getOption()));
            }
            builder.append(SPACE).append(opt.getSpec());
        }
        return builder.toString().getBytes();
    }
}

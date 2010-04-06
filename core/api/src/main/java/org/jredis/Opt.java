package org.jredis;

import org.jredis.protocol.Command;

public interface Opt {
    Command.Options getOption();
    String getSpec();
}

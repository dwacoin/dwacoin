/*
 * Copyright © 2013-2016 The Nxt Core Developers.
 * Copyright © 2016-2017 Jelurida IP B.V.
 *
 * See the LICENSE.txt file at the top-level directory of this distribution
 * for licensing information.
 *
 * Unless otherwise agreed in a custom licensing agreement with Jelurida B.V.,
 * no part of the Nxt software, including this file, may be copied, modified,
 * propagated, or distributed except according to the terms contained in the
 * LICENSE.txt file.
 *
 * Removal or modification of this copyright notice is prohibited.
 *
 */

package dwa.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * Java LogManager extension for use with Dwa
 */
public class DwaLogManager extends LogManager {

    /** Logging reconfiguration in progress */
    private volatile boolean loggingReconfiguration = false;

    /**
     * Create the Dwa log manager
     *
     * We will let the Java LogManager create its shutdown hook so that the
     * shutdown context will be set up properly.  However, we will intercept
     * the reset() method so we can delay the actual shutdown until we are
     * done terminating the Dwa processes.
     */
    public DwaLogManager() {
        super();
    }

    /**
     * Reconfigure logging support using a configuration file
     *
     * @param       inStream            Input stream
     * @throws      IOException         Error reading input stream
     * @throws      SecurityException   Caller does not have LoggingPermission("control")
     */
    @Override
    public void readConfiguration(InputStream inStream) throws IOException, SecurityException {
        loggingReconfiguration = true;
        super.readConfiguration(inStream);
        loggingReconfiguration = false;
    }

    /**
     * Reset the log handlers
     *
     * This method is called to reset the log handlers.  We will forward the
     * call during logging reconfiguration but will ignore it otherwise.
     * This allows us to continue to use logging facilities during Dwa shutdown.
     */
    @Override
    public void reset() {
        if (loggingReconfiguration)
            super.reset();
    }

    /**
     * Dwa shutdown is now complete, so call LogManager.reset() to terminate
     * the log handlers.
     */
    void dwaShutdown() {
        super.reset();
    }
}

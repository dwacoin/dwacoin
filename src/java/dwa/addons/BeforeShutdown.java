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

package dwa.addons;

import dwa.Dwa;
import dwa.util.Logger;

public final class BeforeShutdown implements AddOn {

    final String beforeShutdownScript = Dwa.getStringProperty("dwa.beforeShutdownScript");

    @Override
    public void shutdown() {
        if (beforeShutdownScript != null) {
            try {
                Runtime.getRuntime().exec(beforeShutdownScript);
            } catch (Exception e) {
                Logger.logShutdownMessage("Failed to run after start script: " + beforeShutdownScript, e);
            }
        }
    }

}

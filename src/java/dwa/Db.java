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

package dwa;

import dwa.db.BasicDb;
import dwa.db.TransactionalDb;

public final class Db {

    public static final String PREFIX = Constants.isTestnet ? "dwa.testDb" : "dwa.db";
    public static final TransactionalDb db = new TransactionalDb(new BasicDb.DbProperties()
            .maxCacheSize(Dwa.getIntProperty("dwa.dbCacheKB"))
            .dbUrl(Dwa.getStringProperty(PREFIX + "Url"))
            .dbType(Dwa.getStringProperty(PREFIX + "Type"))
            .dbDir(Dwa.getStringProperty(PREFIX + "Dir"))
            .dbParams(Dwa.getStringProperty(PREFIX + "Params"))
            .dbUsername(Dwa.getStringProperty(PREFIX + "Username"))
            .dbPassword(Dwa.getStringProperty(PREFIX + "Password", null, true))
            .maxConnections(Dwa.getIntProperty("dwa.maxDbConnections"))
            .loginTimeout(Dwa.getIntProperty("dwa.dbLoginTimeout"))
            .defaultLockTimeout(Dwa.getIntProperty("dwa.dbDefaultLockTimeout") * 1000)
            .maxMemoryRows(Dwa.getIntProperty("dwa.dbMaxMemoryRows"))
    );

    public static void init() {
        db.init(new DwaDbVersion());
    }

    static void shutdown() {
        db.shutdown();
    }

    private Db() {} // never

}

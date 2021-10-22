package dwa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dwa.db.DbClause;
import dwa.db.DbIterator;
import dwa.db.DbKey;
import dwa.db.VersionedEntityDbTable;

public class AssetFeeRate {
    private static final DbKey.LongKeyFactory<AssetFeeRate> assetDbKeyFactory = new DbKey.LongKeyFactory<AssetFeeRate>("asset") {

        @Override
        public DbKey newKey(AssetFeeRate asset) {
            return asset.dbKey;
        }

    };

    private static final VersionedEntityDbTable<AssetFeeRate> assetFeeTable = new VersionedEntityDbTable<AssetFeeRate>("asset_fee_rate", assetDbKeyFactory,null) {

        @Override
        protected AssetFeeRate load(Connection con, ResultSet rs, DbKey dbKey) throws SQLException {
            return new AssetFeeRate(rs, dbKey);
        }

        @Override
        protected void save(Connection con, AssetFeeRate asset) throws SQLException {
            asset.save(con);
        }

    };

    public static DbIterator<AssetFeeRate> getAllAssetFees() {
        return assetFeeTable.getAllRaw(0,Integer.MAX_VALUE);
    }

    public static int getCount() {
        return assetFeeTable.getCount();
    }

    public static void addAssetFee(long assetId,long feeAsk,long feeAskCancel,long feeTransfer) {
        assetFeeTable.insertRaw(new AssetFeeRate(assetId, feeAsk,feeAskCancel,feeTransfer));
    }

    public static void deleteAssetFee(long assetId) {
        assetFeeTable.deleteRaw(new DbClause.LongClause("asset",assetId));
    }

    static void init() {}


    private final long assetId;
    private final DbKey dbKey;
    private final long feeAsk;
    private final long feeAskCancel;
    private final long feeTransfer;

    private AssetFeeRate(long assetId, long feeAsk, long feeAskCancel, long feeTransfer) {
        this.assetId = assetId;
        this.dbKey = assetDbKeyFactory.newKey(this.assetId);
        this.feeAsk = feeAsk;
        this.feeAskCancel = feeAskCancel;
        this.feeTransfer = feeTransfer;
    }

    private AssetFeeRate(ResultSet rs, DbKey dbKey) throws SQLException {
        this.dbKey = dbKey;
        this.assetId = rs.getLong("asset");
        this.feeAsk = rs.getLong("fee_ask");
        this.feeAskCancel = rs.getLong("fee_ask_cancel");
        this.feeTransfer = rs.getLong("fee_transfer");
    }

    public static AssetFeeRate getAssetFeeRate(long assetId) {
        AssetFeeRate assetFeeRate = assetFeeTable.get(assetDbKeyFactory.newKey(assetId));
        if(assetFeeRate == null) return new AssetFeeRate(assetId,0,0,0);
        else return assetFeeRate;
    }

    public static boolean isFeeSet(long assetId) {
        AssetFeeRate assetFeeRate = assetFeeTable.get(assetDbKeyFactory.newKey(assetId));
        if(assetFeeRate == null) return false;
        else return true;
    }

    private void save(Connection con) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement("MERGE INTO asset_fee_rate "
                + "(asset, fee_ask, fee_ask_cancel, fee_transfer) "
                + "KEY(asset) "
                + "VALUES (?, ?, ?, ?)")) {
            int i = 0;
            pstmt.setLong(++i, this.assetId);
            pstmt.setLong(++i, this.feeAsk);
            pstmt.setLong(++i, this.feeAskCancel);
            pstmt.setLong(++i, this.feeTransfer);
            pstmt.executeUpdate();
        }
    }

    public long getAssetId() {
        return assetId;
    }
    public long getFeeAsk() {
        return feeAsk;
    }
    public long getFeeAskCancel() {
        return feeAskCancel;
    }
    public long getFeeTransfer() {
        return feeTransfer;
    }

}

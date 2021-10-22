package dwa.http;

import static dwa.http.JSONResponses.INCORRECT_SECRET_PHRASE;
import static dwa.http.JSONResponses.incorrect;
import static dwa.http.JSONResponses.unknown;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import dwa.Account;
import dwa.Asset;
import dwa.AssetFeeRate;
import dwa.Attachment;
import dwa.DwaException;
import dwa.crypto.Crypto;
import dwa.db.DbIterator;
import dwa.util.Convert;
import dwa.util.Logger;

public class GetAssetFeeRate extends APIServlet.APIRequestHandler {

    static final GetAssetFeeRate instance = new GetAssetFeeRate();

    private GetAssetFeeRate() {
        super(new APITag[] {APITag.AE}, "asset");
    }

    Asset getAsset(HttpServletRequest request) throws ParameterException {
        return ParameterParser.getAsset(request);
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws DwaException {
        Asset asset = getAsset(req);
        AssetFeeRate assetFeeRate = AssetFeeRate.getAssetFeeRate(asset.getId());
        if(assetFeeRate.getFeeTransfer()==0){
            return unknown("Asset Fee Rate");
        }
        JSONObject item = new JSONObject();
        item.put("asset", assetFeeRate.getAssetId());
        item.put("fee_ask", assetFeeRate.getFeeAsk());
        item.put("fee_ask_cancel", assetFeeRate.getFeeAskCancel());
        item.put("fee_transfer", assetFeeRate.getFeeTransfer());
        item.put("status",1);
        return item;
    }

    @Override
    protected boolean allowRequiredBlockParameters() {
        return false;
    }
}

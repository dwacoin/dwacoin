package dwa.http;

import static dwa.http.JSONResponses.INCORRECT_ACCOUNT;
import static dwa.http.JSONResponses.INCORRECT_SECRET_PHRASE;
import static dwa.http.JSONResponses.MISSING_ACCOUNT;
import static dwa.http.JSONResponses.MISSING_SECRET_PHRASE;
import static dwa.http.JSONResponses.UNKNOWN_ACCOUNT;
import static dwa.http.JSONResponses.incorrect;

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

public class SetAssetFeeRate extends CreateTransaction {

    static final SetAssetFeeRate instance = new SetAssetFeeRate();

    private SetAssetFeeRate() {
        super(new APITag[] {APITag.AE, APITag.CREATE_TRANSACTION},"asset","fee_ask","fee_ask_cancel","fee_transfer");
    }

    Asset getAsset(HttpServletRequest request) throws ParameterException {
        return ParameterParser.getAsset(request);
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest request) throws DwaException {
        JSONObject response = new JSONObject();
        response.put("status",-1);
        String secretPhrase = ParameterParser.getSecretPhrase(request, true);
        byte[] publicKey = Crypto.getPublicKey(secretPhrase);
        Account accountSender = Account.getAccount(publicKey);
        if(accountSender==null){
            return INCORRECT_SECRET_PHRASE;
        }
        Asset asset = getAsset(request);
        if(asset.getAccountId()!=accountSender.getId()){
            return INCORRECT_SECRET_PHRASE;
        }
        long feeAsk = ParameterParser.getLong(request,"fee_ask",0,Long.MAX_VALUE,true);
        long feeAskCancel = ParameterParser.getLong(request,"fee_ask_cancel",0,Long.MAX_VALUE,true);
        long feeTransfer = ParameterParser.getLong(request,"fee_transfer",0,Long.MAX_VALUE,true);
        //Create Transaction
        response.put("status",1);
        Attachment attachment = new Attachment.ColoredCoinsAssetFeeRate(asset.getId(),feeAsk,feeAskCancel,feeTransfer);
        return createTransaction(request, accountSender, 0, 0, attachment);
    }

    @Override
    protected boolean requireBlockchain() {
        return false;
    }

    @Override
    protected boolean requireFullClient() {
        return false;
    }

    @Override
    protected boolean requirePassword() {
        return false;
    }

}

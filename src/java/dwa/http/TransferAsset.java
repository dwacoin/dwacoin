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

package dwa.http;

import dwa.Account;
import dwa.Asset;
import dwa.AssetFeeRate;
import dwa.Attachment;
import dwa.DwaException;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static dwa.http.JSONResponses.NOT_ENOUGH_ASSETS;
import static dwa.http.JSONResponses.unknown;

public final class TransferAsset extends CreateTransaction {

    static final TransferAsset instance = new TransferAsset();

    private TransferAsset() {
        super(new APITag[] {APITag.AE, APITag.CREATE_TRANSACTION}, "recipient", "asset", "quantityQNT");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws DwaException {

        long recipient = ParameterParser.getAccountId(req, "recipient", true);

        Asset asset = ParameterParser.getAsset(req); //Got the asset, from asset, got the account
        AssetFeeRate assetFeeRate = AssetFeeRate.getAssetFeeRate(asset.getId());
        if(assetFeeRate.getFeeTransfer()==0){
            return unknown("Asset Fee Rate");
        }
        long quantityQNT = ParameterParser.getQuantityQNT(req);
        Account accountSender = ParameterParser.getSenderAccount(req);

        Account accountIssuer = Account.getAccount(asset.getAccountId());

        Attachment attachment = new Attachment.ColoredCoinsAssetTransfer(asset.getId(), quantityQNT,assetFeeRate.getFeeTransfer());
        try {
            return createTransaction(req, accountSender,accountIssuer, recipient, 0, attachment);
        } catch (DwaException.InsufficientBalanceException e) {
            return NOT_ENOUGH_ASSETS;
        }
    }

}

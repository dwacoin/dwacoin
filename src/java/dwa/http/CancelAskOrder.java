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
import dwa.Order;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static dwa.http.JSONResponses.UNKNOWN_ORDER;
import static dwa.http.JSONResponses.unknown;

public final class CancelAskOrder extends CreateTransaction {

    static final CancelAskOrder instance = new CancelAskOrder();

    private CancelAskOrder() {
        super(new APITag[] {APITag.AE, APITag.CREATE_TRANSACTION}, "order");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws DwaException {
        long orderId = ParameterParser.getUnsignedLong(req, "order", true);
        Account account = ParameterParser.getSenderAccount(req);
        Order.Ask orderData = Order.Ask.getAskOrder(orderId);
        Asset asset = Asset.getAsset(orderData.getAssetId());
        AssetFeeRate assetFeeRate = AssetFeeRate.getAssetFeeRate(asset.getId());
        if(assetFeeRate.getFeeTransfer()==0){
            return unknown("Asset Fee Rate");
        }
        if (orderData == null || orderData.getAccountId() != account.getId()) {
            return UNKNOWN_ORDER;
        }
        Attachment attachment = new Attachment.ColoredCoinsAskOrderCancellation(orderId,assetFeeRate.getFeeAskCancel());
        return createTransaction(req, account, attachment);
    }

}

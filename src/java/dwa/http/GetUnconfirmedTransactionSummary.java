package dwa.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import dwa.Dwa;
import dwa.DwaException;

public class GetUnconfirmedTransactionSummary extends APIServlet.APIRequestHandler {

    static final GetUnconfirmedTransactionSummary instance = new GetUnconfirmedTransactionSummary();

    private GetUnconfirmedTransactionSummary() {
        super(new APITag[] {APITag.TRANSACTIONS, APITag.ACCOUNTS});
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest request) throws DwaException {
        JSONObject response = new JSONObject();
        response.put("count", Dwa.getTransactionProcessor().countUnconfirmedTransaction());
        return response;
    }

    @Override
    protected boolean allowRequiredBlockParameters() {
        return false;
    }

    @Override
    protected boolean requireBlockchain() {
        return false;
    }
}

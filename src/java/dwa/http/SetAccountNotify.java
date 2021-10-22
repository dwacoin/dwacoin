package dwa.http;

import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import dwa.Constants;
import dwa.DwaException;
import dwa.util.Convert;

import static dwa.http.JSONResponses.incorrect;
import static dwa.http.JSONResponses.missing;

public class SetAccountNotify extends APIServlet.APIRequestHandler {

    static final SetAccountNotify instance = new SetAccountNotify();
    private SetAccountNotify() {
        super(new APITag[] {APITag.ACCOUNTS},"url_post", "account", "action (get,put,delete)");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest request) throws DwaException {
        JSONObject response = new JSONObject();
        response.put("status", -1);
        String action = Convert.nullToEmpty(request.getParameter("action (get,put,delete)")).trim(); //ACTION
        if(action.isEmpty()){
            throw new ParameterException(missing("action"));
        }
        if(!action.equalsIgnoreCase("get")&&!action.equalsIgnoreCase("put")&&!action.equalsIgnoreCase("delete")){
            throw new ParameterException(incorrect("action"));
        }
        final String urlPost = Convert.nullToEmpty(request.getParameter("url_post")).trim(); //Single URLPOST
        Long accountId = ParameterParser.getAccountId(request, true);
        String account = Convert.nullToEmpty(request.getParameter("account")).trim(); //Single URLPOST
        if(action.equalsIgnoreCase("put")){
            //Verify the URL
            try{
                new URL(urlPost);
            }catch (MalformedURLException malformedURLException){
                throw new ParameterException(incorrect("url_post"));
            }
            Constants.accountNotificationRecipient.put(""+accountId,new HashMap<String,String>(){{put("account",account);put("url_post",urlPost);}});
            response.put("accountId", accountId);
            response.put("account", account);
            response.put("url_post", urlPost);
            response.put("status", 1);
        }else if(action.equalsIgnoreCase("delete")){
            response.put("status", 1);
            response.put("accountId", accountId);
            response.put("account", account);
            Constants.accountNotificationRecipient.remove(""+accountId);
        }else if(action.equalsIgnoreCase("get")&&Constants.accountNotificationRecipient.get(""+accountId)!=null){
            response.put("accountId", accountId);
            response.put("account", account);
            response.put("url_post", Constants.accountNotificationRecipient.get(""+accountId).get("url_post"));
            response.put("status", 1);
        }
        return response;
    }

    @Override
    protected boolean requirePost() {
        return true;
    }

    @Override
    protected boolean requirePassword() {
        return true;
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

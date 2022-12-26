package bizibox.idbKeyDesktop;

import bizibox.idbKeyDesktop.Base32String._A;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringEscapeUtils;

@Path("/getIDBKey")
public class GetIDBKey {
    private static final long E = -8712819859213309826L;
    Response.ResponseBuilder rb = null;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getIDBKey(String inputJsonObj) throws IOException {
        JSONObject objRes = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            JSONObject objVal = (JSONObject) parser.parse(inputJsonObj);
            String idbBKey = (String) objVal.get("idbBKey");
            if (null != idbBKey && idbBKey.length() >= 16) {
                Base32String.decode(idbBKey);
                try {
                    byte[] var4 = Base32String.decode(A(idbBKey));
                    Mac var5 = Mac.getInstance("HMACSHA1");
                    var5.init(new SecretKeySpec(var4, ""));
                    PasscodeGenerator var6 = new PasscodeGenerator(var5);
                    String varCode = ((PasscodeGenerator) var6).generateTimeoutCode();
                    int varTime = (int) (System.currentTimeMillis() % 30000L / 1000L);
//                    System.out.println(varCode);
//                    System.out.println("time:");
//                    System.out.println(varTime);

                    objRes.put("code", varCode);
                    objRes.put("time", varTime);
                    return Response.status(200).entity(objRes).build();
                } catch (Exception varEx) {
                   // System.out.printf("fail" + varEx);
                    objRes.put("code", false);
                    objRes.put("time", null);
                    return Response.status(200).entity(objRes).build();
                }
            } else {
                objRes.put("code", "short");
                objRes.put("time", null);
               // System.out.printf("קוד התקנה קצר מדי");
                return Response.status(200).entity(objRes).build();
            }
        } catch (_A errEx) {
            objRes.put("code", "Illegal");
            objRes.put("time", null);
           // System.out.printf("קוד התקנה לא חוקי");
            return Response.status(200).entity(objRes).build();
        } catch (ParseException pe) {
            objRes.put("code", false);
            objRes.put("time", null);
           // System.out.println("position: " + pe.getPosition());
            return Response.status(200).entity("[]").build();
        }
    }

    private String A(String var1) {
        StringBuilder var2 = new StringBuilder();
        var2.append(var1.substring(0, 1));
        var2.append(var1.substring(14, 15));
        var2.append(var1.substring(2, 14));
        var2.append(var1.substring(1, 2));
        var2.append(var1.substring(15));
        String var3 = var2.toString();
        return var3;
    }
}



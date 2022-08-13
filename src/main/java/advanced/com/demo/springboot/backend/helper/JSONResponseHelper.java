package advanced.com.demo.springboot.backend.helper;

import java.util.HashMap;
import java.util.Map;

public class JSONResponseHelper {
    public Map<String, String> res = new HashMap<>();

    public static Map<String, String> CustomResponse(String msg){
        JSONResponseHelper jsonResponseHelper = new JSONResponseHelper();
        jsonResponseHelper.res.put("msg", msg);
        return jsonResponseHelper.res;
    }
}

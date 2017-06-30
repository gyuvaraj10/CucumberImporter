package sample.com.app.utils;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Created by ctl-user on 24/1/2016.
 */
public class JsonUtil {

    JSONParser parser = new JSONParser();
    JSONArray objectA;

    public void load(String json) {
        try {
            objectA = (JSONArray) parser.parse(json);
        }
        catch (Exception ignore){

        }
    }

    public String updateScenarioTagNode(String jiraKey, String testSceName) {
        JSONObject jsonObject = (JSONObject) objectA.get(0);
        JSONArray objectArray = ((JSONArray) jsonObject.get("elements"));
        int sceLine=0;
        JSONObject scenario = new JSONObject();
        for(Object object: objectArray.toArray()){
            if(((JSONObject)object).get("name").toString().contains(testSceName)){
                scenario = ((JSONObject)object);
                sceLine = Integer.valueOf(((JSONObject)object).get("line").toString());
                break;
            }
        }
        JSONArray array = (JSONArray) scenario.get("tags");
        if(sceLine != 0) {
            array.add(tagStructure(sceLine - 1, jiraKey));
        }
        else{
            array.add(tagStructure(0, jiraKey));
        }
        return  objectA.toString();
    }

    JSONObject tagStructure(int line, String jiraKey) {
            JSONObject tagObj = new JSONObject();
            tagObj.put("line", new Integer(line));
            tagObj.put("name", "@JIRA="+jiraKey);
            return tagObj;
//            StringBuilder builder = new StringBuilder();
//            builder.append({\"line\":");
//            builder.append(line);
//            builder.append(",name:");
//            builder.append("\"@JIRA="+jiraKey+"\"}");
//            return builder.toString();
//            return "{line: " + line + ", name: \"@JIRA=" + jiraKey + "}";
        }
}

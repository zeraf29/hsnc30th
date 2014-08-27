package hsnc.eaglero.gcm;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GCMinfoClass {
	private String reg_id;
	private String s_key;
	
	
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getS_key() {
		return s_key;
	}
	public void setS_key(String s_key) {
		this.s_key = s_key;
	}
	public JsonObject get_jsonObject(){
		JsonObject jsonObject = new JsonObject();
		JsonObject jsonObject2 = new JsonObject();
		//JsonArray jArray = new JsonArray();
	    jsonObject.addProperty("regId", this.reg_id);
	    jsonObject.addProperty("sKey", this.s_key);
	    //jArray.add(jsonObject);
	    //jsonObject2.add("data", jArray);
	    jsonObject2.add("data", jsonObject);
	    //System.out.println(jsonObject);
	    Log.e("json",jsonObject2.toString());
		return jsonObject2;
	}
	
	
}

package hsnc.eaglero.main;

import android.util.Log;

import com.google.gson.JsonObject;

public class CouponInfoClass {
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public JsonObject get_jsonObject(){
		JsonObject jsonObject = new JsonObject();
		JsonObject jsonObject2 = new JsonObject();
		// JsonArray jArray = new JsonArray();
		jsonObject.addProperty("title", this.title);
		//jsonObject.addProperty("sKey", this.s_key);
		// jArray.add(jsonObject);
		// jsonObject2.add("data", jArray);
		jsonObject2.add("data", jsonObject);
		// System.out.println(jsonObject);
		Log.e("json", jsonObject2.toString());
		return jsonObject2;
	}
	
}

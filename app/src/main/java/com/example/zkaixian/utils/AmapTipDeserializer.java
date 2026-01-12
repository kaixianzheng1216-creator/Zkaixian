package com.example.zkaixian.utils;

import com.example.zkaixian.pojo.AmapTip;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class AmapTipDeserializer implements JsonDeserializer<AmapTip> {
    @Override
    public AmapTip deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        
        AmapTip tip = new AmapTip();

        tip.setId(getStringOrEmpty(jsonObject, "id"));
        tip.setName(getStringOrEmpty(jsonObject, "name"));
        tip.setDistrict(getStringOrEmpty(jsonObject, "district"));
        tip.setAdcode(getStringOrEmpty(jsonObject, "adcode"));
        tip.setLocation(getStringOrEmpty(jsonObject, "location"));
        tip.setAddress(getStringOrEmpty(jsonObject, "address"));
        tip.setTypecode(getStringOrEmpty(jsonObject, "typecode"));

        return tip;
    }

    private String getStringOrEmpty(JsonObject jsonObject, String memberName) {
        JsonElement element = jsonObject.get(memberName);
        
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        }
        
        return "";
    }
}

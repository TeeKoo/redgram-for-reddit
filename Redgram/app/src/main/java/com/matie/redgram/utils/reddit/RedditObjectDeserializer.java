package com.matie.redgram.utils.reddit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.matie.redgram.models.reddit.base.RedditObject;
import com.matie.redgram.models.reddit.base.RedditObjectWrapper;

import java.lang.reflect.Type;

/**
 * Created by matie on 16/04/15.
 */
public class RedditObjectDeserializer implements JsonDeserializer<RedditObject> {
    public static final String TAG = RedditObjectDeserializer.class.getSimpleName();
    @Override
    public RedditObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonObject()){
            return null; //string returned
        }
        try{
            RedditObjectWrapper wrapper = new Gson().fromJson(json, RedditObjectWrapper.class);
            return context.deserialize(wrapper.getData(), wrapper.getKind().getDerivedClass());
        }catch (JsonParseException e){
            Log.e(TAG, "Failed to deserialized", e);
            return null;
        }
    }
}

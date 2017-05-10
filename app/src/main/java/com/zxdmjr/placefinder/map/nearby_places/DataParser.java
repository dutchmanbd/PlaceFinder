package com.zxdmjr.placefinder.map.nearby_places;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by navneet on 23/7/16.
 */
public class DataParser
{
    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try
        {
            Log.e("Places", "parse");
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e)
        {
            Log.e("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray)
    {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.e("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++)
        {
            try
            {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.e("Places", "Adding places");

            }
            catch (JSONException e)
            {
                Log.e("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = null;
        String latitude = "";
        String longitude = "";
        String reference = "";
        String formatted_address = "";

        Log.e("getPlace", "Entered");

        try
        {
            if (!googlePlaceJson.isNull("name"))
            {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity"))
            {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if (googlePlaceJson.has("geometry"))
            {
                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            }
            if (googlePlaceJson.has("reference"))
            {
                reference = googlePlaceJson.getString("reference");
            }
            if (googlePlaceJson.has("formatted_address"))
            {
                formatted_address = googlePlaceJson.getString("formatted_address");
            }

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("formatted_address", formatted_address);

            Log.e("getPlace", "Putting Places");
        }
        catch (JSONException e)
        {
            Log.e("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}

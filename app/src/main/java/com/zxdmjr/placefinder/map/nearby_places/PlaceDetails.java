package com.zxdmjr.placefinder.map.nearby_places;

/**
 * Created by Mainul on 9/27/2016.
 */

public class PlaceDetails
{
    private String name;
    private String vicinity;
    private String formatted_address;
    private double lat;
    private double lng;

    public PlaceDetails(String name, String vicinity, double lat, double lng, String formatted_address)
    {
        this.name = name;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lng = lng;
        this.formatted_address = formatted_address;
    }

    public PlaceDetails()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVicinity()
    {
        return vicinity;
    }

    public void setVicinity(String vicinity)
    {
        this.vicinity = vicinity;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLng()
    {
        return lng;
    }

    public void setLng(double lng)
    {
        this.lng = lng;
    }
}

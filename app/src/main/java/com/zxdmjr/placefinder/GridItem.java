package com.zxdmjr.placefinder;
import android.graphics.Bitmap;

/**
 * Created by user on 9/7/2016.
 */


public class GridItem
{
    private Bitmap image;
    private String title;

    public GridItem(Bitmap image, String title)
    {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

}
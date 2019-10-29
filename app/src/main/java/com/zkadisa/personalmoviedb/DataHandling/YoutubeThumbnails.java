package com.zkadisa.personalmoviedb.DataHandling;

import com.google.gson.annotations.SerializedName;

public class YoutubeThumbnails {
    @SerializedName("default")
    public YoutubeThumbnail defaultVal;
    public YoutubeThumbnail medium;
    public YoutubeThumbnail high;
}

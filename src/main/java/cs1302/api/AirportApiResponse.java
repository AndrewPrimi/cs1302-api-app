package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * This class is used to hold the information from the
 * airport api.
 */
public class AirportApiResponse {
    @SerializedName("ICAO")
    public String icao;
    @SerializedName("last_update")
    public String lastUpdate;

    public String name;
    public String url;

}

package cs1302.api;

import com.google.gson.annotations.SerializedName;


/**
 * This class is used to model the part of the json file that
 * contains the icao plane code.
 */
public class Airport {
    public int elevation;
    @SerializedName("iata_code")
    public String iataCode;
    @SerializedName("icao_code")
    public String icaoCode;
    public String name;
}

package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * This class was used to create an error info variable that is
 * used to access information about an error message.
 */
public class NotFound {
    @SerializedName("error")
    public ErrorInfo errorName;
}

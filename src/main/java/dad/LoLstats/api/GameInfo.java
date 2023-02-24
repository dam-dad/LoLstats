
package dad.LoLstats.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameInfo {

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("info")
    @Expose
    private Info info;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}

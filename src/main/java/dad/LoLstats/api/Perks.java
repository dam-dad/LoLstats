
package dad.LoLstats.api;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Perks {

    @SerializedName("statPerks")
    @Expose
    private StatPerks statPerks;
    @SerializedName("styles")
    @Expose
    private List<Style> styles;

    public StatPerks getStatPerks() {
        return statPerks;
    }

    public void setStatPerks(StatPerks statPerks) {
        this.statPerks = statPerks;
    }

    public List<Style> getStyles() {
        return styles;
    }

    public void setStyles(List<Style> styles) {
        this.styles = styles;
    }

}
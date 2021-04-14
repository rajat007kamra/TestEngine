
package com.tm4j.pojo.updatecycle;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomFields {

    @SerializedName("Build Number")
    @Expose
    private Integer buildNumber;
    @SerializedName("Release Date")
    @Expose
    private String releaseDate;
    @SerializedName("Pre-Condition(s)")
    @Expose
    private String preConditionS;
    @SerializedName("Implemented")
    @Expose
    private Boolean implemented;
    @SerializedName("Category")
    @Expose
    private List<Object> category = null;
    @SerializedName("Tester")
    @Expose
    private String tester;

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPreConditionS() {
        return preConditionS;
    }

    public void setPreConditionS(String preConditionS) {
        this.preConditionS = preConditionS;
    }

    public Boolean getImplemented() {
        return implemented;
    }

    public void setImplemented(Boolean implemented) {
        this.implemented = implemented;
    }

    public List<Object> getCategory() {
        return category;
    }

    public void setCategory(List<Object> category) {
        this.category = category;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

}

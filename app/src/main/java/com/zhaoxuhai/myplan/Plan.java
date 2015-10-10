package com.zhaoxuhai.myplan;

/**
 * Created by zhaoxuhai on 15-4-18.
 */
public class Plan {
    private int planId;
    private String planName;
    private String planUnit;
    private String iconImg;
    private String iconColor;

    public Plan(int planId, String planName, String planUnit, String iconImg, String iconColor) {
        this.planId = planId;
        this.planName = planName;
        this.planUnit = planUnit;
        this.iconImg = iconImg;
        this.iconColor = iconColor;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanUnit() {
        return planUnit;
    }

    public void setPlanUnit(String planUnit) {
        this.planUnit = planUnit;
    }

    public String getIconImg() {
        return iconImg;
    }
    public void setIconImg(String iconImg) {
        this.iconImg = iconImg;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }


}

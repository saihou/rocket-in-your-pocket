package com.propulsion.rocketjobs;

/**
 * Created by saihou on 2/13/16.
 */
public class DetailedListing {
    String employer = "";
    String id = "";
    String desc= "";
    String title = "";
    boolean onlineStatus = false;

    public DetailedListing(String employer, String id, String desc, String title, boolean status) {
        this.employer = employer;
        this.id = id;
        this.desc = desc;
        this.title = title;
        this.onlineStatus = status;
    }

    public String getEmployer() {
        return employer;
    }
    public String getId() {
        return id;
    }
    public String getDesc() {
        return desc;
    }
    public String getTitle() {
        return title;
    }
    public boolean getStatus() {
        return onlineStatus;
    }
}

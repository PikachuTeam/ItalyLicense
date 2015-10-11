package com.tatteam.patente.control;

/**
 * Created by ThanhNH on 2/3/2015.
 */
public class UserManager {
    public static final int LICENSE_TYPE_B = 1;
    public static final int LICENSE_TYPE_AM = 2;

    private static UserManager instance;
    private int licenseType;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public int getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(int licenseType) {
        this.licenseType = licenseType;
    }

    public boolean isLicenseTypeB() {
        return licenseType == LICENSE_TYPE_B;
    }

    public void destroy() {
        instance = null;
    }

}

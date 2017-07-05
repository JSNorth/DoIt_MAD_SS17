package de.fh_luebeck.jsn.doit.util;

public class AppVariables {

    private static boolean isWebAppReachable = false;

    public static boolean isWebAppReachable() {
        return isWebAppReachable;
    }

    public static void setIsWebAppReachable(boolean isWebAppReachable) {
        AppVariables.isWebAppReachable = isWebAppReachable;
    }

}

package com.worksit.app.commons;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by SKYNET-DEV01 on 25/07/2017.
 */

public class AppDomain {

    public void callApp(Activity activity, String packageName) {
        Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage(String.format("%s", packageName));
        if (launchIntent != null) {
            activity.startActivity(launchIntent);//null pointer check in case package name was not found
        }
        else {
            Uri uri = Uri.parse(String.format("market://details?id=%s", packageName));
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                activity.startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://play.google.com/store/apps/details?id=%s", packageName))));
            }
        }
    }

    public void callRateApp(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            activity.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    /**
     * Check if an activity category is launcher
     * @param act
     * @return
     */
    public boolean isLauncherActivity(Activity act) {
        final PackageManager pm = act.getPackageManager();
        // package manager is provider of all the application information
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : appList) {
            if (act.getApplicationInfo().name.equals(info.activityInfo.name))
                return true;
        }

        return false;
    }

}

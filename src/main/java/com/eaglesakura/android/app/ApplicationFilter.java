package com.eaglesakura.android.app;

import com.eaglesakura.android.util.PermissionUtil;
import com.eaglesakura.util.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApplicationFilter {

    private final PackageManager packageManager;

    private List<PackageInfo> packages = new ArrayList<PackageInfo>();

    public ApplicationFilter(Context context) {
        packageManager = context.getApplicationContext().getPackageManager();

        packages = packageManager.getInstalledPackages(0);
    }

    /**
     * 特定Permissionでフィルタする
     */
    public ApplicationFilter filterPermission(String permission) {
        return andPermission(Util.convert(new String[]{
                permission,
        }));
    }

    /**
     * 全Permissionを含んだアプリを列挙する
     */
    public ApplicationFilter andPermission(List<String> permissions) {
        Iterator<PackageInfo> iterator = packages.iterator();
        while (iterator.hasNext()) {
            boolean hasPermission = true;
            PackageInfo packageInfo = iterator.next();
            for (String permission : permissions) {
                // packageを含んでいないならば削除する
                if (!PermissionUtil.supportedPermission(packageManager, packageInfo, permission)) {
                    hasPermission = false;
                }
            }

            if (!hasPermission) {
                // 指定のPermissionを満たしていないため、削除
                iterator.remove();
            }
        }

        return this;
    }

    /**
     * フィルタしたインストール済みアプリ一覧を取得する
     */
    public List<PackageInfo> getPackageInfos() {
        return packages;
    }

    /**
     * アプリケーション情報を取り出す
     */
    public List<ApplicationInfo> getApplicationInfos(boolean loadLabel) {
        List<ApplicationInfo> result = new ArrayList<ApplicationInfo>();
        for (PackageInfo info : packages) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(info.packageName, 0);
                if (appInfo != null) {
                    if (loadLabel) {
                        appInfo.name = appInfo.loadLabel(packageManager).toString();
                    }
                    result.add(appInfo);
                }
            } catch (Exception e) {
                // error;
            }
        }

        return result;
    }
}

package com.aviraxp.adblocker.continued.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.aviraxp.adblocker.continued.BuildConfig;
import com.aviraxp.adblocker.continued.R;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;

public class SettingsActivity extends PreferenceActivity {

    static boolean isActivated = false;

    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.pref_general);
        checkState();
        donate();
    }

    private void checkState() {
        if (!isActivated) {
            showNotActive();
        }
    }

    private void showNotActive() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.hint_reboot_not_active)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openXposed();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void openXposed() {
        Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
        if (getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
            intent = getPackageManager().getLaunchIntentForPackage("de.robv.android.xposed.installer");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("section", "modules").putExtra("fragment", 1).putExtra("module", BuildConfig.APPLICATION_ID);
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    private void donate() {
        findPreference("DONATE").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (AlipayZeroSdk.hasInstalledAlipayClient(getApplicationContext())) {
                    AlipayZeroSdk.startAlipayClient(SettingsActivity.this, "aex00388woilyb9ln32hlfe");
                } else {
                    Toast.makeText(getApplicationContext(), R.string.donate_failed, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}
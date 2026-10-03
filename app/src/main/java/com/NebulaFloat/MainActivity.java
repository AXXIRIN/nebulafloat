package com.NebulaFloat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {
package com.NebulaFloat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        handle(getIntent());
    }

    @Override
    protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
        handle(i);
    }

    private void handle(Intent in) {
        if (in.getBooleanExtra("stop", false)) {
            stopService(new Intent(this, NebulaService.class));
            finish();
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Izinkan 'tampil di atas aplikasi lain', lalu buka app lagi", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
            finish();
            return;
        }
        if (!Environment.isExternalStorageManager()) {
            Toast.makeText(this, "Izinkan 'akses semua file', lalu buka app lagi", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
            finish();
            return;
        }
        Intent s = new Intent(this, NebulaService.class);
        s.putExtras(in);
        startForegroundService(s);
        finish();
    }
}
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        handle(getIntent());
    }

    @Override
    protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
        handle(i);
    }

    private void handle(Intent in) {
        if (in.getBooleanExtra("stop", false)) {
            stopService(new Intent(this, NebulaService.class));
            finish();
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Izinkan 'tampil di atas aplikasi lain', lalu buka app lagi", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
            finish();
            return;
        }
        if (!Environment.isExternalStorageManager()) {
            Toast.makeText(this, "Izinkan 'akses semua file', lalu buka app lagi", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
            finish();
            return;
        }
        Intent s = new Intent(this, NebulaService.class);
        s.putExtras(in);
        startForegroundService(s);
        finish();
    }
}
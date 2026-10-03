package com.NebulaFloat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NebulaService extends Service {

    private WindowManager wm;
    private View root;

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public int onStartCommand(Intent in, int flags, int startId) {
        NotificationChannel ch = new NotificationChannel(
                "float", "Floating widget", NotificationManager.IMPORTANCE_MIN);
        getSystemService(NotificationManager.class).createNotificationChannel(ch);
        Notification n = new Notification.Builder(this, "float")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setContentTitle("Floating widget aktif")
                .build();
        startForeground(1, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);

        String file = "https://raw.githubusercontent.com/AXXIRIN/nebulafloat/main/widget.html";
        int w = 320, h = 240;
        boolean input = false;
        if (in != null) {
            if (in.hasExtra("file")) file = in.getStringExtra("file");
            w = in.getIntExtra("w", w);
            h = in.getIntExtra("h", h);
            input = in.getBooleanExtra("input", false);
        }
        show(file, w, h, input);
        return START_NOT_STICKY;
    }

    private void show(String file, int wDp, int hDp, boolean input) {
        hide();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        float d = getResources().getDisplayMetrics().density;

        final LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);

        FrameLayout bar = new FrameLayout(this);
        bar.setBackgroundColor(0x88000000);
        TextView close = new TextView(this);
        close.setText("\u2715");
        close.setTextColor(Color.WHITE);
        close.setGravity(Gravity.CENTER);
        close.setPadding((int) (14 * d), 0, (int) (14 * d), 0);
        bar.addView(close, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.END));
        box.addView(bar, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (28 * d)));

        WebView web = new WebView(this);
        web.setBackgroundColor(Color.TRANSPARENT);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        web.setWebViewClient(new WebViewClient());
				web.loadUrl(file);
        box.addView(web, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int fl = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        if (!input) fl |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        final WindowManager.LayoutParams p = new WindowManager.LayoutParams(
                (int) (wDp * d), (int) ((hDp + 28) * d),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                fl, PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.TOP | Gravity.START;
        p.x = (int) (20 * d);
        p.y = (int) (120 * d);

        bar.setOnTouchListener(new View.OnTouchListener() {
            int sx, sy;
            float tx, ty;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sx = p.x; sy = p.y;
                        tx = e.getRawX(); ty = e.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        p.x = sx + (int) (e.getRawX() - tx);
                        p.y = sy + (int) (e.getRawY() - ty);
                        wm.updateViewLayout(box, p);
                        return true;
                }
                return false;
            }
        });
        close.setOnClickListener(v -> stopSelf());

        wm.addView(box, p);
        root = box;
    }

    private void hide() {
        if (root != null && wm != null) {
            try { wm.removeView(root); } catch (Exception ignored) {}
        }
        root = null;
    }

    @Override
    public void onDestroy() {
        hide();
        super.onDestroy();
    }
}

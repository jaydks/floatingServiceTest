package com.example.testcheckonresumebackground;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.testcheckonresumebackground.Activity.MainActivity;


public class MUpdateService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private WindowManager windowManager;
    private View floatingView;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkOverlayPermission();


        Log.d("MUpdateService", "MUpdateService");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        windowManager.addView(floatingView, params);

        startForeground(NOTIFICATION_ID, buildNotification());



        TextView tvClock = (TextView) floatingView.findViewById(R.id.tv_clock);
        //tvClock.setOnClickListener();




        // Service 로직을 실행합니다.
        return START_STICKY;
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private Notification buildNotification() {
        // Foreground Service에 대한 Notification을 생성합니다.
        // Notification.Builder 또는 NotificationCompat.Builder를 사용할 수 있습니다.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "my_channel_id";
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 알림을 클릭했을 때 실행할 Intent 생성
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE);

        // 알림 빌더 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setContentTitle("Foreground Service")
                .setContentText("Foreground Service is running")
                .setContentIntent(pendingIntent)
                .setOngoing(true); // 사용자가 알림을 지울 수 없도록 설정 (계속 실행 중인 상태 표시)

        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }
}

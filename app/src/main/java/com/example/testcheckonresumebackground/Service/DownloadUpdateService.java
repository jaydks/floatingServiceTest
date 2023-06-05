package com.example.testcheckonresumebackground.Service;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.testcheckonresumebackground.Activity.MainActivity;
import com.example.testcheckonresumebackground.Activity.ProgressReceiver;
import com.example.testcheckonresumebackground.Constants;
import com.example.testcheckonresumebackground.R;
import com.example.testcheckonresumebackground.ReservedUpdateReceiver;
import com.example.testcheckonresumebackground.databinding.FloatingLayoutBinding;


public class DownloadUpdateService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private WindowManager windowManager;
    private View floatingView;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkOverlayPermission();


        Log.d("MUpdateService", "MUpdateService");


        String percent = intent.getStringExtra(Constants.ACTION_SEND_PERCENT);
        sendPercentBroadCast(percent);

//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//        int LAYOUT_FLAG;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
//            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
//        }
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                LAYOUT_FLAG,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT
//        );
//
//
//        if (floatingView == null) {
//            floatingView = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);
//            floatingView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            initialX = params.x;
//                            initialY = params.y;
//                            initialTouchX = event.getRawX();
//                            initialTouchY = event.getRawY();
//                            return true;
//                        case MotionEvent.ACTION_MOVE:
//                            int offsetX = (int) (event.getRawX() - initialTouchX);
//                            int offsetY = (int) (event.getRawY() - initialTouchY);
//                            params.x = initialX + offsetX;
//                            params.y = initialY + offsetY;
//                            windowManager.updateViewLayout(floatingView, params);
//                            return true;
//                    }
//                    return false;
//                }
//            });
//
//
//
//            windowManager.addView(floatingView, params);
//
//            floatingView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener(){
//
//                @Override
//                public void onClick(View v) {
//                    stopSelf();
//                }
//            });
//
//
//
//
//
//
//
//
//        } else {
//            Log.d("floatingView", "already exist");
//        }



        // 플로팅 뷰 터치 이벤트 처리


        startForeground(NOTIFICATION_ID, buildNotification());



        // TextView tvClock = (TextView) floatingView.findViewById(R.id.tv_clock);




        // Service 로직을 실행합니다.
        return START_STICKY;
    }

    private void sendPercentBroadCast(String percent){
        Intent broadcastIntent = new Intent(this, ProgressReceiver.class);

        broadcastIntent.putExtra("progressPercent", percent);

        broadcastIntent.setAction(Constants.ACTION_UPDATE_PROGRESS);

        this.sendBroadcast(broadcastIntent);

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

package com.example.ecommerceapp.Workmanager



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.ecommerceapp.R
import com.example.ecommerceapp.ui.cart.CartFragment
import com.example.ecommerceapp.ui.splash.SplashFragment
import com.google.android.material.snackbar.Snackbar


class SendNotificationWorker(val context: Context, workerParams: WorkerParameters) :Worker(context,
    workerParams
) {
    override fun doWork(): Result {
        val sharedPreferences=context.getSharedPreferences("com.example.ecommerceapp",Context.MODE_PRIVATE)
        val savedProduct=sharedPreferences.getBoolean("isProductInBag",false)
        if(savedProduct){
            createNotification()
        }else{

        }
        return Result.success()
    }

    private fun createNotification() {
        val builder: NotificationCompat.Builder
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, CartFragment::class.java)

        val contentToGo = PendingIntent.getActivity(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "channelId"
        val channelName = "channelName"
        val channelIntroduction = "channelIntroduction"
        val channelPriority = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= 26) {
            var channel: NotificationChannel? = notificationManager
                .getNotificationChannel(channelId)

            if (channel == null) {
                channel = NotificationChannel(channelId, channelName, channelPriority)
                channel.description = channelIntroduction
                notificationManager.createNotificationChannel(channel)
            }

            builder = NotificationCompat.Builder(applicationContext, channelId)
            builder
                .setContentTitle("Did you forget an item in your basket?")
                .setContentText("Complete your order now!")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(contentToGo)

            notificationManager.notify(1, builder.build())
        }
    }

}
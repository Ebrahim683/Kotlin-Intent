package com.example.intentkotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    private val notificationID = 101
    private val channelId = "com.example.intentkotlin"
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel

    //    private lateinit var builder : Notification.Builder
    val descriptions = "Test notification"
    private val name = "Title"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotification()

        button.setOnClickListener {
            go()
        }
        button.tooltipText = "Click for action"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun go() {
        when {
            web.isChecked -> {
                webIntent()
            }
            email.isChecked -> {
                sendMail()
            }
            notify.isChecked -> {
                sendNotification()
            }
            else -> {
                Toast.makeText(this,"None",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Sent Mail
    private fun sendMail() {
        val intent: Intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("ebrahimsonchay3@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Kotlin Intent")
            putExtra(Intent.EXTRA_TEXT, "This is kotlin intent")
            Intent.createChooser(intent, "Send via")
        }
        startActivity(intent)
    }

    //Open Browser
    private fun webIntent() {
        val webIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
        try {
            startActivity(webIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH).apply {
                    description = description
                }
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification() {

        var intent:Intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        var pendingIntent = PendingIntent.getActivity(this,0,intent,0)

        var bitmap = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.kotlin)
        var bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.kotlin)

        val notificationCompat = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.kotlin)
            .setContentTitle("Hi")
            .setContentText("This is notification from kotlin")
            .setLargeIcon(bitmapLargeIcon)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this@MainActivity)) {
            notify(notificationID, notificationCompat.build())
        }
    }
}
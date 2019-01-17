package com.example.jobschedulertest

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.ResultReceiver
import android.util.Log
import java.util.*

class MyJobService: JobService() {
    companion object {
        private const val TAG = "MyJobService"
    }

    private var resultReceiver: ResultReceiver? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        resultReceiver = intent.getParcelableExtra("resultReceiver") as ResultReceiver
        return Service.START_NOT_STICKY
    }

    override fun onStartJob(job: JobParameters): Boolean {
        val time = "[Job ${job.jobId}] ${Date()}: Job started"
        Log.d(TAG, time)
        saveTime(time)

        if (resultReceiver != null) {
            resultReceiver?.send(MyResultReceiver.RESULT_CODE_SUCCESS, null)
        }

        jobFinished(job, true)
        return true
    }

    override fun onStopJob(job: JobParameters): Boolean {
        val time = "[Job ${job.jobId}] ${Date()}: Job stopped"
        saveTime(time)
        Log.d(TAG, time)
        return true
    }

    private fun saveTime(time: String) {
        val prefs = getSharedPreferences("JobSchedulerTest", Context.MODE_PRIVATE)

        val timesString = prefs.getString("times", "")
        val serialId = prefs.getInt("serialId", 0) + 1

        val editor = prefs.edit()
        editor.putString("times", "$timesString,[Serial $serialId] $time")
        editor.putInt("serialId", serialId)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "${Date()}: onDestroy")
    }
}
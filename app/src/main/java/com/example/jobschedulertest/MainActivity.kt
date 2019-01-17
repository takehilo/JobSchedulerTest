package com.example.jobschedulertest

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyResultReceiver.Callback {
    private lateinit var listAdapter: ListAdapter
    private var times = mutableListOf("Job has not been started")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startJob()
        setupRecyclerView()
        scheduleJob()
    }

    private fun startJob() {
        val intent = Intent(this, MyJobService::class.java)
        val resultReceiver = MyResultReceiver(Handler(), this)
        intent.putExtra("resultReceiver", resultReceiver)
        startService(intent)
    }

    private fun setupRecyclerView() {
        listAdapter = ListAdapter(times)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter
        }
    }

    private fun scheduleJob() {
        getJobId()
        val builder = JobInfo.Builder(1, ComponentName(this, MyJobService::class.java))
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        (getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(builder.build())
    }

    override fun onEvent() {
        val newTimes = getTimes()
        times.clear()
        times.addAll(newTimes)
        listAdapter.notifyDataSetChanged()
    }

    private fun getTimes(): List<String> {
        val prefs = getSharedPreferences("JobSchedulerTest", Context.MODE_PRIVATE)
        val times = prefs.getString("times", "")
        return times.split(",")
    }

    private fun getJobId(): Int {
        val prefs = getSharedPreferences("JobSchedulerTest", Context.MODE_PRIVATE)
        val newJobId = prefs.getInt("jobId", 0) + 1

        val editor = prefs.edit()
        editor.putInt("jobId", newJobId)
        editor.apply()

        return newJobId
    }
}

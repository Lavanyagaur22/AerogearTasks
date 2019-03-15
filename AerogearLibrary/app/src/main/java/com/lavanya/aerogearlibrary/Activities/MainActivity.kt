package com.lavanya.aerogearlibrary.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lavanya.aerogearlibrary.Adapter.TaskAdapter
import com.lavanya.aerogearlibrary.Model.Task
import com.lavanya.aerogearlibrary.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val noteslist = arrayListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskAdapter = TaskAdapter(noteslist)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = taskAdapter


        button.setOnClickListener {
            noteslist.add(Task(etTitle.text.toString(), etDesc.text.toString()))
            taskAdapter.notifyItemInserted(noteslist.size - 1)
        }


    }
}

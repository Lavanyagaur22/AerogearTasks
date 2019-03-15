package com.lavanya.aerogearlibrary.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.lavanya.aerogearlibrary.Adapter.TaskAdapter
import com.lavanya.aerogearlibrary.Model.Task
import com.lavanya.aerogearlibrary.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alertdialog_task.view.*

class MainActivity : AppCompatActivity() {

    val noteslist = arrayListOf<Task>()
    var title = ""
    var desc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskAdapter = TaskAdapter(noteslist)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = taskAdapter

        fabAdd.setOnClickListener {

            val inflatedView = LayoutInflater.from(this).inflate(R.layout.alertdialog_task, null, false)

            val customAlert: AlertDialog = AlertDialog.Builder(this)
                .setView(inflatedView)
                .setTitle("Create a new Note")
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, which ->
                    title = inflatedView.etTitle.text.toString()
                    desc = inflatedView.etDesc.text.toString()
                    noteslist.add(Task(title, desc))
                    taskAdapter.notifyItemInserted(noteslist.size - 1)
                    dialog.dismiss()
                }
                .create()
            customAlert.show()

        }


    }
}

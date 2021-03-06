package com.lavanya.aerogearlibrary.adapter

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lavanya.aerogearlibrary.R
import com.lavanya.aerogearlibrary.activities.MainActivity
import com.lavanya.aerogearlibrary.model.Task
import kotlinx.android.synthetic.main.alertdialog_task.view.*
import kotlinx.android.synthetic.main.item_row_tasks.view.*

class TaskAdapter(private val notes: List<Task>, private val context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): TaskAdapter.TaskHolder {

        return TaskHolder(
            LayoutInflater.from(container.context).inflate(
                com.lavanya.aerogearlibrary.R.layout.item_row_tasks,
                container,
                false
            )
        )
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: TaskAdapter.TaskHolder, position: Int) {

        val currentTask = notes[position]
        with(holder.itemView) {
            task_tv.text = currentTask.task
            desc_tv.text = currentTask.desc
            id_tv.text = currentTask.id.toString()
            version_tv.text = currentTask.version.toString()
        }

        holder.itemView.setOnClickListener {
            val inflatedView = LayoutInflater.from(context).inflate(R.layout.alertdialog_task, null, false)
            val customAlert: AlertDialog = AlertDialog.Builder(context)
                .setView(inflatedView)
                .setTitle("Update the clicked Note")
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, which ->

                    val id = inflatedView.etId.toString()
                    val titleEt = inflatedView.etTitle.text.toString()
                    val versionEt = inflatedView.etVersion.text.toString()

                    Log.e("Adapter", "${inflatedView.etId.text}")

                    if (context is MainActivity) this.context.updateTask(
                        currentTask.id.toString(),
                        titleEt,
                        versionEt.toInt()
                    )
                    dialog.dismiss()
                }
                .create()
            customAlert.show()
        }
    }
}

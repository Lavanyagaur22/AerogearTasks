package com.lavanya.aerogearlibrary.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lavanya.aerogearlibrary.Model.Task
import kotlinx.android.synthetic.main.item_row_tasks.view.*

class TaskAdapter(private val notes: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {


    inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): TaskAdapter.TaskHolder {

        return TaskHolder(LayoutInflater.from(container.context).inflate(com.lavanya.aerogearlibrary.R.layout.item_row_tasks, container, false))

    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: TaskAdapter.TaskHolder, position: Int) {

        val currentTask = notes[position]
        with(holder.itemView) {
            task_tv.text = currentTask.task
            desc_tv.text = currentTask.desc

        }


    }
}
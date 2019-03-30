package com.lavanya.aerogearlibrary.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.lavanya.aerogearlibrary.*
import com.lavanya.aerogearlibrary.adapter.TaskAdapter
import com.lavanya.aerogearlibrary.model.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val noteslist = arrayListOf<Task>()
    val TAG = javaClass.simpleName
    val taskAdapter by lazy {
        TaskAdapter(noteslist, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getTasks()

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = taskAdapter


        /**  Hidden the fab action to insert a new note and
         *   just to display the tasks received from the query
         */

//        fabAdd.setOnClickListener {
//
//            val inflatedView = LayoutInflater.from(this).inflate(R.layout.alertdialog_task, null, false)
//
//            val customAlert: AlertDialog = AlertDialog.Builder(this)
//                .setView(inflatedView)
//                .setTitle("Create a new Note")
//                .setNegativeButton("No") { dialog, which ->
//                    dialog.dismiss()
//                }
//                .setPositiveButton("Yes") { dialog, which ->
//                    title = inflatedView.etTitle.text.toString()
//                    desc = inflatedView.etDesc.text.toString()
//                    noteslist.add(Task(title, desc))
//                    Log.e(TAG, "jhjj")
//                    taskAdapter.notifyItemInserted(noteslist.size - 1)
//                    dialog.dismiss()
//                }
//                .create()
//            customAlert.show()
//
//        }

    }

    fun getTasks() {

        Log.e(TAG, "inside getTasks")

        val client = Utils.getApolloClient(this)?.query(
            AllTasksQuery.builder().build()
        )

        client?.enqueue(object : ApolloCall.Callback<AllTasksQuery.Data>() {

            override fun onFailure(e: ApolloException) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
            }

            override fun onResponse(response: Response<AllTasksQuery.Data>) {

                val result = response.data()?.allTasks()
                Log.e(TAG, "onResponse: ${result?.get(1)?.title()} ${result?.get(1)?.version()}")

                result?.forEach {
                    val title = it.title()
                    val desc = it.description()
                    val id = it.id()
                    val version: Int? = it.version()
                    val task = Task(title, desc, id.toInt(), version!!)
                    noteslist.add(task)
                }
                runOnUiThread {
                    taskAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun updateTask(id: String, title: String, version: Int) {

        Log.e(TAG, "inside update task")

        val client = Utils.getApolloClient(this)?.mutate(
            UpdateCurrentTask.builder().id(id).title(title).version(version).build()
        )
        client?.enqueue(object : ApolloCall.Callback<UpdateCurrentTask.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.e("onFailure" + "updateTask", e.toString())
            }

            override fun onResponse(response: Response<UpdateCurrentTask.Data>) {
                val result = response.data()?.updateTask()
                Log.e(TAG, "${result?.id()}")
                Log.e(TAG, "${result?.title()}")
                Log.e(TAG, "${result?.description()}")
                Log.e(TAG, "${result?.version()}")
            }
        })
        noteslist.clear()
        getTasks()
    }

    private fun createtask(title: String, description: String) {

        Log.e(TAG, "inside create task")

        val client = Utils.getApolloClient(this)?.mutate(
            CreateTask.builder().title(title).description(description).build()
        )

        client?.enqueue(object : ApolloCall.Callback<CreateTask.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.e("onFailure" + "createTask", e.toString())
            }

            override fun onResponse(response: Response<CreateTask.Data>) {
                val result = response.data()?.createTask()
                Log.e(TAG, "${result?.id()}")
                Log.e(TAG, "${result?.title()}")
                Log.e(TAG, "${result?.description()}")
                Log.e(TAG, "${result?.version()}")
            }
        })
    }
}
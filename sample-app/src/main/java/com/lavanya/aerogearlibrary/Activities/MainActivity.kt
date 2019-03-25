package com.lavanya.aerogearlibrary.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.lavanya.aerogearlibrary.Adapter.TaskAdapter
import com.lavanya.aerogearlibrary.AllTasksQuery
import com.lavanya.aerogearlibrary.Model.Task
import com.lavanya.aerogearlibrary.MyApolloClient
import com.lavanya.aerogearlibrary.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val noteslist = arrayListOf<Task>()
    val TAG = javaClass.simpleName
    val taskAdapter by lazy {
        TaskAdapter(noteslist)
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

    private fun getTasks() {

//        val client=MyApolloClient.getMyApolloClient()?.query(
//            AllTasksQuery.builder().build()
//        )

        val client = MyApolloClient.getMyApolloClient()?.query(
            AllTasksQuery.builder().build()
        )

        client?.enqueue(object : ApolloCall.Callback<AllTasksQuery.Data>() {

            override fun onFailure(e: ApolloException) {

                Log.e(TAG, e.toString())

            }

            override fun onResponse(response: Response<AllTasksQuery.Data>) {

                val result = response.data()?.allTasks()
                Log.e(TAG, "onResponse: ${result?.get(2)?.title()}")

                result?.forEach {
                    val title = it.title()
                    val desc = it.description()
                    val id = it.id()
                    val task = Task(title, desc, id.toInt())
                    noteslist.add(task)
                }
                runOnUiThread {
                    taskAdapter.notifyDataSetChanged()
                }

            }
        })
    }
}

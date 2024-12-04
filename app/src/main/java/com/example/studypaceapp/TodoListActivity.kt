package com.example.studypaceapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class TodoListActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private val tasks = TaskManager.getTasks().toMutableList()
    private val taskStatus = TaskManager.getTaskStatus().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        TaskManager.initialize(this)

        val listView = findViewById<ListView>(R.id.todoListView)
        val taskInput = findViewById<EditText>(R.id.taskInput)
        val addTaskButton = findViewById<Button>(R.id.addTaskButton)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, tasks)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        for (i in taskStatus.indices) {
            listView.setItemChecked(i, taskStatus[i])
        }

        addTaskButton.setOnClickListener {
            val task = taskInput.text.toString().trim()
            if (task.isNotEmpty()) {
                if (tasks.size < 8) { // Increased task limit to 8
                    tasks.add(task)
                    taskStatus.add(false)
                    TaskManager.addTask(task)
                    adapter.notifyDataSetChanged()
                    taskInput.text.clear()
                } else {
                    Toast.makeText(this, "Maximum task limit reached (8 tasks).", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a valid task.", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            taskStatus[position] = listView.isItemChecked(position)
            TaskManager.updateTaskStatus(taskStatus)

            if (taskStatus[position]) {
                if (TaskManager.areAllTasksCompleted()) {
                    startActivity(Intent(this, RewardActivity::class.java))
                } else {
                    startActivity(Intent(this, BalloonActivity::class.java))
                }
                finish()
            }
        }
    }
}




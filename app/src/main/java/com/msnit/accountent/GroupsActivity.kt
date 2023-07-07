package com.msnit.accountent

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.msnit.accountent.groups.ClickListener
import com.msnit.accountent.groups.GroupEntity
import com.msnit.accountent.groups.GroupsListAdapter
import java.sql.Date

class GroupsActivity : AppCompatActivity() {
    private lateinit var adapter: GroupsListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var listener: ClickListener

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.background = null


        val list = getData()

        recyclerView = findViewById(R.id.groupsList)
        listener = object : ClickListener() {
            override fun click(index: Int) {
                Toast.makeText(
                    this@GroupsActivity,
                    "Clicked item index is $index",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        adapter = GroupsListAdapter(list, listener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun getData(): List<GroupEntity> {
        val list = ArrayList<GroupEntity>()

        list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
        list.add(GroupEntity("Second Group", Date.valueOf("2023-02-15"), 1))
        list.add(GroupEntity("My Test Group", Date.valueOf("2023-03-30"), 1))
      list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
      list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
      list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
      list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
        list.add(GroupEntity("Second Group", Date.valueOf("2023-02-15"), 1))
        list.add(GroupEntity("My Test Group", Date.valueOf("2023-03-30"), 1))
      list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
        list.add(GroupEntity("Second Group", Date.valueOf("2023-02-15"), 1))
        list.add(GroupEntity("My Test Group", Date.valueOf("2023-03-30"), 1))
      list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
        list.add(GroupEntity("Second Group", Date.valueOf("2023-02-15"), 1))
        list.add(GroupEntity("My Test Group", Date.valueOf("2023-03-30"), 1))
        list.add(GroupEntity("First Group", Date.valueOf("2023-01-01"), 11))
        list.add(GroupEntity("Second Group", Date.valueOf("2023-02-15"), 1))
        list.add(GroupEntity("My Test Group", Date.valueOf("2023-03-30"), 1))

        return list
    }
}

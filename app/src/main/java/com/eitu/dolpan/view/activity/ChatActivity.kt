package com.eitu.dolpan.view.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.eitu.dolpan.adapter.recycler.AdapterChatByDate
import com.eitu.dolpan.dataClass.FireStoreItem
import com.eitu.dolpan.databinding.ActivityChatBinding
import com.eitu.dolpan.view.base.BaseActivity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class ChatActivity: BaseActivity() {

    private lateinit var binding: ActivityChatBinding

    private val adapter = AdapterChatByDate()

    private val owner:String
        get() = intent.getStringExtra("owner")!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun init() {
        initRecycler()
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        fdb.collection("item")
            .whereEqualTo("owner", owner)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(5)
            .addSnapshotListener { value, error ->
                if (value != null && !value.isEmpty && adapter.getList().isNotEmpty()) {
                    for (change in value.documentChanges) {
                        if (change.type == DocumentChange.Type.ADDED) {
                            adapter.addItem(change.document.toObject(FireStoreItem::class.java))
                        }
                    }
                }
            }
        FireStoreItem.setList(owner, adapter)
    }

}
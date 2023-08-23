package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.view.base.BaseActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageItemActivity : BaseActivity() {

    @Inject lateinit var fdb : FirebaseFirestore

    private val _items = mutableStateListOf<DocumentSnapshot>()

    override fun setBinding(): View? {
        return null
    }

    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
        fdb.collection("item")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                _items.addAll(it.documents)
            }
    }

    @Composable
    private fun App() {

        Column(modifier = Modifier.fillMaxSize()) {
            TextButton(onClick = {
                _items.filter { it.getString("type") == "naverCafe" }.forEach {doc ->
                    fdb.collection("item").document(doc.id).delete().addOnSuccessListener { _items.remove(doc) }
                }
            }) {
                Text(text = "네이버 카페 글 초기화")
            }
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                items(_items) {
                    ListItem(item = it)
                }
            }
        }
    }
    
    @Composable
    private fun ListItem(item : DocumentSnapshot) {
        
        val chat = item.toObject(Chat::class.java)
        var alert by remember { mutableStateOf(false) }
        
        Column(modifier = Modifier.clickable { 
            alert = true
        }) {
            chat?.owner?.let { Text(text = it) }
            chat?.type?.let { Text(text = it) }
            chat?.title?.let { Text(text = it) }
            chat?.getFormattedDate()?.let { Text(text = it) }
            Divider()
        }

        if (alert) {
            AlertDialog(
                onDismissRequest = { alert = false },
                confirmButton = {
                    TextButton(onClick = {
                        alert = false
                        fdb.collection("item").document(item.id).delete().addOnSuccessListener { _items.remove(item) }
                    }) {
                        Text(text = "삭제")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { alert = false }) {
                        Text(text = "닫기")
                    }
                }
            )
        }
        
    }

}
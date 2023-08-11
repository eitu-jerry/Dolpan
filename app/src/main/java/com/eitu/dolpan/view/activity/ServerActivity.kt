package com.eitu.dolpan.view.activity

import android.os.Bundle
import android.view.View
import com.eitu.dolpan.databinding.ActivityServerBinding
import com.eitu.dolpan.view.base.BaseActivity
import java.util.*

class ServerActivity: BaseActivity() {

    private lateinit var binding: ActivityServerBinding

    private val wakCafe = "Iep9BEdfIxd759MU7JgtSg"
    private val ineCafe = "Lp-S_8ZQuLCK03pDpod-7Q"
    private val jingCafe = "8g_F8kj48MSqBeVnVAhnCw"
    private val lilpaCafe = "ANjFuUREskKRC7DcGwAXNA"
    private val jururuCafe = "ri0vjEn1-XpglkwfwSDuBw"
    private val goseguCafe = "kvYmWvSHP9_wnnbRX4nGXg"
    private val vichanCafe = "6Wj7By3k4NnbeXohIaIltQ"
    private val memberArray = arrayOf(wakCafe, ineCafe, jingCafe, lilpaCafe, jururuCafe, goseguCafe, vichanCafe)
    private var memberIndex = 0
    private var currentMember: String = ""
    private val memberMap = hashMapOf(
        Pair(wakCafe, "wak"),
        Pair(ineCafe, "ine"),
        Pair(jingCafe, "jing"),
        Pair(lilpaCafe, "lilpa"),
        Pair(jururuCafe, "jururu"),
        Pair(goseguCafe, "gosegu"),
        Pair(vichanCafe, "vichan"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setBinding(): View {
        binding = ActivityServerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {

    }

}
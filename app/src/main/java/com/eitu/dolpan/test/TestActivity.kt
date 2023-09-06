package com.eitu.dolpan.test

import android.view.View
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eitu.dolpan.R
import com.eitu.dolpan.databinding.ActivityTestBinding
import com.eitu.dolpan.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity() {

    private val binding : ActivityTestBinding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }

    private val model : TestViewModel by viewModels()

    private val ownerMap = hashMapOf(
        Pair("wak", "우왁굳"),
        Pair("ine", "아이네"),
        Pair("jing", "징버거"),
        Pair("lilpa", "릴파"),
        Pair("jururu", "주르르"),
        Pair("gosegu", "고세구"),
        Pair("vichan", "비챤"),
    )

    override fun setBinding(): View? {
        return binding.root
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun init() {
        binding.lifecycleOwner = this
        binding.model = model

        (binding.root as CoordinatorLayout).addView(
            ComposeView(this@TestActivity).apply {
                setContent {
                    GlideImage(model = R.drawable.img, contentDescription = "asdf", contentScale = ContentScale.Crop, modifier = Modifier.blur(10.dp))
                }
            }
        )

        binding.scrollView.bringToFront()
        binding.appBar.bringToFront()

        binding.frame.addView(
            ComposeView(this@TestActivity).apply {
                setContent {
                    App()
                }
            }
        )
    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun App() {

        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
        ) {
            model.list.forEach {
                Box() {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .defaultMinSize(minHeight = 70.dp)
                            .background(
                                color = colorResource(id = R.color.stackBackground),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(15.dp)
                    ) {
                        GlideImage(
                            model = R.drawable.button_write,
                            contentDescription = "asdf",
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .background(
                                    color = colorResource(id = R.color.stackBackground),
                                    shape = CircleShape
                                )
                                .align(Alignment.CenterVertically)
                        )
                        Column() {
                            Text(
                                text = ownerMap[it.owner] ?: "모시깽"
                            )
                            Text(
                                text = it.title
                            )
                        }
                    }
                }
            }
//            for (it in 0..100) {
//                Text(
//                    text = "$it 번째",
//                    modifier = Modifier
//                        .padding(15.dp)
//                        .fillMaxWidth()
//                        .height(80.dp)
//                        .background(
//                            color = colorResource(id = R.color.stackBackground),
//                            shape = RoundedCornerShape(10.dp)
//                        )
//                )
//            }
        }
    }

}
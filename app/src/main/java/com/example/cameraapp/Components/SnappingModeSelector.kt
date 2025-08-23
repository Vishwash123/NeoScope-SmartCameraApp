package com.example.cameraapp.Components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ui.theme.Montserrat


@RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR2)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PerfectlyCenteredCarousel(items: List<String>,cameraViewModel: CameraViewModel,onModeSelected:(String)->Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 3

    val pagerState = rememberPagerState(
        initialPage = cameraViewModel.selectedPage,
        pageCount = { items.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        cameraViewModel.updatePage(pagerState.currentPage)
        onModeSelected(items[pagerState.currentPage])
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = 0.dp,
        beyondViewportPageCount = 2,
        contentPadding = PaddingValues(horizontal = itemWidth),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        pageSize = PageSize.Fixed(itemWidth)
    ) { page ->
        val isSelected = pagerState.currentPage == page

        Box(
            modifier = Modifier
                .width(itemWidth)
                .fillMaxHeight()
                .graphicsLayer {
                    alpha = if (isSelected) 1f else 0.8f
                    scaleX = if (isSelected) 1f else 0.85f
                    scaleY = if (isSelected) 1f else 0.85f
                },
            contentAlignment = Alignment.Center
        ) {
            CustomizableGradientText(
                text = items[page],
                gradientStartColor = if (isSelected) Color(0xFFDBEBF2) else Color(0xFFF2FBFF),
                gradientEndColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFB3D4ED),
                strokeColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFFCFCFC),
                dropShadowColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFF00B7FF),
                textAlign = TextAlign.Center,
                fontSize = if (isSelected) 16.sp else 14.sp,
                fontFamily = Montserrat,
                fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

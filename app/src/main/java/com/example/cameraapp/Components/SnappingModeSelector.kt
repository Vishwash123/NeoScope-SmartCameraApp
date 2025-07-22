package com.example.cameraapp.Components

import CustomizableGradientText
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cameraapp.ui.theme.Montserrat

//@Composable
//fun SnappingModeSelector(
//    items:List<String>,
//    modifier: Modifier=Modifier
//){
//    val listState = rememberLazyListState()
//    val scope = rememberCoroutineScope()
//
//    val centerIndex = remember {
//        derivedStateOf {
//            val visibleItems = listState.layoutInfo.visibleItemsInfo
//            val center = listState.layoutInfo.viewportEndOffset/2
//
//            visibleItems.minByOrNull {
//                kotlin.math.abs((it.offset + it.size/2)-center)
//            }?.index ?:0
//        }
//    }
//
//    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
//    LazyRow(
//        state = listState,
//        flingBehavior = flingBehavior,
//        contentPadding = PaddingValues(horizontal = 100.dp),
//        horizontalArrangement = Arrangement.spacedBy(24.dp),
//        modifier = modifier.fillMaxWidth().height(60.dp)
//    ) {
//        itemsIndexed(items){index,item->
//            val isSelected = index == centerIndex.value
//
//            CustomizableGradientText(
//                text = item,
//                gradientStartColor = if(isSelected) Color(0xFFDBEBF2) else Color(0xFFF2FBFF) ,
//                gradientEndColor = if(isSelected) Color(0xFFFFFFFF) else Color(0xFFB3D4ED),
//                strokeColor = if(isSelected) Color(0xFFFFFFFF) else Color(0xFFFCFCFC),
//                dropShadowColor = if(isSelected) Color(0xFFFFFFFF) else Color(0xFF00B7FF),
//                textAlign = TextAlign.Center,
//                fontSize = if(isSelected) 18.sp else 14.sp,
//                fontFamily = Montserrat,
//                fontWeight = FontWeight.Medium
//            )
//        }
//    }
//}
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun SnappingModeSelector(
//    items: List<String>,
//    modifier: Modifier = Modifier
//) {
//    val listState = rememberLazyListState()
//    val scope = rememberCoroutineScope()
//
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//    val itemWidth = screenWidth / 3 // 3 items on screen at a time
//
//    val centerIndex = remember {
//        derivedStateOf {
//            val layoutInfo = listState.layoutInfo
//            val viewportCenter = layoutInfo.viewportEndOffset / 2
//
//            layoutInfo.visibleItemsInfo.minByOrNull { item->
//                val itemCenter = item.offset + item.size / 2
//                kotlin.math.abs(itemCenter - viewportCenter)
//            }?.index ?: 0
//        }
//    }
//
//    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
//
//    LazyRow(
//        state = listState,
//        flingBehavior = flingBehavior,
//        contentPadding = PaddingValues(horizontal = itemWidth / 2),
//        horizontalArrangement = Arrangement.spacedBy(0.dp),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(60.dp)
//    ) {
//        itemsIndexed(items) { index, item ->
//            val isSelected = index == centerIndex.value
//
//            Box(
//                modifier = Modifier.width(itemWidth)
//                    .fillMaxSize()
//            ) {
//                CustomizableGradientText(
//                    text = item,
//                    gradientStartColor = if (isSelected) Color(0xFFDBEBF2) else Color(0xFFF2FBFF),
//                    gradientEndColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFB3D4ED),
//                    strokeColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFFCFCFC),
//                    dropShadowColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFF00B7FF),
//                    textAlign = TextAlign.Center,
//                    fontSize = if (isSelected) 18.sp else 14.sp,
//                    fontFamily = Montserrat,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//    }
//
//    // Optional: scroll to initial centered item
//    LaunchedEffect(Unit) {
//        listState.scrollToItem(items.size / 2)
//    }
//}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun CarouselModeSelector(items: List<String>) {
//    val pagerState = rememberPagerState(
//        initialPage = 1, // middle of first 3
//        pageCount = { items.size }
//    )
//
//    HorizontalPager(
//        state = pagerState,
//        pageSpacing = 0.dp,
//        beyondViewportPageCount = 2,
//        contentPadding = PaddingValues(horizontal = 100.dp), // 3 items
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(60.dp)
//            .padding(horizontal = 40.dp)
//    ) { page ->
//        val isSelected = pagerState.currentPage == page
//
//        Box(
//            modifier = Modifier
//                .graphicsLayer {
//                    alpha = if (isSelected) 1f else 0.5f
//                    scaleX = if (isSelected) 1f else 0.9f
//                    scaleY = if (isSelected) 1f else 0.9f
//                }
//                .fillMaxHeight(),
//            contentAlignment = Alignment.Center
//        ) {
//            CustomizableGradientText(
//                text = items[page],
//                gradientStartColor = if (isSelected) Color(0xFFDBEBF2) else Color(0xFFF2FBFF),
//                gradientEndColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFB3D4ED),
//                strokeColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFFCFCFC),
//                dropShadowColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFF00B7FF),
//                textAlign = TextAlign.Center,
//                fontSize = if (isSelected) 18.sp else 14.sp,
//                fontFamily = Montserrat,
//                fontWeight = FontWeight.Medium
//            )
//        }
//    }
//}
@RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR2)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PerfectlyCenteredCarousel(items: List<String>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 3

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { items.size }
    )

    HorizontalPager(
        state = pagerState,
        pageSpacing = 0.dp,
        beyondViewportPageCount = 2,
        contentPadding = PaddingValues(horizontal = itemWidth),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        pageSize = PageSize.Fixed(itemWidth) // ✅ FIX THIS FOR PROPER CENTERING
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

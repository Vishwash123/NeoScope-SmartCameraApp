package com.example.cameraapp.Navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultEnter(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultExit(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopEnter(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopExit(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

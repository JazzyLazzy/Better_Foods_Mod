package com.jazzylazzy.BetterFoods.util


fun convertDropletsToMb(droplets: Long): Long {
    return droplets / 81
}

fun convertMbToDroplets(mb: Long): Long {
    return mb * 81
}
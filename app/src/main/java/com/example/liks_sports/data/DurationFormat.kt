package com.example.liks_sports.data

fun formatClockDuration(seconds: Int): String =
    "%d:%02d".format(seconds / 60, seconds % 60)

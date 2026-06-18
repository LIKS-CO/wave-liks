package com.example.liks_sports.data

sealed interface DownloadState {
    data object Idle : DownloadState
    data class Downloading(val progress: Int) : DownloadState
    data class Error(val message: String) : DownloadState
}

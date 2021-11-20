package com.sbb

data class Brawl(
    var board1: Board,
    var board2: Board,
) {
    enum class Result {
        BOARD1_WIN,
        BOARD2_WIN,
        TIE,
    }
}

/**
 * @author Larry
 */
val defaultPointOf: (Piece) -> Double = {
    when (it) {
        Piece.PAWN -> 1.0
        Piece.ROOK -> 5.0
        Piece.BISHOP -> 3.0
        Piece.QUEEN -> 9.0
        Piece.KING -> 0.0
        Piece.KNIGHT -> 2.5
    }
}

val calculateScore: (Board) -> Map<Color, Double> = { board ->
    board.tiles.filterIsInstance(Tile.ColoredPiece::class.java)
        .groupBy { it.color }
        .mapValues {
            val royals = it.value.filter { v -> v.piece != Piece.PAWN }.map { t -> t.piece }
            val pawns = it.value.filter { v -> v.piece == Piece.PAWN }
            calculatePieces(royals, pawns)
        }
}

fun calculatePieces(royals: List<Piece>, pawns: List<Tile.ColoredPiece>): Double {
    return royals.map { defaultPointOf(it) }.sum() + calculatePawnScore(pawns)
}

fun calculatePawnScore(pawns: List<Tile.ColoredPiece>): Double {
    assert(pawns.all { it.piece == Piece.PAWN })
    return pawns.groupBy { it.coord.file }.map {
        val sum = it.value.map { p -> defaultPointOf(p.piece) }.sum()
        if (it.value.size > 1) sum.div(2)
        else sum
    }.sum()
}
/**
 * @author Larry
 */
val printPicture: (Picture) -> Unit = { picture ->
    println(picture)
}
val printBoard: (Board) -> Unit = {
    board -> printPicture(drawBoard(board))
}
val compositePicture: (Picture) -> (Array<Picture>) -> Picture =
    { origin ->
        { newPictures -> "$origin${newPictures.joinToString(separator = "")}" }
    }

val drawTile: (Tile) -> Picture = { representationToPicture(representationOf(it)) }

val selectColorRepresentation: (Color) -> ((Char) -> Char) = {
    when (it) {
        Color.BLACK -> { c -> c.toUpperCase() }
        Color.WHITE -> { c -> c }
    }
}

val baseRepresentationOf: (Piece) -> Char = {
    when (it) {
        Piece.PAWN -> 'p'
        Piece.ROOK -> 'r'
        Piece.KING -> 'k'
        Piece.KNIGHT -> 'n'
        Piece.QUEEN -> 'q'
        Piece.BISHOP -> 'b'
    }
}

val representationOf: (Tile) -> Char = { tile ->
    when (tile) {
        is Tile.Blank -> '.'
        is Tile.ColoredPiece -> selectColorRepresentation(tile.color)(baseRepresentationOf(tile.piece))
    }
}

val representationToPicture: (Char) -> Picture = {
    it.toString()
}

fun drawBoard(board: Board): Picture {
    tailrec fun helper(cnt: Int, acc: Picture, tiles: List<Tile>): Picture =
        when {
            tiles.isEmpty() -> acc
            cnt == LENGTH_OF_FILE -> helper(
                cnt = 1,
                acc = compositePicture(acc)(arrayOf(drawTile(tiles.head()), "\n")),
                tiles = tiles.tail()
            )
            else -> helper(
                cnt = cnt + 1,
                acc = compositePicture(acc)(arrayOf(drawTile(tiles.head()))),
                tiles = tiles.tail()
            )
        }
    return helper(
        1, "", board.tiles
            .sortedWith(
                compareByDescending<Tile> { it.coord.rank }
                    .thenBy { it.coord.file })).trimIndent()
}

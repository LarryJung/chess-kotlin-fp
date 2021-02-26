/**
 * @author Larry
 */

// 초기화 기능 모음
val initializeBoard: () -> Board = {
    val tiles: List<Tile> = (0 until LENGTH_OF_FILE).flatMap(createRank)
    Board(tiles)
}

val createTile: (Color?) -> (Piece?) -> (Position) -> Tile =
    { color ->
        { piece ->
            { position ->
                if (color == null || piece == null) Tile.Blank(position)
                else Tile.ColoredPiece(color, piece, position)
            }
        }
    }

val whitePieceOf: (Piece?) -> (Position) -> Tile = createTile(Color.WHITE)
val blackPieceOf: (Piece?) -> (Position) -> Tile = createTile(Color.BLACK)
val blankOf: (Position) -> Tile = createTile(null)(null)

val createRank: (Int) -> List<Tile> = { rankIndex ->
    when (rankIndex) {
        0 -> listOf(
            whitePieceOf(Piece.ROOK)(positionOfIndex(rankIndex)(0)),
            whitePieceOf(Piece.KNIGHT)(positionOfIndex(rankIndex)(1)),
            whitePieceOf(Piece.BISHOP)(positionOfIndex(rankIndex)(2)),
            whitePieceOf(Piece.KING)(positionOfIndex(rankIndex)(3)),
            whitePieceOf(Piece.QUEEN)(positionOfIndex(rankIndex)(4)),
            whitePieceOf(Piece.BISHOP)(positionOfIndex(rankIndex)(5)),
            whitePieceOf(Piece.KNIGHT)(positionOfIndex(rankIndex)(6)),
            whitePieceOf(Piece.ROOK)(positionOfIndex(rankIndex)(7)),
        )
        1 -> (0 until LENGTH_OF_FILE).map { whitePieceOf(Piece.PAWN)(positionOfIndex(rankIndex)(it)) }
        2, 3, 4, 5 -> (0 until LENGTH_OF_FILE).map { blankOf(positionOfIndex(rankIndex)(it)) }
        6 -> (0 until LENGTH_OF_FILE).map { blackPieceOf(Piece.PAWN)(positionOfIndex(rankIndex)(it)) }
        7 -> listOf(
            blackPieceOf(Piece.ROOK)(positionOfIndex(rankIndex)(0))
        )
        else -> throw RuntimeException("wrong rankIndex range. must between 0 and 7")
    }
}

val positionOfIndex: (RankIndex) -> (FileIndex) -> Position =
    { rank ->
        { file ->
            Position(rank, file)
        }
    }

val drawBoard: (Board) -> Picture = {

    """
            ........
            PPPPPPPP
            ........
            ........
            ........
            pppppppp
            ........
        """.trimIndent()
}

val printPicture: (Picture) -> Unit = { picture ->
    println(picture)
}

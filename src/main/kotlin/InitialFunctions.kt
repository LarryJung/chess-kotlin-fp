/**
 * @author Larry
 */

// 초기화 기능 모음
val initializeBoard: () -> Board = {
    val tiles: List<Tile> = (0 until LENGTH_OF_RANK).flatMap(rankOf)
    Board(tiles)
}

val initializeEmptyBoard: () -> Board = {
    val tiles: List<Tile> =
        (0 until LENGTH_OF_RANK).flatMap { r ->
            (0 until LENGTH_OF_FILE).map { f ->
                blankOf(coordOf(r)(f))
            }
        }
    Board(tiles)
}

val createTile: (Color?) -> (Piece?) -> (Coord) -> Tile =
    { color ->
        { piece ->
            { coord ->
                if (color == null || piece == null) Tile.Blank(coord)
                else Tile.ColoredPiece(color, piece, coord)
            }
        }
    }

val whitePieceOf: (Piece?) -> (Coord) -> Tile = createTile(Color.WHITE)

val blackPieceOf: (Piece?) -> (Coord) -> Tile = createTile(Color.BLACK)

val blankOf: (Coord) -> Tile = createTile(null)(null)

val coordOf: (RankIndex) -> (FileIndex) -> Coord =
    { rank ->
        { file ->
            Coord(rank, file)
        }
    }

val coordOfString: (String) -> Coord =
    { s ->
        val xPos = s[0] - 'a'
        val yPos = Character.getNumericValue(s[1]) - 1
        Coord(yPos, xPos)
    }

val rankOf: (Int) -> List<Tile> = { rank ->
    when (rank) {
        0 -> listOf(
            whitePieceOf(Piece.ROOK)(coordOf(rank)(0)),
            whitePieceOf(Piece.KNIGHT)(coordOf(rank)(1)),
            whitePieceOf(Piece.BISHOP)(coordOf(rank)(2)),
            whitePieceOf(Piece.KING)(coordOf(rank)(3)),
            whitePieceOf(Piece.QUEEN)(coordOf(rank)(4)),
            whitePieceOf(Piece.BISHOP)(coordOf(rank)(5)),
            whitePieceOf(Piece.KNIGHT)(coordOf(rank)(6)),
            whitePieceOf(Piece.ROOK)(coordOf(rank)(7)),
        )
        1 -> (0 until LENGTH_OF_FILE).map { whitePieceOf(Piece.PAWN)(coordOf(rank)(it)) }
        2, 3, 4, 5 -> (0 until LENGTH_OF_FILE).map { blankOf(coordOf(rank)(it)) }
        6 -> (0 until LENGTH_OF_FILE).map { blackPieceOf(Piece.PAWN)(coordOf(rank)(it)) }
        7 -> listOf(
            blackPieceOf(Piece.ROOK)(coordOf(rank)(0)),
            blackPieceOf(Piece.KNIGHT)(coordOf(rank)(1)),
            blackPieceOf(Piece.BISHOP)(coordOf(rank)(2)),
            blackPieceOf(Piece.KING)(coordOf(rank)(3)),
            blackPieceOf(Piece.QUEEN)(coordOf(rank)(4)),
            blackPieceOf(Piece.BISHOP)(coordOf(rank)(5)),
            blackPieceOf(Piece.KNIGHT)(coordOf(rank)(6)),
            blackPieceOf(Piece.ROOK)(coordOf(rank)(7)),
        )
        else -> throw RuntimeException("wrong rankIndex range. must between 0 and 7")
    }
}

/**
 * @author Larry
 */
fun positionPiece(coord: Coord, color: Color, piece: Piece): (Board) -> Board =
    { board ->
        Board(
            board.tiles.map {
                if (it.coord == coord) Tile.ColoredPiece(color, piece, coord)
                else it
            }
        )
    }

val findPiece: (String) -> (Board) -> Tile? =
    { s ->
        { b ->
            b.tiles.find { it.coord == coordOfString(s) }
        }
    }

fun move(board: Board, source: String, dest: String): Board {
    val sourceTile: Tile =
        board.tiles.find { it.coord == coordOfString(source) } ?: throw RuntimeException("wrong position")
    tailrec fun helper(rest: List<Tile>, acc: List<Tile>): List<Tile> {
        return when {
            rest.isEmpty() -> acc
            movable() && rest.head().coord == coordOfString(source) -> {
                helper(rest.tail(), acc + Tile.Blank(rest.head().coord))
            }
            movable() && rest.head().coord == coordOfString(dest)
                    && sourceTile is Tile.ColoredPiece -> {
                helper(rest.tail(), acc + Tile.ColoredPiece(sourceTile.color, sourceTile.piece, rest.head().coord))
            }
            else -> helper(rest.tail(), acc + rest.head())
        }
    }
    return Board(helper(board.tiles, emptyList()))
}

val movable: () -> Boolean = {
    // 각 기물간 이동가능여부 파악
    true
}

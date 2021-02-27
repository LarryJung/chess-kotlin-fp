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
    { coord ->
        { board ->
            board.tiles.find { it.coord == coordOfString(coord) }
        }
    }

fun move(board: Board, source: String, dest: String): Board {
    val sourceCoord = coordOfString(source)
    val destCoord = coordOfString(dest)
    val sourceTile: Tile =
        board.tiles.find { it.coord == coordOfString(source) } ?: throw RuntimeException("wrong position")

    tailrec fun helper(rest: List<Tile>, acc: List<Tile>): List<Tile> {
        return when {
            rest.isEmpty() -> acc
            movable(sourceTile)(destCoord)(rest.head())
                    && rest.head().coord == sourceCoord -> {
                helper(rest.tail(), acc + Tile.Blank(rest.head().coord))
            }
            movable(sourceTile)(destCoord)(rest.head())
                    && rest.head().coord == destCoord
                    && sourceTile is Tile.ColoredPiece -> {
                helper(rest.tail(), acc + Tile.ColoredPiece(sourceTile.color, sourceTile.piece, rest.head().coord))
            }
            else -> helper(rest.tail(), acc + rest.head())
        }
    }
    return Board(helper(board.tiles, emptyList()))
}

val movable: (source: Tile) -> (dest: Coord) -> (now: Tile) -> Boolean =
    { source ->
        { dest ->
            { now ->
                when (source) {
                    is Tile.Blank -> true
                    is Tile.ColoredPiece -> {
                        val vector = calculateVector(source.coord)(dest)
                        if (vector == null) false
                        else possibleDirections(source.piece)(source.color).find { it == vector } != null
                    }
                }
            }
        }
    }

val applyDirection: (Direction) -> (Coord) -> Coord =
    { direction ->
        { coord ->
            when (direction) {
                Direction.NORTH -> Coord(coord.rank + 1, coord.file)
                Direction.NORTHEAST -> Coord(coord.rank + 1, coord.file + 1)
                Direction.EAST -> Coord(coord.rank, coord.file + 1)
                Direction.SOUTHEAST -> Coord(coord.rank - 1, coord.file + 1)
                Direction.SOUTH -> Coord(coord.rank - 1, coord.file)
                Direction.SOUTHWEST -> Coord(coord.rank - 1, coord.file - 1)
                Direction.WEST -> Coord(coord.rank, coord.file - 1)
                Direction.NORTHWEST -> Coord(coord.rank + 1, coord.file - 1)
                Direction.NNE -> Coord(coord.rank + 2, coord.file + 1)
                Direction.NNW -> Coord(coord.rank + 2, coord.file - 1)
                Direction.SSE -> Coord(coord.rank - 2, coord.file + 1)
                Direction.SSW -> Coord(coord.rank - 2, coord.file - 1)
                Direction.EEN -> Coord(coord.rank + 1, coord.file + 2)
                Direction.EES -> Coord(coord.rank - 1, coord.file + 2)
                Direction.WWN -> Coord(coord.rank + 1, coord.file - 2)
                Direction.WWS -> Coord(coord.rank - 1, coord.file - 2)
            }
        }
    }

val possibleDirections: (Piece) -> (Color) -> Array<Direction> =
    { piece ->
        { color ->
            when (piece) {
                Piece.PAWN -> {
                    if (color == Color.WHITE) Direction.whitePawnDirection()
                    else Direction.blackPawnDirection()
                }
                Piece.KING -> Direction.everyDirection()
                Piece.QUEEN -> Direction.everyDirection()
                Piece.ROOK -> Direction.linearDirection()
                Piece.BISHOP -> Direction.diagonalDirection()
                Piece.KNIGHT -> Direction.knightDirection()
            }
        }
    }

// 재귀로 여러번 돌려야 할듯. 벡터도 아님.. 벡터는 방향과 크기를 가져야함
val calculateVector: (source: Coord) -> (dest: Coord) -> Direction? =
    { source ->
        { dest ->
            Direction.values().find { applyDirection(it)(source) == dest }
        }
    }
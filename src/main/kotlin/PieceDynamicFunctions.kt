import java.lang.Math.toDegrees
import kotlin.math.atan2
import kotlin.math.floor

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
            movableDirection(sourceTile)(destCoord)(rest.head())
                    && rest.head().coord == sourceCoord -> {
                helper(rest.tail(), acc + Tile.Blank(rest.head().coord))
            }
            movableDirection(sourceTile)(destCoord)(rest.head())
                    && rest.head().coord == destCoord
                    && sourceTile is Tile.ColoredPiece -> {
                helper(rest.tail(), acc + Tile.ColoredPiece(sourceTile.color, sourceTile.piece, rest.head().coord))
            }
            else -> helper(rest.tail(), acc + rest.head())
        }
    }
    return Board(helper(board.tiles, emptyList()))
}

val movableDirection: (source: Tile) -> (dest: Coord) -> (now: Tile) -> Boolean =
    { source ->
        { dest ->
            { now ->
                when (source) {
                    is Tile.Blank -> true
                    is Tile.ColoredPiece -> {
                        val direction = Direction.findDirection(source.coord, dest)
                        if (direction == null) false
                        else possibleDirections(source.piece)(source.color).find { it == direction } != null
                    }
                }
            }
        }
    }

val movable: (now: Tile) -> (neighbors: List<Tile>) -> (dest: Tile) -> Boolean = { now ->
    { neighbors ->
        { dest ->
            // 각 기물에 대해서 판단
            true
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

fun getAngle(source: Coord, target: Coord): Float {
    var angle = toDegrees(
        atan2(
            target.rank.toDouble() - source.rank.toDouble(),
            target.file.toDouble() - source.file.toDouble()
        )
    ).toFloat()
    if (angle < 0) {
        angle += 360f
    }
    return floor(angle)
}
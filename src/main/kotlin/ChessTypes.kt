/**
 * @author Larry
 */
typealias Picture = String
typealias RankIndex = Int
typealias FileIndex = Int

enum class Color { WHITE, BLACK }
enum class Piece { PAWN, KNIGHT, BISHOP, KING, QUEEN, ROOK }
data class Coord(val rank: RankIndex, val file: FileIndex)
data class Board(val tiles: List<Tile>)
sealed class Tile(open val coord: Coord) {
    class ColoredPiece(val color: Color, val piece: Piece, override val coord: Coord) : Tile(coord)
    class Blank(coord: Coord) : Tile(coord)
}

enum class Direction(val yDiff: Int, val xDiff: Int) {
    NORTH(1, 0),
    NORTHEAST(1, 1),
    EAST(0, 1),
    SOUTHEAST(-1, 1),
    SOUTH(-1, 0),
    SOUTHWEST(-1, -1),
    WEST(0, -1),
    NORTHWEST(1, -1),
    NNE(2, 1),
    NNW(2, -1),
    SSE(-2, 1),
    SSW(-2, -1),
    EEN(1, 2),
    EES(-1, 2),
    WWN(1, -2),
    WWS(-1, -2);

    fun isSameDirection(source: Coord, dest: Coord): Boolean {
        val thisAngle = getAngle(Coord(0,0), Coord(this.yDiff, this.yDiff))
        return getAngle(source, dest) == thisAngle
    }
    companion object {
        fun linearDirection(): Array<Direction> = arrayOf(NORTH, EAST, SOUTH, WEST)
        fun diagonalDirection(): Array<Direction> = arrayOf(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
        fun everyDirection(): Array<Direction> =
            arrayOf(NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
        fun knightDirection(): Array<Direction> = arrayOf(NNE, NNW, SSE, SSW, EEN, EES, WWN, WWS)
        fun whitePawnDirection(): Array<Direction> = arrayOf(NORTH, NORTHEAST, NORTHWEST)
        fun blackPawnDirection(): Array<Direction> = arrayOf(SOUTH, SOUTHEAST, SOUTHWEST)

        fun findDirection(source: Coord, dest: Coord): Direction? {
            return values().find { it.isSameDirection(source, dest) }
        }
    }
}

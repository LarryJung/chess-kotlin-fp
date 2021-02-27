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

enum class Direction {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST,
    NNE,
    NNW,
    SSE,
    SSW,
    EEN,
    EES,
    WWN,
    WWS;

    companion object {
        fun linearDirection(): Array<Direction> = arrayOf(NORTH, EAST, SOUTH, WEST)
        fun diagonalDirection(): Array<Direction> = arrayOf(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
        fun everyDirection(): Array<Direction> = arrayOf(NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
        fun knightDirection(): Array<Direction> = arrayOf(NNE, NNW, SSE, SSW, EEN, EES, WWN, WWS)
        fun whitePawnDirection(): Array<Direction> = arrayOf(NORTH, NORTHEAST, NORTHWEST)
        fun blackPawnDirection(): Array<Direction> = arrayOf(SOUTH, SOUTHEAST, SOUTHWEST)
    }
}

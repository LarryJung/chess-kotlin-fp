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



import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException


/**
 * @author Larry
 */
class BoardTest {
    @Test
    fun `create - borad`() {
        val board = Board.init()
        assertEquals(64, board.pieceCount())
        val blankRank = "........".appendNewLine()
        assertEquals(
            "RNBQKBNR".appendNewLine() +
                    "PPPPPPPP".appendNewLine() +
                    blankRank + blankRank + blankRank + blankRank +
                    "pppppppp".appendNewLine() +
                    "rnbqkbnr".appendNewLine(),
            board.showBoard()
        )
        assertEquals(2, board.pieceCount(PieceName.KING))
        assertEquals(16, board.pieceCount(PieceName.PAWN))

        assertEquals(PieceName.ROOK, board.findPiece("a8").name)
        assertEquals(PieceColor.BLACK, board.findPiece("a8").color)
        assertEquals(PieceName.ROOK, board.findPiece("h8").name)
        assertEquals(PieceColor.BLACK, board.findPiece("h8").color)

        assertEquals(PieceName.ROOK, board.findPiece("a1").name)
        assertEquals(PieceColor.WHITE, board.findPiece("a1").color)
        assertEquals(PieceName.ROOK, board.findPiece("h1").name)
        assertEquals(PieceColor.WHITE, board.findPiece("h1").color)

        println(board.showBoard())
        board.move(standee = Standee.blackRook(), "a8", "a2")
        println(board.showBoard())
        println(board.calculate())
    }

    @Test
    fun calculatePointTest() {
        val board = Board.empty()
        board.setPosition(Standee.blackPawn(), "b6")
        board.setPosition(Standee.blackQueen(), "e6")
        board.setPosition(Standee.blackKing(), "b8")
        board.setPosition(Standee.blackRook(), "c8")
        board.setPosition(Standee.whitePawn(), "f2")
        board.setPosition(Standee.whitePawn(), "g2")
        board.setPosition(Standee.whiteRook(), "e1")
        board.setPosition(Standee.whiteKing(), "f1")
        println(board.showBoard())
        assertEquals(7.0, board.calculate()[PieceColor.WHITE])
        assertEquals(15.0, board.calculate()[PieceColor.BLACK])

        board.clear()
        board.setPosition(Standee.blackKing(), "b8")
        board.setPosition(Standee.blackRook(), "c8")
        board.setPosition(Standee.blackPawn(), "a7")
        board.setPosition(Standee.blackPawn(), "c7")
        board.setPosition(Standee.blackPawn(), "b6")
        board.setPosition(Standee.blackBishop(), "d7")
        board.setPosition(Standee.blackQueen(), "e6")

        board.setPosition(Standee.whiteKnight(), "f4")
        board.setPosition(Standee.whiteQueen(), "g4")
        board.setPosition(Standee.whitePawn(), "f3")
        board.setPosition(Standee.whitePawn(), "h3")
        board.setPosition(Standee.whitePawn(), "f2")
        board.setPosition(Standee.whitePawn(), "g2")
        board.setPosition(Standee.whiteRook(), "e1")
        board.setPosition(Standee.whiteKing(), "f1")

        println(board.showBoard())
        assertEquals(19.5, board.calculate()[PieceColor.WHITE])
        assertEquals(20.0, board.calculate()[PieceColor.BLACK])
    }
}

class Board(
    private var boardPositions: MutableList<BoardPosition> = mutableListOf()
) {
    fun pieceCount(): Int {
        return this.boardPositions.size
    }

    private fun findPiece(x: Int, y: Int): Standee {
        return findPosition(x, y).standee
    }

    private fun findPosition(x: Int, y: Int): BoardPosition {
        return boardPositions.find { it.position == Position(x, y) }!!
    }

    private fun findPosition(positionString: String): BoardPosition {
        return boardPositions.find { it.position == Position.of(positionString) }!!
    }

    fun findPiece(positionString: String): Standee {
        return boardPositions.find { it.position == Position.of(positionString) }!!.standee
    }

    fun showBoard(): String =
        (boardSize.second - 1 downTo 0)
            .map { yPos ->
                (0 until boardSize.first).map { xPos ->
                    findPiece(xPos, yPos).getRepresentation()
                }.joinToString("")
            }.joinToString(separator, postfix = separator)

    fun pieceCount(pieceName: PieceName) = this.boardPositions.count { it.standee.name == pieceName }

    fun move(standee: Standee, from: String, to: String) {
        val oldStandee: Standee = findPiece(from)
        if (oldStandee != standee) {
            throw RuntimeException(
                "threre is no piece. ${standee}, position: $from"
            )
        }

        // BoardPosition.validateMove(findPosition(from), findPosition(to), PieceKind.getDirections(standee))

        println("$from 의 ${oldStandee.getStandeeName()} 를 움직입니다.")
        findPosition(from).set(Standee.noPiece())
        println("$to 의 ${findPiece(to).getStandeeName()} 이 먹혔습니다.")
        findPosition(to).set(oldStandee)
    }

    fun setPosition(standee: Standee, to: String) {
        findPosition(to).set(standee)
    }

    fun calculate(): Map<PieceColor, Double> =
        mapOf(
            PieceColor.WHITE to calculate(this.boardPositions.filter { it.standee.color == PieceColor.WHITE }),
            PieceColor.BLACK to calculate(this.boardPositions.filter { it.standee.color == PieceColor.BLACK })
        )

    fun clear() {
        this.boardPositions = makeEmptyPieces().toMutableList()
    }

    companion object {
        val boardSize = Pair(8, 8)

        fun init(): Board {
            return Board(makeInitialPieces().toMutableList())
        }

        fun empty(): Board {
            return Board(makeEmptyPieces().toMutableList())
        }
    }
}

enum class PieceColor {
    WHITE, BLACK, NO_COLOR
}

enum class PieceName {
    PAWN,
    KNIGHT,
    ROOK,
    BISHOP,
    QUEEN,
    KING,
    NO_PIECE
}

enum class PieceKind(
    val pieceName: PieceName,
    private val repre: Char,
    val defaultPoint: Double,
    val directionFunc: (PieceColor) -> List<Direction>,
    val initPositions: Map<PieceColor, Array<String>>
) {
    PAWN_KIND(
        PieceName.PAWN,
        'p',
        1.0,
        {
            when (it) {
                PieceColor.BLACK -> Direction.blackPawnDirection()
                PieceColor.WHITE -> Direction.whitePawnDirection()
                else -> throw RuntimeException("wrong color")
            }
        },
        mapOf(
            PieceColor.BLACK to arrayOf("a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"),
            PieceColor.WHITE to arrayOf("a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2")
        )
    ) {
        override fun calculatePoint(list: List<Position>): Double =
            list.groupBy { it.x }.map { xLinePawns ->
                if (xLinePawns.value.size > 1) {
                    (this.defaultPoint / 2) * xLinePawns.value.size
                } else {
                    this.defaultPoint * xLinePawns.value.size
                }
            }.sumByDouble { it }
    },
    KNIGHT_KIND(
        PieceName.KNIGHT,
        'n',
        2.5,
        {
            Direction.knightDirection()
        },
        mapOf(
            PieceColor.BLACK to arrayOf("b8", "g8"),
            PieceColor.WHITE to arrayOf("b1", "g1")
        )
    ) {
        override fun calculatePoint(list: List<Position>): Double {
            return list.sumByDouble { this.defaultPoint }
        }
    },
    ROOK_KIND(
        PieceName.ROOK,
        'r',
        5.0,
        {
            Direction.linearDirection()
        },
        mapOf(
            PieceColor.BLACK to arrayOf("a8", "h8"),
            PieceColor.WHITE to arrayOf("a1", "h1")
        )
    ) {
        override fun calculatePoint(list: List<Position>): Double {
            return list.sumByDouble { this.defaultPoint }
        }
    },
    BISHOP_KIND(
        PieceName.BISHOP,
        'b',
        3.0,
        {
            Direction.diagonalDirection()
        },
        mapOf(
            PieceColor.BLACK to arrayOf("c8", "f8"),
            PieceColor.WHITE to arrayOf("c1", "f1")
        )
    ) {
        override fun calculatePoint(list: List<Position>): Double {
            return list.sumByDouble { this.defaultPoint }
        }
    },
    QUEEN_KIND(
        PieceName.QUEEN,
        'q',
        9.0,
        {
            Direction.everyDirection()
        },
        mapOf(
            PieceColor.BLACK to arrayOf("d8"),
            PieceColor.WHITE to arrayOf("d1")
        )
    ) {
        override fun calculatePoint(list: List<Position>): Double {
            return list.sumByDouble { this.defaultPoint }
        }
    },
    KING_KIND(
        PieceName.KING,
        'k',
        0.0,
        {
            Direction.everyDirection()
        },
        mapOf(
            PieceColor.BLACK to arrayOf("e8"),
            PieceColor.WHITE to arrayOf("e1")
        )
    ) {
        override fun calculatePoint(list: List<Position>): Double {
            return list.sumByDouble { this.defaultPoint }
        }
    },
    NO_PIECE_KIND(
        PieceName.NO_PIECE,
        '.',
        0.0,
        {
            throw RuntimeException("not supported.")
        },
        mapOf(
            PieceColor.NO_COLOR to arrayOf(
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
            )
        )
    ) {
        override fun calculatePoint(list: List<Position>): Double {
            return list.sumByDouble { this.defaultPoint }
        }
    };

    abstract fun calculatePoint(list: List<Position>): Double

    fun getRepresentation(color: PieceColor) =
        when (color) {
            PieceColor.BLACK -> this.repre.toUpperCase()
            PieceColor.WHITE -> this.repre.toLowerCase()
            else -> this.repre
        }

    companion object {
        private fun getPieceKind(pieceName: PieceName) = values().find { it.pieceName == pieceName }!!
        fun getRepresentation(pieceName: PieceName, color: PieceColor): Char {
            return getPieceKind(pieceName).getRepresentation(color)
        }

        fun calculate(pieceName: PieceName, positions: List<Position>): Double {
            return getPieceKind(pieceName).calculatePoint(positions)
        }

        fun getDirections(standee: Standee): List<Direction> {
            return getPieceKind(standee.name).directionFunc(standee.color)
        }
    }
}

private fun makeInitialPieces(): List<BoardPosition> {
    return PieceKind.values().flatMap { kind ->
        kind.initPositions.flatMap { initPositionMap ->
            initPositionMap.value.map { initPositionString ->
                BoardPosition(
                    standee = Standee(
                        kind.pieceName,
                        initPositionMap.key
                    ),
                    position = Position.of(initPositionString)
                )
            }
        }
    }
}

private fun makeEmptyPieces(): List<BoardPosition> {
    return (0..7).flatMap { x -> (0..7).map { y -> BoardPosition(Standee.noPiece(), Position(x, y)) } }
}

private val separator = System.getProperty("line.separator")

fun String.appendNewLine() = this + separator

data class Position(val x: Int, val y: Int) {
    companion object {
        fun of(positionString: String): Position {
            val x: Char = positionString[0]
            val xPos: Int = x - 'a'
            val y = positionString[1]
            val yPos: Int = Character.getNumericValue(y) - 1
            return Position(xPos, yPos)
        }
    }
}

data class BoardPosition(
    var standee: Standee,
    val position: Position
) {
    fun set(target: Standee) {
        this.standee = target
    }
}

private fun calculate(boardPositions: List<BoardPosition>): Double =
    boardPositions
        .groupBy { it.standee }
        .map { it -> PieceKind.calculate(it.key.name, it.value.map { it.position }) }
        .sumByDouble { it }

data class Standee(
    val name: PieceName,
    val color: PieceColor
) {
    companion object {
        fun whitePawn(): Standee = Standee(PieceName.PAWN, PieceColor.WHITE)
        fun whiteRook(): Standee = Standee(PieceName.ROOK, PieceColor.WHITE)
        fun whiteKnight(): Standee = Standee(PieceName.KNIGHT, PieceColor.WHITE)
        fun whiteQueen(): Standee = Standee(PieceName.QUEEN, PieceColor.WHITE)
        fun whiteKing(): Standee = Standee(PieceName.KING, PieceColor.WHITE)
        fun whiteBishop(): Standee = Standee(PieceName.BISHOP, PieceColor.WHITE)

        fun blackPawn(): Standee = Standee(PieceName.PAWN, PieceColor.BLACK)
        fun blackRook(): Standee = Standee(PieceName.ROOK, PieceColor.BLACK)
        fun blackKnight(): Standee = Standee(PieceName.KNIGHT, PieceColor.BLACK)
        fun blackQueen(): Standee = Standee(PieceName.QUEEN, PieceColor.BLACK)
        fun blackKing(): Standee = Standee(PieceName.KING, PieceColor.BLACK)
        fun blackBishop(): Standee = Standee(PieceName.BISHOP, PieceColor.BLACK)
        fun noPiece(): Standee = Standee(PieceName.NO_PIECE, PieceColor.NO_COLOR)
    }

    fun getRepresentation(): Char = PieceKind.getRepresentation(name, color)
    fun getStandeeName(): String = "${name}(${color})"
}

enum class Direction(
    val xDegree: Int,
    val yDegree: Int
) {
    NORTH(0, 1),
    NORTHEAST(1, 1),
    EAST(1, 0),
    SOUTHEAST(1, -1),
    SOUTH(0, -1),
    SOUTHWEST(-1, -1),
    WEST(-1, 0),
    NORTHWEST(-1, 1),
    NNE(1, 2),
    NNW(-1, 2),
    SSE(1, -2),
    SSW(-1, -2),
    EEN(2, 1),
    EES(2, -1),
    WWN(-2, 1),
    WWS(-2, -1);

    companion object {
        fun linearDirection(): List<Direction> {
            return listOf(NORTH, EAST, SOUTH, WEST)
        }

        fun diagonalDirection(): List<Direction> {
            return listOf(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
        }

        fun everyDirection(): List<Direction> {
            return listOf(NORTH, EAST, SOUTH, WEST, NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
        }

        fun knightDirection(): List<Direction> {
            return listOf(NNE, NNW, SSE, SSW, EEN, EES, WWN, WWS)
        }

        fun whitePawnDirection(): List<Direction> {
            return listOf(NORTH, NORTHEAST, NORTHWEST)
        }

        fun blackPawnDirection(): List<Direction> {
            return listOf(SOUTH, SOUTHEAST, SOUTHWEST)
        }
    }
}
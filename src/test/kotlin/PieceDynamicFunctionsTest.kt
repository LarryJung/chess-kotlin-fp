import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Larry
 */
class PieceDynamicFunctionsTest {

    @Test
    fun piecePositionTest() {
        val placedBoard: Board =
            positionPiece(coordOf(6)(0), Color.BLACK, Piece.PAWN)
                .andThen(positionPiece(coordOf(5)(1), Color.BLACK, Piece.PAWN))
                .andThen(positionPiece(coordOf(7)(1), Color.BLACK, Piece.KING))
                .andThen(positionPiece(coordOf(6)(2), Color.BLACK, Piece.PAWN))
                .andThen(positionPiece(coordOf(6)(3), Color.BLACK, Piece.BISHOP))
                .andThen(positionPiece(coordOf(7)(2), Color.BLACK, Piece.ROOK))
                .andThen(positionPiece(coordOf(5)(4), Color.BLACK, Piece.QUEEN))
                .andThen(positionPiece(coordOf(0)(4), Color.WHITE, Piece.ROOK))
                .andThen(positionPiece(coordOf(0)(5), Color.WHITE, Piece.KING))
                .andThen(positionPiece(coordOf(2)(5), Color.WHITE, Piece.PAWN))
                .andThen(positionPiece(coordOf(3)(5), Color.WHITE, Piece.KNIGHT))
                .andThen(positionPiece(coordOf(1)(6), Color.WHITE, Piece.PAWN))
                .andThen(positionPiece(coordOf(3)(6), Color.WHITE, Piece.QUEEN))
                .invoke(initializeEmptyBoard())

        val expectBoard = """
            .KR.....
            P.PB....
            .P..Q...
            ........
            .....nq.
            .....p..
            ......p.
            ....rk..
        """.trimIndent()
        printBoard(placedBoard)
        Assertions.assertEquals(expectBoard, drawBoard(placedBoard))
    }

    @Test
    fun findPieceTest() {
        val board = initializeBoard()
        val tile = findPiece("a8")(board) as Tile.ColoredPiece
        Assertions.assertEquals(tile.piece, Piece.ROOK)
        Assertions.assertEquals(tile.color, Color.BLACK)
    }

    @Test
    fun moveTest() {
        val board = initializeBoard()
        val beforeB2 = findPiece("b2")(board)!!
        val beforeB4 = findPiece("b4")(board)!!
        Assertions.assertEquals((beforeB2 as Tile.ColoredPiece).piece, Piece.PAWN)
        Assertions.assertTrue(beforeB4 is Tile.Blank)
        printBoard(board)

        val rePlacedBoard = move(board, source = "b2", dest = "b4")

        val afterB2 = findPiece("b2")(rePlacedBoard)!!
        val afterB4 = findPiece("b4")(rePlacedBoard)!!
        Assertions.assertTrue(afterB2 is  Tile.Blank)
        Assertions.assertEquals((afterB4 as Tile.ColoredPiece).piece, Piece.PAWN)
        printBoard(rePlacedBoard)
    }
}

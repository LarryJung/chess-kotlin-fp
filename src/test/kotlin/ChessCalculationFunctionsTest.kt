import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Larry
 */
class ChessCalculationFunctionsTest {
    @Test
    fun calculateTest() {
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
                .andThen(positionPiece(coordOf(1)(5), Color.WHITE, Piece.PAWN))
                .andThen(positionPiece(coordOf(2)(7), Color.WHITE, Piece.PAWN))
                .andThen(positionPiece(coordOf(3)(6), Color.WHITE, Piece.QUEEN))
                .invoke(initializeEmptyBoard())
        printBoard(placedBoard)
        val scoreMap = calculateScore(placedBoard)
        Assertions.assertEquals(20.0, scoreMap[Color.BLACK])
        Assertions.assertEquals(19.5, scoreMap[Color.WHITE])
    }
}
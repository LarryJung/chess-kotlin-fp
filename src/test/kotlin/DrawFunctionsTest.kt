import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Larry
 */
class DrawFunctionsTest {
    @Test
    fun printPicture() {
        val picture: Picture = "picture"
        printPicture(picture)
    }

    @Test
    fun drawBoard() {
        val board = initializeBoard()
        val boardPicture: Picture = drawBoard(board)
        val initialBoardStr =
            """
                RNBKQBNR
                PPPPPPPP
                ........
                ........
                ........
                ........
                pppppppp
                rnbkqbnr
            """.trimIndent()
        Assertions.assertEquals(boardPicture, initialBoardStr)
    }

    @Test
    fun drawEmptyBoard() {
        val board = initializeEmptyBoard()
        val boardPicture: Picture = drawBoard(board)
        val initialBoardStr =
            """
                ........
                ........
                ........
                ........
                ........
                ........
                ........
                ........
            """.trimIndent()
        Assertions.assertEquals(boardPicture, initialBoardStr)
    }
}

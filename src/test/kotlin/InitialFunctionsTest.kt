import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Larry
 */
class BoardTest {

    @Test
    fun printTest() {
        val picture: Picture = "adf"
        printPicture(picture)
    }

    @Test
    fun showBoard() {
        val initialBoardStr =
            """
                ........
                PPPPPPPP
                ........
                ........
                ........
                pppppppp
                ........
            """.trimIndent()
        val board: Board = initializeBoard()
        val boardPicture: Picture = drawBoard(board)
        Assertions.assertEquals(boardPicture, initialBoardStr)
    }
}
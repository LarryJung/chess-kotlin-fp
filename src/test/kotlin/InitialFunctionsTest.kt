import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Larry
 */
class InitialFunctionsTest {

    @Test
    fun initialTest() {
        val board = initializeBoard()
        Assertions.assertEquals(board.tiles.size, 64)
    }

    @Test
    fun coordTest() {
        val coord1 = coordOfString("a1")
        val coord2 = coordOfString("a8")
        val coord3 = coordOfString("d6")
        Assertions.assertEquals(coord1.file, 0)
        Assertions.assertEquals(coord1.rank, 0)

        Assertions.assertEquals(coord2.file, 0)
        Assertions.assertEquals(coord2.rank, 7)

        Assertions.assertEquals(coord3.file, 3)
        Assertions.assertEquals(coord3.rank, 5)
    }
}

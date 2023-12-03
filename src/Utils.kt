import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

typealias Char2D = Array<CharArray>

fun List<String>.toChar2d() = map { it.toCharArray() }.toTypedArray()

operator fun Char2D.get(i: Int, j: Int) = try {
    this[i][j]
} catch (e: ArrayIndexOutOfBoundsException) {
    null
}

/**
 * Visit the neighbour cells:
 *
 * |1|2|3|
 * |4|#|5|
 * |6|7|8|
 *
 * */
fun <T> Char2D.onNeighbours(i: Int, j: Int, block: (char: Char?, i: Int, j: Int) -> T): List<T> = listOf(
        // 1
        block(this[i - 1, j - 1], i - 1, j - 1),
        // 2Neighbours
        block(this[i - 1, j], i - 1, j),
        // 3
        block(this[i - 1, j + 1], i - 1, j + 1),
        // 4
        block(this[i, j - 1], i, j - 1),
        // 5
        block(this[i, j + 1], i, j + 1),
        // 6
        block(this[i + 1, j - 1], i + 1, j - 1),
        // 7
        block(this[i + 1, j], i + 1, j),
        // 8
        block(this[i + 1, j + 1], i + 1, j + 1)
)

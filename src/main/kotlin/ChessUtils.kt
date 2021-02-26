/**
 * @author Larry
 */
fun <T> List<T>.head() = this.first()
fun <T> List<T>.tail() = this.drop(1)

fun <T, U, V> ((T) -> U).andThen(f: (U) -> V): (T) -> V = { t ->
    f(this(t))
}

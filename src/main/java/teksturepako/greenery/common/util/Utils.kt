package teksturepako.greenery.common.util


fun <T> List<T>.isNotNull(index: Int): Boolean
{
    return if (index in 0..lastIndex) get(index) != null else false
}

infix fun CharSequence.inNotNull(charSequence: CharSequence?): Boolean
{
    return charSequence?.let { this in it } ?: false
}
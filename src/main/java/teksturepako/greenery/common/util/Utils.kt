package teksturepako.greenery.common.util
fun List<String?>.isNotNull(index: Int): Boolean
{
    return if (index in 0..lastIndex) get(index) != null && get(index)!!.isNotEmpty() else false
}

infix fun CharSequence.inNotNull(charSequence: CharSequence?): Boolean
{
    return charSequence?.let { this in it } ?: false
}
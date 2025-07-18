package xyz.devcmb.treeTumblers

object Constants {
    const val VERSION: String = "0.1-00006d"
    const val DEV_MODE: Boolean = true
    val AUTOMATIC_REPLICATION_INTERVAL: Long = Times.Minute.Ticks * 5L

    data class Times(val Ticks: Int) {
        companion object {
            val Second = Times(20)
            val Minute = Times(20 * 60)
            val Hour = Times(20 * 60 * 60)
        }
    }
}
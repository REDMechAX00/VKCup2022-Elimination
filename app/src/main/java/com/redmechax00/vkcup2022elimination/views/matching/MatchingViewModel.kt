package com.redmechax00.vkcup2022elimination.views.matching

data class MatchingViewModel(
    val matchingData: List<Pair<String, String>>,
) {

    override fun equals(other: Any?): Boolean {
        other as MatchingViewModel
        for (i in matchingData.indices) {
            if (matchingData[i].first != other.matchingData[i].first) return false
            if (matchingData[i].second != other.matchingData[i].second) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = 0
        matchingData.forEach {
            result = 31 * result + it.first.hashCode()
            result = 31 * result + it.second.hashCode()
        }
        return result
    }
}
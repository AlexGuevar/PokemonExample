package com.killacorp.pokemonexample.model.offline

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class CustomizedModel(
    var name : String = "",
    var data : ByteArray
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomizedModel

        if (name != other.name) return false
        if (!data.contentEquals(other.data)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + (id ?: 0)
        return result
    }
}

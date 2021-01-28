package pl.portfolio.kotlinapp.newsapp.db

import androidx.room.TypeConverter
import pl.portfolio.kotlinapp.newsapp.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}
package com.github.andersonarc.sqlgen.builder.select

import com.github.andersonarc.sqlgen.Database

abstract class BaseSelectBuilder<T> {
    var fields = ArrayList<ValueBuilder>()

    fun build(): String {
        return fields.toString()
    }

    fun execute(db: Database): List<T> {
        TODO("Not yet implemented")
    }

    fun execute(dbUrl: String) = execute(Database(dbUrl))
}


open class SelectBuilderClass<T>(val clazz: Class<T>)
    : BaseSelectBuilder<T>() {

    inline fun <reified A> field(block: (FieldBuilder<T>) -> ValueBuilder): SelectBuilderTuple1<T, Tuple1<A>> {
        fields.add(block(FieldBuilder(clazz)))
        return SelectBuilderTuple1(clazz, fields)
    }
}


class SelectBuilderTuple1<T, A>(val clazz: Class<T>, fields: ArrayList<ValueBuilder>)
    : BaseSelectBuilder<Tuple1<A>>() {

    init {
        this.fields = fields
    }

    inline fun <reified B> field(block: (FieldBuilder<T>) -> ValueBuilder): SelectBuilderTuple2<T, A, B> {
        fields.add(block(FieldBuilder(clazz)))
        return SelectBuilderTuple2(clazz, fields)
    }
}


class SelectBuilderTuple2<T, A, B>(val clazz: Class<T>, fields: ArrayList<ValueBuilder>)
    : BaseSelectBuilder<Tuple2<A, B>>() {

    init {
        this.fields = fields
    }

    inline fun <reified C> field(block: (FieldBuilder<T>) -> ValueBuilder): SelectBuilderTuple3<T, A, B, C> {
        fields.add(block(FieldBuilder(clazz)))
        return SelectBuilderTuple3(clazz, fields)
    }
}


class SelectBuilderTuple3<T, A, B, C>(val clazz: Class<T>, fields: ArrayList<ValueBuilder>)
    : BaseSelectBuilder<Tuple3<A, B, C>>() {

    init {
        this.fields = fields
    }

    inline fun <reified D> field(block: (FieldBuilder<T>) -> ValueBuilder): SelectBuilderTuple4<T, A, B, C, D> {
        fields.add(block(FieldBuilder(clazz)))
        return SelectBuilderTuple4(clazz, fields)
    }
}


class SelectBuilderTuple4<T, A, B, C, D>(val clazz: Class<T>, fields: ArrayList<ValueBuilder>)
    : BaseSelectBuilder<Tuple4<A, B, C, D>>() {

    init {
        this.fields = fields
    }

    inline fun <reified E> field(block: (FieldBuilder<T>) -> ValueBuilder): SelectBuilderTuple5<T, A, B, C, D, E> {
        fields.add(block(FieldBuilder(clazz)))
        return SelectBuilderTuple5(clazz, fields)
    }
}


class SelectBuilderTuple5<T, A, B, C, D, E>(val clazz: Class<T>, fields: ArrayList<ValueBuilder>)
    : BaseSelectBuilder<Tuple5<A, B, C, D, E>>() {

    init {
        this.fields = fields
    }
}


inline fun <reified T> select() = SelectBuilderClass(T::class.java)
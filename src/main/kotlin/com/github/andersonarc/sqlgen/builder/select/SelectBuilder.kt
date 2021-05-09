package com.github.andersonarc.sqlgen.builder.select

open class SelectBuilder<T>(val clazz: Class<T>) {
    protected val fields = ArrayList<ValueBuilder>()

    fun build(): String {
        return fields.toString()
    }

    open fun <A> field(block: (FieldBuilder<T>) -> ValueBuilder): SelectBuilder<*> {
        fields.add(block(FieldBuilder(clazz)))
        return SelectBuilderTuple1<A>()
    }
}

class Tuple1<A>(var field1: A)

class SelectBuilderTuple1<A> : SelectBuilder<Tuple1<*>>(Tuple1::class.java) {
    override fun <B> field(block: (FieldBuilder<Tuple1<*>>) -> ValueBuilder): SelectBuilderTuple2<A, B> {
        fields.add(block(FieldBuilder(clazz)))
        return SelectBuilderTuple2()
    }
}

class Tuple2<A, B>(var field1: A, var field2: B)

class SelectBuilderTuple2<A, B> : SelectBuilder<Tuple2<*, *>>(Tuple2::class.java) {
    override fun <C> field(block: (FieldBuilder<Tuple2<*, *>>) -> ValueBuilder): SelectBuilder<*> {
        TODO()
    }
}

inline fun <reified T> select() = SelectBuilder(T::class.java)
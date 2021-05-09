package com.github.andersonarc.sqlgen.builder.select

data class Tuple1<A>(
    var f1: A
)

data class Tuple2<A, B>(
    var f1: A,
    var f2: B,
)

data class Tuple3<A, B, C>(
    var f1: A,
    var f2: B,
    var f3: C,
)

data class Tuple4<A, B, C, D>(
    var f1: A,
    var f2: B,
    var f3: C,
    var f4: D,
)

data class Tuple5<A, B, C, D, E>(
    var f1: A,
    var f2: B,
    var f3: C,
    var f4: D,
    var f5: E,
)
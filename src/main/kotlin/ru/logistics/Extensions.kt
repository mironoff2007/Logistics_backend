package ru.logistics

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

inline fun Expression<Boolean>.andIf(
    cond: Boolean,
    op: SqlExpressionBuilder.() -> Op<Boolean>
): Op<Boolean> = if (cond) and(Op.build(op)) else and { Op.TRUE }
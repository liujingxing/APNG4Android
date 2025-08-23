package com.github.pengfeizhou.apng

import java.nio.ByteBuffer
import kotlin.experimental.and

/**
 * User: ljx
 * Date: 2023/8/20
 * Time: 22:17
 */
fun ByteBuffer.skip(skip: Int): ByteBuffer {
    position(position() + skip)
    return this
}

fun ByteBuffer.bytes(size: Int): ByteArray {
    val bytes = ByteArray(size)
    get(bytes)
    return bytes
}

fun ByteBuffer.uInt8(): Short {
    return get().toShort() and 0xff
}

fun ByteBuffer.uInt16(): Int {
    return short.toInt() and 0xffff
}

fun ByteBuffer.uInt32(): Long {
    return int.toLong() and 0xffffffff
}
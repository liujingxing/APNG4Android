package com.github.pengfeizhou.apng

import com.bumptech.glide.load.resource.bitmap.VideoDecoder.byteBuffer
import java.io.ByteArrayOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * User: ljx
 * Date: 2025/8/22
 * Time: 10:08
 */
class Webp(buffer: ByteBuffer) {
    private val chunks = mutableListOf<WebpChunk>()
    var fileSize:Int

    init {
        val headerRIFF = String(buffer.bytes(4))
        fileSize = buffer.int
        val headerWEBP = String(buffer.bytes(4))
        while (buffer.hasRemaining()) {
            val chunkType = String(buffer.bytes(4))
            val chunkSize = buffer.int
            val bytes = buffer.bytes(chunkSize)
            chunks.add(WebpChunk(chunkType, chunkSize, bytes))
        }
    }

    fun insertChunk(chunkType: String, bytes: ByteArray) {
        chunks.add(1, WebpChunk(chunkType, bytes.size, bytes))
        fileSize += bytes.size + 8
    }

    fun toBytes(): ByteArray {
        val bos = ByteArrayOutputStream()
        val byteBuffer = ByteBuffer.allocate(12)
            .order(ByteOrder.LITTLE_ENDIAN)
        byteBuffer.put("RIFF".toByteArray())
        byteBuffer.putInt(fileSize)
        byteBuffer.put("WEBP".toByteArray())
        bos.writeBytes(byteBuffer.array())
        chunks.forEach { chunk->
            val byteBuffer = ByteBuffer.allocate(8)
                .order(ByteOrder.LITTLE_ENDIAN)
            byteBuffer.put(chunk.chunkType.toByteArray())
            byteBuffer.putInt(chunk.chunkSize)
            bos.writeBytes(byteBuffer.array())
            bos.writeBytes(chunk.bytes)
        }
        return bos.toByteArray()
    }
}

class WebpChunk(
    val chunkType: String,
    val chunkSize: Int,
    val bytes: ByteArray,


)
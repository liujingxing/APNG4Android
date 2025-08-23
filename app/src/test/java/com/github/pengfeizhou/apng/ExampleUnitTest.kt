package com.github.pengfeizhou.apng

import com.github.penfeizhou.animation.io.StreamReader
import com.github.penfeizhou.animation.webp.decode.WebPParser
import com.github.penfeizhou.animation.webp.io.WebPReader
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(IOException::class)
    fun webpParse() {
        val file = File("webp/dian_new.webp")
        val streamReader = StreamReader(FileInputStream(file))
        val webPReader = WebPReader(streamReader)
        val chunks = WebPParser.parse(webPReader)
        println("chunks.size=" + chunks.size)
    }


//    @Test
//    fun ninePatchChunk(){
//        //[1, 2, 2, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 74, 0, 0, 0, 75, 0, 0, 0, 35, 0, 0, 0, 36, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0]
//        val ninePatchChunk = NinePatchChunk(150, 72)
//            .addXCenteredRegion(1)
//            .addYCenteredRegion(1)
//            .buildChunk()
//        println(ninePatchChunk)
//    }
    @Test
    fun webpPase(){
        val file = File("webp/dian.webp")
        val readBytes = file.readBytes()
//        val buffer = ByteBuffer.wrap(readBytes)
//            .order(ByteOrder.LITTLE_ENDIAN)
//        val webp = Webp(buffer)

        val ninePatchChunk = NinePatchChunk(150, 72)
            .addXCenteredRegion(1)
            .addYCenteredRegion(1)
            .setPadding(15, 5, 40, 5)
            .buildChunk()


        val len = ninePatchChunk.size + 8
        val buffer = ByteBuffer.allocate(len)
            .order(ByteOrder.LITTLE_ENDIAN)
        buffer.put("NPTC".toByteArray())
        buffer.putInt(ninePatchChunk.size)
        buffer.put(ninePatchChunk)
        val newBytes = readBytes.insertAt(30, buffer.array())
        val buffer1 = ByteBuffer.allocate(4)
            .order(ByteOrder.LITTLE_ENDIAN)
        buffer1.putInt(newBytes.size-8)
        val array = buffer1.array()
        newBytes[4] = array[0]
        newBytes[5] = array[1]
        newBytes[6] = array[2]
        newBytes[7] = array[3]

//        webp.insertChunk("NPTC", ninePatchChunk)
//        val toBytes = webp.toBytes()
        val newFile = File("webp/dian_new.webp")
        newFile.outputStream().write(newBytes)
//        val buffer1 = ByteBuffer.wrap(toBytes)
//            .order(ByteOrder.LITTLE_ENDIAN)
//        val webp1 = Webp(buffer1)
//        println("Webp=${webp}")
    }

    fun ByteArray.insertAt(pos: Int, toInsert: ByteArray): ByteArray {
        require(pos in 0..this.size) { "pos out of range" }

        val result = ByteArray(this.size + toInsert.size)

        // 前半部分
        System.arraycopy(this, 0, result, 0, pos)
        // 插入部分
        System.arraycopy(toInsert, 0, result, pos, toInsert.size)
        // 后半部分
        System.arraycopy(this, pos, result, pos + toInsert.size, this.size - pos)

        return result
    }
}
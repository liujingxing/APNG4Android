package com.github.pengfeizhou.apng

import android.R.attr.padding
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.NinePatch
import android.graphics.drawable.NinePatchDrawable
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * User: ljx
 * Date: 2025/8/21
 * Time: 10:50
 */
class NinePatchChunk(val width: Int, val height: Int) {

    private var paddingLeft: Int = 0
    private var paddingTop: Int = 0
    private var paddingRight: Int = 0
    private var paddingBottom: Int = 0
    private val xRegions = ArrayList<Int>()
    private val yRegions = ArrayList<Int>()

    fun addXRegionPoint(x1: Int, x2: Int): NinePatchChunk {
        xRegions.add(x1)
        xRegions.add(x2)
        return this
    }

    fun addXRegion(x: Int, width: Int): NinePatchChunk {
        return addXRegionPoint(x, x + width)
    }

    fun addXRegion(xPercent: Float, widthPercent: Float): NinePatchChunk {
        val x1 = (xPercent * this.width).toInt()
        val x2 = x1 + (widthPercent * this.width).toInt()
        return addXRegionPoint(x1, x2)
    }

    fun addXRegionPoint(x1Percent: Float, x2Percent: Float): NinePatchChunk {
        val x1 = (x1Percent * this.width).toInt()
        val x2 = (x2Percent * this.width).toInt()
        return addXRegionPoint(x1, x2)
    }

    fun addXCenteredRegion(width: Int): NinePatchChunk {
        val x1 = ((this.width - width) / 2)
        return addXRegionPoint(x1, x1 + width)
    }

    fun addXCenteredRegion(widthPercent: Float): NinePatchChunk {
        val width = (widthPercent * this.width).toInt()
        val x1 = ((this.width - width) / 2)
        return addXRegionPoint(x1, x1 + width)
    }

    fun addYRegionPoint(y1: Int, y2: Int): NinePatchChunk {
        yRegions.add(y1)
        yRegions.add(y2)
        return this
    }

    fun addYRegion(y: Int, height: Int): NinePatchChunk {
        return addYRegionPoint(y, y + height)
    }

    fun addYRegion(yPercent: Float, heightPercent: Float): NinePatchChunk {
        val y1 = (yPercent * this.height).toInt()
        val y2 = y1 + (heightPercent * this.height).toInt()
        return addYRegionPoint(y1, y2)
    }

    fun addYRegionPoint(y1Percent: Float, y2Percent: Float): NinePatchChunk {
        val y1 = (y1Percent * this.height).toInt()
        val y2 = (y2Percent * this.height).toInt()
        return addYRegionPoint(y1, y2)
    }

    fun addYCenteredRegion(height: Int): NinePatchChunk {
        val y1 = ((this.height - height) / 2)
        return addYRegionPoint(y1, y1 + height)
    }

    fun addYCenteredRegion(heightPercent: Float): NinePatchChunk {
        val height = (heightPercent * this.height).toInt()
        val y1 = ((this.height - height) / 2)
        return addYRegionPoint(y1, y1 + height)
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int): NinePatchChunk {
        paddingLeft = left
        paddingRight = right
        paddingTop = top
        paddingBottom = bottom
        return this
    }

    fun buildChunk(): ByteArray {
        if (xRegions.isEmpty()) {
            xRegions.add(0)
            xRegions.add(width)
        }
        if (yRegions.isEmpty()) {
            yRegions.add(0)
            yRegions.add(height)
        }

        val noColor = 1 //0x00000001;
        val colorSize = 9 //could change, may be 2 or 6 or 15 - but has no effect on output
        val arraySize = 1 + 2 + 4 + 1 + xRegions.size + yRegions.size + colorSize
        val byteBuffer = ByteBuffer.allocate(arraySize * 4).order(ByteOrder.nativeOrder())
        byteBuffer.put(1.toByte()) //was translated
        byteBuffer.put(xRegions.size.toByte()) //divisions x
        byteBuffer.put(yRegions.size.toByte()) //divisions y
        byteBuffer.put(colorSize.toByte()) //color size

        //skip
        byteBuffer.putInt(0)
        byteBuffer.putInt(0)

        //padding -- always 0 -- left right top bottom
        byteBuffer.putInt(paddingLeft)
        byteBuffer.putInt(paddingRight)
        byteBuffer.putInt(paddingTop)
        byteBuffer.putInt(paddingBottom)

        //skip
        byteBuffer.putInt(0)

        xRegions.forEach { byteBuffer.putInt(it) }// regions left right left right ...
        yRegions.forEach { byteBuffer.putInt(it) }// regions top bottom top bottom ...

        (0..<colorSize).forEach { _ ->
            byteBuffer.putInt(noColor)
        }
        return byteBuffer.array()
    }

    fun buildNinePatch(bitmap: Bitmap, srcName: String? = null): NinePatch {
        val chunk = buildChunk()
        return NinePatch(bitmap, chunk, srcName)
    }

    fun buildNinePatchDrawable(
        resources: Resources?,
        bitmap: Bitmap,
        srcName: String? = null,
    ): NinePatchDrawable {
        val chunk = buildChunk()
        val padding = android.graphics.Rect(paddingLeft, paddingTop,paddingRight, paddingBottom)
        return NinePatchDrawable(resources, bitmap, chunk, padding, srcName)
    }

}
package com.liferay.scalapress.media

import java.awt.image.BufferedImage
import java.awt.Color
import org.apache.commons.io.FilenameUtils
import com.liferay.scalapress.enums.MimeType
import javax.ws.rs.core.MediaType

object ImageTools {

    def contentType(filename: String) = {
        val ext = FilenameUtils.getExtension(filename).toLowerCase
        MimeType.values.find(_.name.toLowerCase == ext) match {
            case None => MediaType.WILDCARD
            case Some(enum) => enum.contentType
        }
    }

    val BG_COLOR = Color.WHITE
    val SCALING_METHOD = java.awt.Image.SCALE_AREA_AVERAGING

    /**
     * Scales the given image to fit the target dimensions while keeping the current aspect ratio.
     */
    def fit(source: java.awt.Image, size: (Int, Int)): BufferedImage = {
        val fitted = dimensionsToFit(size, (source.getWidth(null), source.getHeight(null)))
        val scaled: java.awt.Image = source.getScaledInstance(fitted._1, fitted._2, SCALING_METHOD)
        val offset = ((size._1 - fitted._1) / 2, (size._2 - fitted._2) / 2)
        _draw(size, offset, scaled)
    }

    def _draw(size: (Int, Int), offset: (Int, Int), image: java.awt.Image) = {
        val target = new BufferedImage(size._1, size._2, BufferedImage.TYPE_INT_RGB)
        val g = target.createGraphics
        g.setColor(BG_COLOR)
        g.fillRect(0, 0, size._1, size._2)
        g.drawImage(image, offset._1, offset._2, null)
        target
    }

    /**
     * Resizes the given image into the new target dimensions.
     */
    def resize(source: java.awt.Image, target: (Int, Int)): BufferedImage = {
        val scaled = source.getScaledInstance(target._1, target._2, SCALING_METHOD)
        _draw(target, (0, 0), scaled)
    }

    def ratio(width: Int, height: Int): Double = width / height.toDouble

    /**
     * Returns width and height that allow the given source width, height to fit inside the target width, height
     * without losing aspect ratio
     */
    def dimensionsToFit(target: (Int, Int), source: (Int, Int)): (Int, Int) = {

        // if target width/height is zero then we have no preference for that, so set it to the original value,
        // since it cannot be any larger
        val maxWidth = if (target._1 == 0) source._1 else target._1
        val maxHeight = if (target._2 == 0) source._2 else target._2

        val wscale = maxWidth / source._1.toDouble
        val hscale = maxHeight / source._2.toDouble

        if (wscale < hscale)
            ((source._1 * wscale).toInt, (source._2 * wscale).toInt)
        else
            ((source._1 * hscale).toInt, (source._2 * hscale).toInt)
    }
}
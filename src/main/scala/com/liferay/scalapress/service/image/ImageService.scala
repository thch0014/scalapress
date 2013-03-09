package com.liferay.scalapress.service.image

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.awt.Image
import java.awt.Color
import com.liferay.scalapress.service.asset.AssetStore

@Service
class ImageService {

    @Autowired
    var provider: AssetStore = _

    //    def path(img: Img) = provider.link(img.getFilename)

    /**
     * resize the given image into the new size exactly.
     */
    def resize(source: Image, w: Int, h: Int): BufferedImage = {

        // create new buffered image to hold the resized image
        val target = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

        // get graphics for target image
        val g = target.createGraphics

        // fill background with bg colour (defaults to white)
        g.setColor(Color.white)
        g.fillRect(0, 0, w, h)

        // resize the original
        val scaled = source.getScaledInstance(w, h, java.awt.Image.SCALE_AREA_AVERAGING)

        g.drawImage(scaled, 0, 0, null)

        target
    }

    //    /**
    //     * Returns an AWT image obj for the full size image represented by this.
    //     */
    //    def awt(img: Img) = ImageIO.read(provider.get(img.getFilename))
    //
    //    def getImageTag(img: Img): ImageTag = getImageTag(img.getWidth, img.getHeight, img)
    //
    //    def getImageTag(maxWidth: Int, maxHeight: Int, img: Img): ImageTag = {
    //        val d = getDimensionsToFit(maxWidth, maxHeight, img.getWidth, img.getHeight)
    //        new ImageTag(provider.getImageUrl(img.getFilename), 0, d(0), d(1))
    //    }
    //
    //    def getThumbnailTag(maxWidth: Int, maxHeight: Int, img: Img) = {
    //        val d = getDimensionsToFit(maxWidth, maxHeight, img.getWidth, img.getHeight)
    //        new ImageTag(provider.getImageUrl(img.getFilename), 0, d(0), d(1))
    //    }
    //
    //    def getDimensionsToFit(targetWidth: Int, targetHeight: Int, image: Img): Array[Int] = {
    //        getDimensionsToFit(targetWidth, targetHeight, image.getWidth, image.getHeight)
    //    }

    private[image] def ratio(width: Int, height: Int): Double = width / height.toDouble

    /**
     * Returns the dimensions that will enable the source image to fit inside the target bounding box
     */
    def getDimensionsToFit(targetWidth: Int, targetHeight: Int, sourceWidth: Int, sourceHeight: Int): Array[Int] = {

        // if target width is zero then we don't care how wide it is, so use
        // the src width
        val maxWidth = if (targetWidth == 0) sourceWidth else targetWidth

        // if target height is zero then we don't care how high the image
        // is, so use the src height
        val maxHeight = if (targetHeight == 0) sourceHeight else targetHeight

        val wscale = maxWidth / sourceWidth.toDouble
        val hscale = maxHeight / sourceHeight.toDouble

        if (wscale < hscale)
            Array((sourceWidth * wscale).toInt, (sourceHeight * wscale).toInt)
        else
            Array((sourceWidth * hscale).toInt, (sourceHeight * hscale).toInt)
    }
}
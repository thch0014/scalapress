package com.liferay.scalapress.section.gbase

import java.io.File
import com.liferay.scalapress.Logging
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
class GoogleBaseGenerator extends Logging {

    private final val COLON_SEPARATOR: String = ":"
    private final val COMMA_SEPARATOR: String = ","

    private def buildHeaders = {
        Array("brand"
            , "color"
            , "condition"
            , "description"
            , "expiration_date"
            , "id"
            , "image_link"
            , "additional_image_link"
            , "gtin"
            , "link"
            , "capacity"
            , "mpn"
            , "price"
            , "product_type"
            , "google_product_category"
            , "availability"
            , "shipping"
            , "shipping_weight"
            , "size"
            , "title"
            , "online_only")
    }

    def buildRow(obj: Obj) = {

        val price = obj.sellPriceDecimal
        val size = AttributeFuncs.attributeValue(obj, "mpn")

        Array("brand"
            , "color"
            , "condition"
            , "description"
            , "expiration_date"
            , "id"
            , "image_link"
            , "additional_image_link"
            , "gtin"
            , "link"
            , "capacity"
            , "mpn"
            , "price"
            , "product_type"
            , "google_product_category"
            , "availability"
            , "shipping"
            , "shipping_weight"
            , "size"
            , "title"
            , "online_only")
    }

    def run: File = {

        val file = File.createTempFile("gbase", "csv")
        file.deleteOnExit()

        //        val csv = new CsvWriter(new FileWriter(file, true), ',')
        //        logger.debug("Opened CSV for writing")
        //
        //        val h = buildHeaders
        //        logger.debug("Writing headers [{}]", h)
        //        csv.writeRecord(h)

        val obj = new Obj
        val row = buildRow(obj)

        //        val searcher: ItemSearcher = new ItemSearcher(context)
        //        searcher.setItemType(getItemType)
        //        searcher.setStatus("Live")
        //        searcher.setVisibleOnly(true)
        //        searcher.setLimit(getPreviewCount)
        //        if (ItemModule.IgnoreItemsInFeed.enabled(context, getItemType)) {
        //            searcher.setExcludeFromGoogleFeed(true)
        //        }
        //        val stockModule: Boolean = ItemModule.Ordering.enabled(context, getItemType) && Module.Shopping.enabled
        //        import scala.collection.JavaConversions._
        //        for (item <- searcher) {
        //            val price: Money = item.getSellPrice
        //            if (price == null || price.isZero) {
        //                continue //todo: continue is not supported
        //            }
        //            val row: Array[String] = new Array[String](fields.size)
        //            row(0) = item.getAttributeValue(getBrandAttribute)
        //            row(1) = null
        //            row(2) = "new"
        //            var content: String = item.getContentStripped(10000)
        //            if (content != null) {
        //                content = content.replace("\t", "").replace("\n", "").replace("\r", "")
        //            }
        //            if (content == null) {
        //                content = item.getName.replaceAll("\t", "")
        //            }
        //            row(3) = content
        //            row(4) = null
        //            row(5) = item.getIdString
        //            val imgs: List[Img] = item.getApprovedImages
        //            if (imgs == null || imgs.isEmpty) {
        //                row(6) = null
        //            }
        //            else {
        //                row(6) = "FIXME"
        //                imgs.remove(0)
        //            }
        //            if (imgs == null || imgs.isEmpty) {
        //                row(7) = null
        //            }
        //            else {
        //                val sb: StringBuilder = new StringBuilder
        //                if (!imgs.isEmpty) {
        //                    import scala.collection.JavaConversions._
        //                    for (img <- imgs) {
        //                        sb.append("FIXME")
        //                        if (imgs.get(imgs.size - 1) ne img) {
        //                            sb.append(COMMA_SEPARATOR)
        //                        }
        //                    }
        //                    row(7) = sb.toString
        //                }
        //            }
        //            row(8) = if (item.getAttributeValue(getEanAttribute) != null) item
        //              .getAttributeValue(getEanAttribute)
        //            else item.getAttributeValue(getIsbnAttribute)
        //            row(9) = item.getUrl
        //            row(10) = null
        //            row(11) = item.getAttributeValue(getMpcAttribute)
        //            row(12) = item.getSellPriceInc.toEditString + " " + Currency.getDefault(context).getName
        //            var product_type: String = null
        //            if (item.hasCategory) {
        //                if (!(item.getCategoryName == "CSV FAULT")) {
        //                    product_type = getProductType + " - " + item.getCategoryName
        //                }
        //                else {
        //                    product_type = getProductType
        //                }
        //            }
        //            else {
        //                product_type = getProductType
        //            }
        //            row(13) = product_type
        //            row(14) = getProductType
        //            if (stockModule) {
        //                if (item.getOurStock > 0) {
        //                    row(15) = "in stock"
        //                }
        //                else if (item.isBackorders) {
        //                    row(15) = "available for order"
        //                }
        //                else {
        //                    row(15) = "out of stock"
        //                }
        //            }
        //            else {
        //                row(15) = "in stock"
        //            }
        //            val weight: String = item.getAttributeValue(getShipWeightAttribute)
        //            if (weight == null) {
        //                val sb: StringBuilder = new StringBuilder
        //                val deliveryOptions: List[DeliveryOption] = DeliveryOption.get
        //                if (!deliveryOptions.isEmpty) {
        //                    {
        //                        var i: Int = 0
        //                        while (i < deliveryOptions.size) {
        //                            {
        //                                val option: DeliveryOption = deliveryOptions.get(i)
        //                                sb
        //                                  .append(if (!option.getCountries.isEmpty) option
        //                                  .getCountries
        //                                  .first
        //                                  .getIsoAlpha2
        //                                else Country.UK.getIsoAlpha2)
        //                                sb.append(COLON_SEPARATOR)
        //                                sb.append(COLON_SEPARATOR)
        //                                sb.append(COLON_SEPARATOR)
        //                                sb.append(option.getFlatCharge)
        //                                if (i < deliveryOptions.size - 1) {
        //                                    sb.append(COMMA_SEPARATOR)
        //                                }
        //                            }
        //                            ({
        //                                i += 1;
        //                                i - 1
        //                            })
        //                        }
        //                    }
        //                }
        //                row(16) = sb.toString
        //                row(17) = null
        //            }
        //            else {
        //                row(16) = null
        //                row(17) = weight + " g"
        //            }
        //            row(18) = null
        //            row(19) = item.getName.replaceAll("\t", "")
        //            row(20) = if ((ShoppingPlugin.DispatchType.Collection == ShoppingPlugin
        //              .getInstance
        //              .getDispatchType)) "n"
        //            else "y"
        //            CollectionsUtil.replaceNulls(row, "")
        //            HtmlHelper.replaseSymbolsWithUTF(row)
        //            saver.next(row)
        //        }
        //        saver.end
        //        super.exportFtp(file)
        //        file.delete
        //        return new FeedResult(csvman)

        file
    }

}

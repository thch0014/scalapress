package com.cloudray.scalapress.theme.tag

import com.cloudray.scalapress.plugin.ecommerce.tags._
import com.cloudray.scalapress.item.tag._
import com.cloudray.scalapress.folder.tag.{PrimaryFoldersTag, FolderTag}
import com.cloudray.scalapress.settings.tag.SiteGoogleMapTag
import com.cloudray.scalapress.plugin.ecommerce.shopping.tags.DeliveryOptionsTag

/** @author Stephen Samuel */
object EcreatorCompatibleTagMappings {

  val mappings: Map[String, ScalapressTag] = Map(
    "item" -> ItemTag,
    "object" -> ItemTag,
    "categories_primary" -> PrimaryFoldersTag,
    "folders_primary" -> PrimaryFoldersTag,
    "scripts" -> new ScriptTag,
    "price" -> ItemSellPriceTag,
    "pricing" -> ItemSellPriceTag,
    "pricing_sell" -> ItemSellPriceTag,
    "pricing_rrp" -> RrpTag,
    "price_rrp" -> RrpTag,
    "pricing_rrp_discount" -> RrpDiscountTag,
    "price_rrp_discount" -> RrpDiscountTag,
    "folder" -> FolderTag,
    "category" -> FolderTag,
    "image" -> new ImagesTag,
    "image_url" -> ImageUrlTag,
    "images_url" -> ImageUrlTag,
    "ordering_buy" -> new AddToBasketTag,
    "delivery_selector" -> new DeliveryOptionsTag,
    "url" -> LinkTag,
    "item_url" -> LinkTag,
    "attribute" -> new AttributeValueTag(),
    "multimap" -> new SiteGoogleMapTag)
}

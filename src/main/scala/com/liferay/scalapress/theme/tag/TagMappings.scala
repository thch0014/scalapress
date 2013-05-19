package com.liferay.scalapress.theme.tag

import com.liferay.scalapress.widgets.WidgetsTag
import com.liferay.scalapress.plugin.ecommerce.tags._
import com.liferay.scalapress.obj.tag._
import com.liferay.scalapress.folder.tag.{PrimaryFoldersTag, BreadcrumbsTag, FolderTag}
import com.liferay.scalapress.settings.tag.SiteGoogleMapTag

/** @author Stephen Samuel */
object TagMappings {

    val mappings: Map[String, ScalapressTag] = Map("item" -> ObjectTag, "object" -> ObjectTag,
        "categories_primary" -> PrimaryFoldersTag,
        "folders_primary" -> PrimaryFoldersTag,
        "scripts" -> new ScriptTag,
        "price" -> ObjectSellPriceTag,
        "pricing" -> ObjectSellPriceTag,
        "pricing_sell" -> ObjectSellPriceTag,
        "pricing_rrp" -> RrpTag,
        "price_rrp" -> RrpTag,
        "pricing_rrp_discount" -> RrpDiscountTag,
        "price_rrp_discount" -> RrpDiscountTag,
        "folder" -> FolderTag,
        "category" -> FolderTag,
        "image" -> ImagesTag,
        "images" -> ImagesTag,
        "thumbnail" -> ImagesTag,
        "image_url" -> ImageUrlTag,
        "images_url" -> ImageUrlTag,
        "ordering_buy" -> new AddToBasketTag,
        "delivery_selector" -> new DeliveryOptionsTag,
        "url" -> LinkTag,
        "item_url" -> LinkTag,
        "attribute" -> new AttributeValueTag(),
        "image_browser" -> new ColorboxTag(),
        "boxes" -> new WidgetsTag(),
        "widget" -> new WidgetsTag(),
        "breadcrumbs" -> BreadcrumbsTag,
        "breadcrumb" -> BreadcrumbsTag,
        "multimap" -> new SiteGoogleMapTag)
}

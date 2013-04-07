package com.liferay.scalapress.theme.tag

import com.liferay.scalapress.plugin.profile.tag.{LoginEmailTag, LoginPasswordTag}
import com.liferay.scalapress.widgets.WidgetsTag
import com.liferay.scalapress.plugin.ecommerce.tags._
import com.liferay.scalapress.search.tag.AttributeSearchTag
import com.liferay.scalapress.obj.tag._
import com.liferay.scalapress.folder.tag.{PrimaryFoldersTag, BreadcrumbsTag, FolderTag}
import com.liferay.scalapress.media.AssetTag
import com.liferay.scalapress.user.UserStatusTag

/** @author Stephen Samuel */
object TagMappings {

    val mappings: Map[String, ScalapressTag] = Map("item" -> ObjectTag, "object" -> ObjectTag,
        "categories_primary" -> PrimaryFoldersTag,
        "folders_primary" -> PrimaryFoldersTag,
        "pricing_sell" -> ObjectSellPriceTag,
        "asset" -> AssetTag,
        "scripts" -> new ScriptTag,
        "price" -> ObjectSellPriceTag,
        "pricing" -> ObjectSellPriceTag,
        "pricing_rrp" -> RrpTag,
        "price_rrp" -> RrpTag,
        "pricing_rrp_discount" -> RrpDiscountTag,
        "price_rrp_discount" -> RrpDiscountTag,
        "folder" -> FolderTag, "category" -> FolderTag,
        "image" -> ImagesTag,
        "images" -> ImagesTag,
        "thumbnail" -> ImagesTag,
        "image_url" -> ImageUrlTag,
        "images_url" -> ImageUrlTag,
        "ordering_buy" -> AddToBasketTag,
        "addtobasket" -> AddToBasketTag,
        "basket_lines" -> BasketLinesTag,
        "basket_items" -> BasketLinesTag,
        "basket_line_item" -> BasketLineItemTag,
        "basket_line_qty" -> BasketLineQtyTag,
        "basket_line_price" -> BasketLinePriceTag,
        "basket_line_total" -> BasketLineTotalTag,
        "basket_line_stock" -> BasketLineStockTag,
        "basket_line_remove" -> BasketRemoveItemTag,
        "basket_lines_count" -> BasketLineCountTag,
        "basket_lines_total" -> BasketLinesTotalTag,
        "basket_items_total" -> BasketLinesTotalTag,
        "invoice_account_number" -> InvoiceAccountNumberTag,
        "invoice_account_email" -> InvoiceAccountEmailTag,
        "invoice_account_name" -> InvoiceAccountNameTag,
        "invoice_billing_address" -> InvoiceBillingAddressTag,
        "invoice_delivery_address" -> InvoiceDeliveryAddressTag,
        "invoice_line_desc" -> InvoiceLineDescTag,
        "invoice_total" -> InvoiceTotalTag,
        "invoice_number" -> InvoiceNumberTag,
        "invoice_lines" -> InvoiceLinesTag,
        "invoice_attribute_value" -> InvoiceAttributeValueTag,
        "invoice_line_qty" -> InvoiceLineQtyTag,
        "invoice_line_price" -> InvoiceLinePriceTag,
        "invoice_line_total" -> InvoiceLineTotalTag,
        "invoice_lines_total" -> InvoiceLinesTotalTag,
        "invoice_delivery_charge" -> InvoiceDeliveryChargeTag,
        "invoice_delivery_desc" -> InvoiceDeliveryDetailsTag,
        "delivery_options" -> DeliveryOptionsTag,
        "delivery_selector" -> DeliveryOptionsTag,
        "url" -> LinkTag,
        "item_url" -> LinkTag,
        "attribute_table" -> AttributeTableTag,
        "attributes_table" -> AttributeTableTag,
        "attribute" -> new AttributeValueTag(),
        "search_attribute" -> AttributeSearchTag,
        "image_browser" -> new ColorboxTag(),
        "user_status" -> UserStatusTag,
        "account_status" -> UserStatusTag,
        "member_status" -> UserStatusTag,
        "boxes" -> new WidgetsTag(),
        "widget" -> new WidgetsTag(),
        "breadcrumbs" -> BreadcrumbsTag,
        "breadcrumb" -> BreadcrumbsTag,
        "login_username" -> LoginEmailTag,
        "login_email" -> LoginEmailTag,
        "login_password" -> LoginPasswordTag)
}

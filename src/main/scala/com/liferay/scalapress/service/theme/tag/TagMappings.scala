package com.liferay.scalapress.service.theme.tag

import folder.{FolderTag, PrimaryFoldersTag, BreadcrumbsTag}
import general._
import obj._
import user.{LogoutTag, UserStatusTag}
import com.liferay.scalapress.plugin.search.tag.{AttributeSearchTag, QuickSearchTag}
import com.liferay.scalapress.plugin.account.tag.{UsernameTag, LoginEmailTag, LoginPasswordTag, RegisterTag}
import com.liferay.scalapress.widgets.WidgetsTag
import com.liferay.scalapress.plugin.ecommerce.tags._
import com.liferay.scalapress.plugin.attachment.{AttachmentNameTag, AttachmentLinkTag}

/** @author Stephen Samuel */
object TagMappings {

    val mappings: Map[String, ScalapressTag] = Map("item" -> ObjectTag, "object" -> ObjectTag,
        "categories_primary" -> PrimaryFoldersTag,
        "folders_primary" -> PrimaryFoldersTag,
        "summary" -> SummaryTag,
        "content" -> ContentTag,
        "pricing_sell" -> ObjectSellPriceTag,
        "asset" -> AssetTag,
        "price" -> ObjectSellPriceTag,
        "pricing" -> ObjectSellPriceTag,
        "pricing_rrp" -> RrpTag,
        "price_rrp" -> RrpTag,
        "pricing_rrp_discount" -> RrpDiscountTag,
        "price_rrp_discount" -> RrpDiscountTag,
        "stock" -> ObjectStockTag,
        "availability" -> ObjectAvailabilityTag,
        "folder" -> FolderTag, "category" -> FolderTag,
        "image" -> ImagesTag,
        "images" -> ImagesTag,
        "thumbnail" -> ImagesTag,
        "image_url" -> ImageUrlTag,
        "images_url" -> ImageUrlTag,
        "attachments" -> AttachmentsTag,
        "ordering_buy" -> AddToBasketTag,
        "addtobasket" -> AddToBasketTag,
        "register" -> RegisterTag,
        "basket" -> BasketLinkTag,
        "basket_total" -> BasketTotalTag,
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
        "basket_delivery_charge" -> BasketDeliveryChargeTag,
        "basket_delivery_desc" -> BasketDeliveryDescTag,
        "invoice_date" -> InvoiceDateTag,
        "invoice_account_number" -> InvoiceAccountNumberTag,
        "invoice_account_email" -> InvoiceAccountEmailTag,
        "invoice_account_name" -> InvoiceAccountNameTag,
        "invoice_billing_address" -> InvoiceBillingAddressTag,
        "invoice_delivery_address" -> InvoiceDeliveryAddressTag,
        "invoice_line_desc" -> InvoiceLineDescTag,
        "invoice_total" -> InvoiceTotalTag,
        "invoice_lines" -> InvoiceLinesTag,
        "invoice_line_qty" -> InvoiceLineQtyTag,
        "invoice_line_price" -> InvoiceLinePriceTag,
        "invoice_line_total" -> InvoiceLineTotalTag,
        "invoice_lines_total" -> InvoiceLinesTotalTag,
        "invoice_delivery_charge" -> InvoiceDeliveryChargeTag,
        "invoice_delivery_desc" -> InvoiceDeliveryDetailsTag,
        "checkout" -> CheckoutTag,
        "delivery_options" -> DeliveryOptionsTag,
        "delivery_selector" -> DeliveryOptionsTag,
        "url" -> LinkTag,
        "item_url" -> LinkTag,
        "quicksearch" -> QuickSearchTag,
        "search_keywords" -> QuickSearchTag,
        "meta_title" -> TitleTagTag,
        "meta_keywords" -> KeywordsTagTag,
        "meta_description" -> DescriptionTagTag,
        "date" -> DateTag,
        "home" -> HomeTag,
        "logout" -> LogoutTag,
        "attachment_link" -> AttachmentLinkTag,
        "attachment_name" -> AttachmentNameTag,
        "attribute_value" -> AttributeValueTag,
        "attribute_table" -> AttributeTableTag,
        "attributes_table" -> AttributeTableTag,
        "attribute_name" -> AttributeNameTag,
        "attribute_section" -> AttributeSectionTag,
        "attribute" -> AttributeValueTag,
        "search_attribute" -> AttributeSearchTag,
        "site_name" -> SiteNameTag,
        "title" -> TitleTag,
        "css" -> CssTag,
        "script" -> ScriptTag,
        "scripts" -> ScriptTag,
        "colorbox" -> ColorboxTag,
        "image_browser" -> ColorboxTag,
        "user_status" -> UserStatusTag,
        "account_status" -> UserStatusTag,
        "account_name" -> UsernameTag,
        "account_username" -> UsernameTag,
        "member_status" -> UserStatusTag,
        "boxes" -> WidgetsTag,
        "widgets" -> WidgetsTag,
        "widget" -> WidgetsTag,
        "date_created" -> DateCreatedTag,
        "order_status" -> OrderStatusTag,
        "breadcrumbs" -> BreadcrumbsTag,
        "breadcrumb" -> BreadcrumbsTag,
        "login_username" -> LoginEmailTag,
        "login_email" -> LoginEmailTag,
        "login_password" -> LoginPasswordTag,
        "comp_name" -> SiteNameTag,
        "comp_vat" -> SiteVatTag,
        "comp_number" -> SiteCompanyNumberTag,
        "comp_email" -> SiteEmailTag,
        "comp_postcode" -> SitePostcodeTag,
        "comp_address_label" -> SiteAddressLabelTag,
        "comp_address" -> SiteAddressTag,
        "comp_country" -> SiteCountryTag,
        "comp_telephone" -> SitePhoneTag,
        "copyright" -> CopyrightTag,
        "multimap" -> SiteGoogleMapTag, "comp_postcode_gmap" -> SiteGoogleMapTag)
}

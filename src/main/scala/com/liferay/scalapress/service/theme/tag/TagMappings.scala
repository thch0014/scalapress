package com.liferay.scalapress.service.theme.tag

import folder.{FolderTag, PrimaryFoldersTag, BreadcrumbsTag}
import general._
import obj._
import theme.WidgetsTag
import user.UserStatusTag
import com.liferay.scalapress.section.attachment.{AttachmentNameTag, AttachmentLinkTag}
import com.liferay.scalapress.plugin.search.tag.{AttributeSearchTag, QuickSearchTag}
import com.liferay.scalapress.plugin.ecommerce.{BasketTotalTag, BasketLinkTag, AddToBasketTag}
import com.liferay.scalapress.plugin.account.tag.RegisterTag

/** @author Stephen Samuel */
object TagMappings {

    val mappings: Map[String, ScalapressTag] = Map("item" -> ObjectTag,
        "categories_primary" -> PrimaryFoldersTag,
        "folders_primary" -> PrimaryFoldersTag,
        "summary" -> SummaryTag,
        "pricing_sell" -> ObjectSellPriceTag,
        "price" -> ObjectSellPriceTag,
        "pricing" -> ObjectSellPriceTag,
        "pricing_rrp" -> RrpTag,
        "pricing_rrp_discount" -> RrpDiscountTag,
        "stock" -> ObjectStockTag,
        "folder" -> FolderTag, "category" -> FolderTag,
        "image" -> ImagesTag,
        "images" -> ImagesTag,
        "thumbnail" -> ImagesTag,
        "image_url" -> ImageUrlTag,
        "images_url" -> ImageUrlTag,
        "ordering_buy" -> AddToBasketTag,
        "addtobasket" -> AddToBasketTag,
        "ordering_qty" -> AddToBasketTag,
        "register" -> RegisterTag,
        "basketqty" -> AddToBasketTag,
        "basket" -> BasketLinkTag,
        "basket_total" -> BasketTotalTag,
        "url" -> LinkTag,
        "item_url" -> LinkTag,
        "quicksearch" -> QuickSearchTag,
        "search_keywords" -> QuickSearchTag,
        "meta_title" -> TitleTagTag,
        "meta_keywords" -> KeywordsTagTag,
        "meta_description" -> DescriptionTagTag,
        "date" -> DateTag,
        "home" -> HomeTag,
        "attachment_link" -> AttachmentLinkTag,
        "attachment_name" -> AttachmentNameTag,
        "attribute_value" -> AttributeValueTag,
        "attribute_name" -> AttributeNameTag,
        "attribute" -> AttributeNameTag,
        "search_attribute" -> AttributeSearchTag,
        "site_name" -> SiteNameTag,
        "title" -> TitleTag,
        "css" -> CssTag,
        "script" -> ScriptTag,
        "scripts" -> ScriptTag,
        "colorbox" -> ColorboxTag,
        "user_status" -> UserStatusTag,
        "account_status" -> UserStatusTag,
        "member_status" -> UserStatusTag,
        "boxes" -> WidgetsTag,
        "widgets" -> WidgetsTag,
        "widget" -> WidgetsTag,
        "date_created" -> DateCreatedTag,
        "breadcrumbs" -> BreadcrumbsTag,
        "breadcrumb" -> BreadcrumbsTag,
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

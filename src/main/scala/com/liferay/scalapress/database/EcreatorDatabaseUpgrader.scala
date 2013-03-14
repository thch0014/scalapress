package com.liferay.scalapress.database

import org.springframework.beans.factory.annotation.Autowired
import javax.sql.DataSource
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component
import com.liferay.scalapress.Logging

/** @author Stephen Samuel */
@Component
class EcreatorDatabaseUpgrader extends Logging {

    @Autowired var dataSource: DataSource = _

    def execute(sql: String) {
        val conn = dataSource.getConnection
        val stmt = conn.createStatement()
        try {
            stmt.executeUpdate(sql)
        } catch {
            case e: Exception => logger.warn(e.getMessage)
        } finally {
            stmt.close()
            conn.close()
        }
    }

    @PostConstruct
    def update() {

        val conn = dataSource.getConnection

        execute("ALTER TABLE items_accessories MODIFY item bigint(10) null")
        execute("UPDATE items_accessories SET item=null WHERE item=0")
        execute("ALTER TABLE items_accessories MODIFY accessory bigint(10) null")
        execute("UPDATE items_accessories SET accessory=null WHERE accessory=0")

        execute("ALTER TABLE search_forms MODIFY itemtype bigint(10) null")
        execute("UPDATE search_forms SET itemtype=null WHERE itemtype=0")

        execute("ALTER TABLE plugins_sagepay DROP enabled")
        execute("ALTER TABLE plugins_paypal_standard DROP enabled")

        execute("ALTER TABLE boxes_highlighted_items MODIFY markup bigint(10) null")
        execute("UPDATE boxes_highlighted_items SET markup=null WHERE markup=0")

        execute("ALTER TABLE addresses MODIFY account bigint(10) null")
        execute("UPDATE addresses SET addresses=null WHERE addresses=0")

        execute("ALTER TABLE addresses MODIFY owner bigint(10) null")
        execute("UPDATE addresses SET owner=null WHERE owner=0")
        execute("ALTER TABLE addresses MODIFY country varchar(100) null")

        var k = 1
        Array("boxes_custom", "categories_boxes", "boxes_search", "boxes_highlighted_items").foreach(table => {
            execute("UPDATE " + table + " SET id=id+10000*" + k + " WHERE id<10000")
            k = k + 1
        })

        Array("boxes_custom", "categories_boxes", "boxes_images", "boxes_search", "boxes_highlighted_items")
          .foreach(table => {
            try {
                execute("UPDATE " + table + " SET restricted=1, `where`=0 where `where`=1")
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        })

        try {
            val rs = conn.prepareStatement("SELECT id FROM items WHERE featured=1").executeQuery
            while (rs.next) {
                try {
                    val id = rs.getLong(1)
                    val stmt = conn.prepareStatement("UPDATE items set labels='Featured' where id=" + id)
                    stmt.execute()
                    stmt.close()
                } catch {
                    case e: Exception => logger.warn(e.getMessage)
                }
            }
        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }

        for (col <- Array("item", "category")) {
            execute("alter TABLE forms_submissions MODIFY " + col + " bigint(10) null")
            execute("UPDATE forms_submissions SET " + col + "=null where " + col + "=0")
        }

        execute("ALTER TABLE categories_items modify item bigint(10) null")
        execute("UPDATE categories set parent=null where parent=0")


        execute("ALTER TABLE blocks_galleries MODIFY gallery bigint(10) null")
        execute("UPDATE blocks_galleries set gallery=null WHERE gallery=0")

        execute("ALTER TABLE categories modify parent bigint(10) null")
        execute("UPDATE categories set parent=null WHERE parent=0")

        <!-- update image assocations -->
        for (col <- Array("imageBox", "item", "gallery", "category")) {
            execute("alter table images MODIFY " + col + " bigint(10) null")
            execute("update images set " + col + "=null WHERE " + col + "=0")
        }

        k = 1
        Array("blocks_items",
            "blocks_content",
            "blocks_subcategories",
            "blocks_galleries",
            "blocks_forms",
            "blocks_attachments",
            "blocks_highlighted_items",
            "blocks_search")
          .foreach(block => {

            execute("UPDATE " + block + " SET id=id+10000*" + k + " WHERE id<10000")
            k = k + 1

            Array("ownerItemtype", "ownercategory", "ownerItem").foreach(col => {
                execute("alter table " + block + " modify " + col + " bigint(10) null")
                execute("update " + block + " set " + col + "=null WHERE " + col + "=0")
            })

            execute("ALTER TABLE " + block + " drop cssid")
            execute("ALTER TABLE " + block + " drop cssclass")
            execute("ALTER TABLE " + block + " drop displaytomembers")
        })


        execute("ALTER TABLE blocks_highlighted_items MODIFY markup bigint(10) null")
        execute("UPDATE blocks_highlighted_items set markup=null WHERE markup=0")

        execute("ALTER TABLE blocks_subcategories MODIFY markup bigint(10) null")
        execute("UPDATE blocks_subcategories set markup=null WHERE markup=0")

        execute("ALTER TABLE search_forms MODIFY markup bigint(10) null")
        execute("UPDATE search_forms set markup=null WHERE markup=0")

        execute("ALTER TABLE search_forms_fields MODIFY searchForm bigint(10) null")
        execute("UPDATE search_forms_fields SET searchForm=null WHERE searchForm=0")
        execute("ALTER TABLE search_forms_fields MODIFY attribute bigint(10) null")
        execute("UPDATE search_forms_fields SET attribute=null WHERE attribute=0")

        execute("ALTER TABLE blocks_items MODIFY listmarkup bigint(10) null")
        execute("UPDATE blocks_items SET listmarkup=null WHERE listmarkup=0")

        // update markup fields to text
        execute("ALTER TABLE markup MODIFY `body` text null")
        execute("ALTER TABLE markup MODIFY `start` text null")
        execute("ALTER TABLE markup MODIFY `end` text null")
        execute("ALTER TABLE markup MODIFY `between` text null")

        execute("ALTER TABLE templates MODIFY header text null")
        execute("ALTER TABLE templates MODIFY footer text null")

        //        execute("UPDATE users SET passwordhash='09b792e75d96dbcb3d49f5af313e9fa1', active=1 " +
        //        "WHERE passwordhash IS NULL AND active=1")

        // attributes
        execute("ALTER TABLE attributes_values MODIFY item bigint(10) null")
        execute("UPDATE attributes_values SET item=null WHERE item=0")

        execute("ALTER TABLE attributes MODIFY itemtype bigint(10) null")
        execute("UPDATE attributes SET itemtype=null WHERE itemtype=0")
        execute("ALTER TABLE attributes_values MODIFY search bigint(10) null")
        execute("UPDATE attributes_values SET search=null WHERE search=0")

        execute("ALTER TABLE orders MODIFY deliveryaddress bigint(10) null")
        execute("UPDATE orders SET deliveryaddress=null WHERE deliveryaddress=0")
        execute("ALTER TABLE orders MODIFY account bigint(10) null")
        execute("UPDATE orders SET account=null WHERE account=0")



        // copy the searchbox from forms to the box
        try {
            val rs = conn.prepareStatement("SELECT id, searchbox from search_forms WHERE searchbox>0").executeQuery()
            while (rs.next) {
                try {

                    val id = rs.getLong(1)
                    val searchbox = rs.getLong(2)
                    val stmt = conn.prepareStatement("UPDATE boxes_search SET searchForm=? WHERE id=?")
                    stmt.setLong(1, id)
                    stmt.setLong(2, searchbox)
                    stmt.execute()
                    stmt.close()

                    val stmt2 = conn.prepareStatement("UPDATE search_forms SET searchbox=0")
                    stmt2.execute()
                    stmt2.close()

                } catch {
                    case e: Exception => logger.warn(e.getMessage)
                }
            }
        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }

        try {
            val rs = conn.prepareStatement("SELECT id, owner from searches").executeQuery()
            while (rs.next) {
                try {

                    val searchId = rs.getLong(1)
                    val owner = rs.getString(2)

                    Option(owner).filter(_.trim.length > 0).foreach(owner => {

                        if (owner.startsWith("blocks_highlighted_items@")) {
                            val blockId = owner.replace("blocks_highlighted_items@", "").toLong + 70000
                            execute("UPDATE blocks_highlighted_items SET search=" + searchId + " WHERE id=" + blockId)

                        } else if (owner.startsWith("boxes_highlighted_items@")) {
                            val boxId = owner.replace("boxes_highlighted_items@", "").toLong + 40000
                            execute("UPDATE boxes_highlighted_items SET search=" + searchId + " WHERE id=" + boxId)

                        } else if (owner.startsWith("categories@")) {
                            val categoryId = owner.replace("categories@", "").toLong
                            execute(
                                "INSERT INTO blocks_highlighted_items (position, visible, ownerCategory, search, name) values (4545, 1, " + categoryId + "," + searchId + ", 'Saved search results')")
                            execute("UPDATE searches set owner='__categories@" + categoryId + "' WHERE id=" + searchId)
                        }
                    })

                } catch {
                    case e: Exception => logger.warn(e.getMessage)
                }
            }
        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }

        // make search blocks from the search_forms
        try {
            val rs = conn.prepareStatement("SELECT id, searchblock from search_forms").executeQuery()
            while (rs.next) {

                val id = rs.getLong(1)
                val searchblock = rs.getLong(2)

                execute("UPDATE blocks_search SET search_form=" + id + " WHERE id=" + searchblock)
                execute("UPDATE blocks_search SET search_form=" + id + " WHERE id=" + (searchblock + 80000))

            }
        }
        catch {
            case e: Exception => logger.warn(e.getMessage)
        }

        execute("ALTER TABLE items MODIFY account bigint(10) null")
        execute("UPDATE items SET account=null WHERE account=0")

        execute("ALTER TABLE categories_boxes MODIFY root bigint(10) null")
        execute("UPDATE categories_boxes SET root=null WHERE root=0")

        execute("DELETE FROM forms_fields WHERE type='Separator'")

        execute("ALTER TABLE searches MODIFY searchcategory varchar(255) null")
        execute("UPDATE searches SET searchcategory=null WHERE searchcategory=0")
        execute("ALTER TABLE searches MODIFY itemType bigint(10) null")
        execute("UPDATE searches SET itemType=null WHERE itemType=0")

        for (column <- Array("disableemails",
            "pendingimagecount",
            "imagecount",
            "imageupdatetimestamp",
            "imageaddedtime", "administrator", "crm", "salesperson", "restrictions", "password")) {
            try {
                execute("ALTER TABLE users DROP " + column)
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        }


        for (column <- Array("noresultsforwardurl",
            "autoforward",
            "activelocation",
            "noresultstext",
            "googlemap",
            "sortattribute",
            "submitsrc",
            "resultspagetitle",
            "savable",
            "excludeaccountitemtype",
            "excludeaccount",
            "filter", "alerts", "numberofhighlightedresults", "parenttemplate", "titletag", "descriptiontag")) {
            try {
                execute("ALTER TABLE search_forms DROP " + column)
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        }



        for (column <- Array("categorynames",
            "pendingimagecount",
            "inactivemessage",
            "branch",
            "buyerfor",
            "pricebreaks",
            "awaitingmoderation",
            "suggestions",
            "shortname",
            "feedsrc",
            "agreement",
            "ourcostprice",
            "postcount",
            "searchcount",
            "lastmessageauthor",
            "endofline",
            "priceband",
            "resetcode",
            "displayname",
            "mobilephone",
            "listingpaymentdone",
            "showoptionslist",
            "bogof",
            "referrer",
            "feedcount",
            "lastmessagedate",
            "bogofitem",
            "pendingreviewscount",
            "prioritised",
            "optiongroup",
            "orderqtymultiple",
            "featured",
            "stocknotifylevel",
            "friendlyurllocked",
            "unit",
            "zebraean",
            "attachmentcount",
            "messagecount",
            "lastactive",
            "categorycached",
            "categoryname",
            "categoryids",
            "categoryfullname",
            "categorycount",
            "optioncount",
            "accessorycount",
            "contentstripped",
            "balance",
            "awaitingvalidation",
            "smscredits",
            "expirydate",
            "videocount",
            "approvedreviewscount",
            "imageaddedtime",
            "owner",
            "comments",
            "ratingaverages", "ratingaverage",
            "friendlyurl",
            "imageupdatetimestamp",
            "imagecount",
            "openrangeproductid",
            "discountapplied")) {
            try {
                execute("ALTER TABLE items DROP " + column)
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        }


        for (column <- Array("padding",
            "labelprefix",
            "searchcontrol", "width", "height", "autosubmit")) {
            try {
                execute("ALTER TABLE search_forms_fields DROP " + column)
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        }


        for (column <- Array("subscriber",
            "smsregistration",
            "booking",
            "bookingsession",
            "review",
            "promotion",
            "registrationsession",
            "order",
            "listingsession",
            "category",
            "basket")) {
            try {
                execute("ALTER TABLE attributes_values DROP " + column)
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        }



        for (column <- Array("style",
            "appletbackgroundcolor",
            "appletheadlinecolor",
            "appletsummarycolor",
            "appletsummaryfamily",
            "appletsummarysize",
            "appletheadlinefamily",
            "appletheadlinesize",
            "appletheadlinehighlightcolor",
            "appletsummaryhighlightcolor",
            "appletlinktocategory",
            "simpleeditor",
            "scrollerspeed", "scrollerheight")) {
            try {
                execute("ALTER TABLE boxes_highlighted_items DROP " + column)
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        }

        for (column <- Array("hidediscountinfo",
            "contentlinkingkeywords",
            "discount",
            "amazonbtg",
            "shopzillacategory",
            "membergrouppricing",
            "pricebreakvalues",
            "itemsperpage",
            "liveitemscount",
            "kelkoocategory",
            "attributecount",
            "searchengineurllocked",
            "supermanlock",
            "friendlyurllocked",
            "permissions",
            "primaryparent",
            "messages",
            "currency",
            "filecount",
            "contentlinkingkeyword",
            "basketforwardcategory",
            "childcount",
            "background",
            "autocontentlinking",
            "listitemmarkup", "subcategorylistitemmarkup", "subcategorymarkup",
            "forwardwho",
            "advanced", "parentcount", "resultstop", "resultsbottom",
            "manualitemsorder", "itemsort", "content", "comments", "template",
            "includeSubcategoryItems", "noitemsmessage", "summary", "fullname", "css",
            "simpleeditor",
            "forum",
            "hash",
            "rendertags",
            "suggestions",
            "includesubcategoryitems",
            "usemanualsort",
            "imageupdatetimestamp",
            "imagecount",
            "imageaddedtime",
            "pendingimagecount",
            "videocount",
            "restrictionforwardurl",
            "wikiaccess")) {

            execute("ALTER TABLE categories DROP " + column)

        }

        //        for (table <- Array("videos",
        //            "videos_block",
        //            "visitors",
        //            "visitors_sessions",
        //            "visitors_views",
        //            "system_messages",
        //            "tickers_settings",
        //            "tickers",
        //            "sms_blocks",
        //            "sms_bulletins",
        //            "sms_registrations",
        //            "stats_hits_items",
        //            "stats_submissions_per_page",
        //            "report_vouchers",
        //            "reports_browsers_counter",
        //            "reports_downloads_monthly",
        //            "reports_downloads_weekly",
        //            "reports_hits",
        //            "reports_hits_copy",
        //            "reports_hits_daily",
        //            "reports_hits_monthly",
        //            "reports_hits_pages_monthly",
        //            "reports_ips",
        //            "reports_ips_listing",
        //            "reports_ips_page",
        //            "reports_referrals",
        //            "reports_referrals_counter",
        //            "reports_search_phrases",
        //            "reports_submissions",
        //            "reports_submissions_members_monthly",
        //            "reports_submissions_monthly",
        //            "reports_submissions_pages_monthly",
        //            "reports_terms",
        //            "reports_terms_counter",
        //            "reports_visitors_daily",
        //            "reports_visitors_monthly",
        //            "meetups_rsvp_blocks",
        //            "members_buddies",
        //            "members_debug_logins",
        //            "members_messages",
        //            "members_nudges",
        //            "members_sessions",
        //            "meetups",
        //            "meetups_blocks_itemrsvps",
        //            "meetups_blocks_myrsvps",
        //            "mailboxes",
        //            "logs_entries",
        //            "licenses",
        //            "jobsheets_jobs",
        //            "languages_translations",
        //            "items_bots_expiry",
        //            "ipoints_settings",
        //            "interaction_views_count",
        //            "interaction_views",
        //            "interactions",
        //            "forums",
        //            "forums_posts",
        //            "forums_topics",
        //            "suppliers",
        //            "tellfriend_settings",
        //            "settings_modules",
        //            "relations_module",
        //            "relations_groups",
        //            "relations",
        //            "po_lines",
        //            "maps_clickable_flash_ukireland",
        //            "locations",
        //            "delivery_extras",
        //            "delivery_details",
        //            "blocks_relations",
        //            "payments_processor_hsbcapi",
        //            "payments_processor_payflowpro_api",
        //            "payments_processor_payflowpro_partner",
        //            "payments_processors_epdqmpi",
        //            "payments_processors_protxdirect",
        //            "payments_processors_secpayxml",
        //            "blocks_polls",
        //            "basketline",
        //            "basket",
        //            "addresses_fields",
        //            "geoip",
        //            "gmap_sku",
        //            "banners",
        //            "banners_joins",
        //            "auctions_bids",
        //            "auctions",
        //            "affiliates_settings",
        //            "affiliates_conversions",
        //            "affiliates_clicks",
        //            "boxes_sms",
        //            "boxes_tagcloud",
        //            "boxes_members_views",
        //            "boxes_member_messages",
        //            "calculators",
        //            "calculators_fields",
        //            "calculators_fields_options",
        //            "captchas",
        //            "categories_referrers",
        //            "dashboard_shortcuts",
        //            "downloads",
        //            "feeds_vue",
        //            "feeds_vip",
        //            "feeds_sykes",
        //            "feeds_shopzilla",
        //            "feeds_savemoneycars",
        //            "feeds_rightmove_export",
        //            "feeds_rightmove",
        //            "feeds_propertyfeed",
        //            "feeds_pricerunner",
        //            "feeds_pricegrabber",
        //            "feeds_pdo_news",
        //            "feeds_open_range_images",
        //            "feeds_open_range",
        //            "feeds_midwich",
        //            "feeds_micro_p",
        //            "feeds_kyero",
        //            "feeds_keloo",
        //            "feeds_edirectory",
        //            "feeds_c2000",
        //            "feeds_artful",
        //            "settings_newsletter",
        //            "settings_delivery",
        //            "boxes_holiday_reminders",
        //            "boxes_highlighted_comments",
        //            "boxes_googlewebsearch",
        //            "boxes_forums_search",
        //            "prices_pricing",
        //            "prices_settings",
        //            "media_modules",
        //            "boxes_login",
        //            "boxes_polls",
        //            "boxes_newsletter",
        //            "boxes_forum_posts_latest",
        //            "boxes_buddies",
        //            "boxes_banners",
        //            "adsense",
        //            "attachments_groups",
        //            "images_settings",
        //            "images_types",
        //            "feeds_kingfield",
        //            "feeds_dealtime ",
        //            "feeds_camdram",
        //            "sitemap_categories",
        //            "feeds_autopart",
        //            "blocks_holiday_reminders",
        //            "blocks_tagcloud",
        //            "blocks_wizards",
        //            "blocks_profile_sms",
        //            "boxes_active_location",
        //            "boxes_callback",
        //            "boxes_favourites",
        //            "forms_submissions_restriction_module",
        //            "forms_submissions_report_module",
        //            "items_stock_audit",
        //            "items_prices_watchers",
        //            "items_prices_requests",
        //            "items_prices_overrides",
        //            "items_prices_cost",
        //            "items_favourites_settings",
        //            "items_favourites_groups_attribute",
        //            "items_favourites_groups",
        //            "items_favourites",
        //            "listings_hit_monthly",
        //            "listings_hits",
        //            "listings_hits_daily",
        //            "profiles_module",
        //            "settings_comments",
        //            "settings_facebook_login",
        //            "settings_forums",
        //            "settings_sitemap",
        //            "settings_sms",
        //            "wizards_steps", "wizards",
        //            "users_sessions",
        //            "userplane_recorder_recordings",
        //            "templates_settings",
        //            "wizards",
        //            "templates_session",
        //            "boxes_newsfeeds",
        //            "boxes_rss_export",
        //            "boxes_wizards",
        //            "payments_requests",
        //            "payments_sessions",
        //            "searcher_comments",
        //            "search_groups",
        //            "searches_alerts",
        //            "searches_labels",
        //            "prices_bands",
        //            "prices",
        //            "payments_adjustments",
        //            "newsletter_optins",
        //            "newsfeeds_items",
        //            "newsfeeds",
        //            "items_views",
        //            "settings_language",
        //            "newsletters_autosignups",
        //            "listings_session_payment",
        //            "listings_session",
        //            " newsletters_templates",
        //            " newsletters_blocks",
        //            "forms_fields_google",
        //            "blocks_banners_rotation",
        //            "blocks_banners",
        //            "access_reminder_bots",
        //            "forms_submissions_ip_stats",
        //            "settings_stats",
        //            "sessions",
        //            "reviews_delegates",
        //            "reviews_module",
        //            "redirects",
        //            "feeds_ecreator_import",
        //            "newsletters_examples",
        //            "feeds_category_mappings",
        //            "feeds_custom",
        //            "feeds_custom_export",
        //            "feeds_custom_fields",
        //            "feeds_ecreator_export",
        //            "feeds_ecreator_import",
        //            "feeds_generic_categories", "boxes_filters", "boxes_language", "boxes_upload", "blocks_ibex",
        //            "blocks_jobsheets_submit", "blocks_maps_europe_countries", "blocks_maps_gb_regions", "blocks_newsfeeds",
        //            "currencies",
        //            "settings_scripts",
        //            "settings_polls",
        //            "settings_captions",
        //            "ratings_types",
        //            "promotions_items",
        //            "ratings_module",
        //            "promotions", "promotions_categories", "promotions_freeitems",
        //            "prices_module",
        //            "blocks_maps_clickable",
        //            "blocks_availabilitychart",
        //            "blocks_calculators")) {
        //            execute("DROP TABLE " + table)
        //        }

        conn.close()
    }
}

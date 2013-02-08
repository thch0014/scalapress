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

    @PostConstruct
    def update() {

        val conn = dataSource.getConnection

        var k = 1
        Array("boxes_custom", "categories_boxes").foreach(table => {

            conn.prepareStatement("UPDATE " + table + " SET id=id+10000*" + k + " WHERE id<10000").execute()
            k = k + 1
        })

        Array("boxes_custom", "categories_boxes", "boxes_images").foreach(table => {
            try {
                conn
                  .prepareStatement("UPDATE " + table + " SET restricted=1, `where`=0 where `where`=1")
                  .execute()
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        })

        val rs = conn.prepareStatement("SELECT id FROM items WHERE featured=1").executeQuery
        while (rs.next) {
            try {
                val id = rs.getLong(1)
                val stmt = conn.prepareStatement("INSERT into object_labels (object_id, labels) values (?,?)")
                stmt.setLong(1, id)
                stmt.setString(2, "Featured")
                stmt.execute()
                stmt.close()
            } catch {
                case e: Exception => logger.warn(e.getMessage)
            }
        }

        for (col <- Array("item", "category")) {
            conn.prepareStatement("alter TABLE forms_submissions MODIFY " + col + " bigint(10) null").execute()
            conn.prepareStatement("UPDATE forms_submissions SET " + col + "=null where " + col + "=0").execute()
        }

        conn.prepareStatement("ALTER TABLE categories_items modify item bigint(10) null").execute()
        conn.prepareStatement("UPDATE categories set parent=null where parent=0").execute()


        conn.prepareStatement("ALTER TABLE blocks_galleries MODIFY gallery bigint(10) null").execute()
        conn.prepareStatement("UPDATE blocks_galleries set gallery=null WHERE gallery=0").execute()

        conn.prepareStatement("ALTER TABLE categories modify parent bigint(10) null").execute()
        conn.prepareStatement("UPDATE categories set parent=null WHERE parent=0").execute()

        <!-- update image assocations -->
        for (col <- Array("imageBox", "item", "gallery", "category")) {
            conn.prepareStatement("alter table images MODIFY " + col + " bigint(10) null").execute()
            conn.prepareStatement("update images set " + col + "=null WHERE " + col + "=0").execute()
        }

        k = 1
        Array("blocks_items",
            "blocks_content",
            "blocks_subcategories",
            "blocks_galleries",
            "blocks_forms",
            "blocks_attachments")
          .foreach(block => {

            conn.prepareStatement("UPDATE " + block + " SET id=id+10000*" + k + " WHERE id<10000").execute()
            k = k + 1

            Array("ownerItemtype", "ownercategory", "ownerItem").foreach(col => {
                conn.prepareStatement("alter table " + block + " modify " + col + " bigint(10) null").execute()
                conn.prepareStatement("update " + block + " set " + col + "=null WHERE " + col + "=0").execute()
            })
        })

        conn.prepareStatement("alter table blocks_subcategories modify markup bigint(10) null").execute()
        conn.prepareStatement("update blocks_subcategories set markup=null WHERE markup=0").execute()

        conn.prepareStatement("alter table blocks_items modify listmarkup bigint(10) null").execute()
        conn.prepareStatement("update blocks_items set listmarkup=null WHERE listmarkup=0").execute()

        // update markup fields to text
        conn.prepareStatement("ALTER TABLE markup MODIFY `body` text null").execute()
        conn.prepareStatement("ALTER TABLE markup MODIFY `start` text null").execute()
        conn.prepareStatement("ALTER TABLE markup MODIFY `end` text null").execute()
        conn.prepareStatement("ALTER TABLE markup MODIFY `between` text null").execute()

        conn.prepareStatement("ALTER TABLE templates MODIFY header text null").execute()
        conn.prepareStatement("ALTER TABLE templates MODIFY footer text null").execute()

        conn
          .prepareStatement(
            "update users set passwordhash='09b792e75d96dbcb3d49f5af313e9fa1', active=1 where passwordhash is null")
          .execute()

        // attributes
        conn.prepareStatement("ALTER TABLE attributes_values MODIFY item bigint(10) null").execute()
        conn.prepareStatement("UPDATE attributes_values SET item=null WHERE item=0").execute()

        conn.prepareStatement("ALTER TABLE attributes MODIFY itemtype bigint(10) null").execute()
        conn.prepareStatement("UPDATE attributes SET itemtype=null WHERE itemtype=0").execute()

        conn.prepareStatement("ALTER TABLE orders MODIFY deliveryaddress bigint(10) null").execute()
        conn.prepareStatement("UPDATE orders SET deliveryaddress=null WHERE deliveryaddress=0").execute()
        conn.prepareStatement("ALTER TABLE orders MODIFY account bigint(10) null").execute()
        conn.prepareStatement("UPDATE orders SET account=null WHERE account=0").execute()

        conn.close()
    }
}

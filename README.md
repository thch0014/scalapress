scalapress
==========

scala based publishing platform

[![Build Status](https://travis-ci.org/sksamuel/scalapress.png)](https://travis-ci.org/sksamuel/scalapress)

### Release History

##### 0.41

Removed images from the codebase
Improved galleries and introduced a new common gallery system for plugins to follow
Fixed tags in bootstrap 3
Fixes galore around the object->item conversion
Added dublin core plugin
Updated order system to include more details on list page
Refactored ecommerce plugins

##### 0.40

* Added new offline settings
* Template upload
* Migration to Boostrap 3 in backoffice
* Section and folder interceptors
* Simplepass plugin for security on folders
* Vouchers for listings
* Renamed objects to items

##### 0.39

* Added prioritized tag and prioritizedonly to image, object and attribute tags.
* Added masonry image gallery plugin
* Reworked S3 asset store for faster enumeration. Updated asset store contract.
* Added options for form fields
* Fixed memory leak with Scalate
* Moved accounts to seperate top level entities. Added migrator for this.
* Updated all plugins to use new accounts system

##### 0.38
* Updated elastic indexer to be incremental
* Closed out multiple bugs
* Upgraded elastic search to 0.90.5
* Updated thumbnail service to eagerly generate thumbnails in asset store and hold local cache
* Added uberjar deployment to pom
* Updated images to be inline instead of a join. Huge reduction in the number of DB hits.
* Updated release procedure and server spool up.
* New multisite hosting option -perm gen usage now constant at ~200mb instead of 100mb per install.
* [Plugin] added PngShrink plugin
* Added backorders to shopping plugin

##### 0.37
* Decreased perm gen usage
* Optimized db access in search results
* Updated elastic to avoid recursion on indexing
* Fixed calendar json for IE 8
* Easier to identify which site a backoffice page is for
* Fixed SBT memory issues in tests
* SP-159 fixed plugin account column lengths

##### 0.36

* Added Listing object to contain listing specific calculations
* Added listing renewal controller flow
* Added delivery_select tag
* Converted elastic to use mostly filters. Added support for ignorePast into the searches.
* Converted listing renderer to SSP
* Updated joda to 2.2 for extra cool methods

##### 0.35

Added bcc to order confirmation emails
Updated availbity tag for in/out boolean stock status
SP-164 allow image ordering on backoffice objects
SP-162 sorted variations by name in backoffice
Added types to image tags
Updated thumbnail to use bound not fix for thumnail resize
Moved various plugins to SSP
SP-155 updated nested folders tag to respect folder order
Permalink field removed from admin folder edit.vm

##### 0.34

Integrated max size plugin for uploaded images
SP-157 Updated delivery tag to include name.
Updated object controller to strip non digts from numerical attribute type
Basket line qty changed to HTML5 number input
Updated thumbnail service to use thumbnails subfolder
Changed shopping bag to basket on basket controller
Added default attribute option rendering into attribute table
Updated object exporter to only include live items
Added error message on login page.
Fixed disqus quote handling
Added JMX support
Added prioritized flag and updated searches to always place prioritized top
Added facets into search controller. Added search settings.

##### 0.33 
* Added deletion for object types.
* Fixed cloning for objects.
* Added bulk object loading to search results for speed
* Added disqus plugin
* Fixed folder ordering
* User friendly links added to listing/registration processes
* Added pagination into search controller

##### 0.32
* Added class param to attribute tables
* Made sections visible by default
* Added object importer
* Updated all tests to share a DB context
* Updated to scala 10.2
* Added more columns to object exporter
* Fixed bug with order line duplication in a set
* Fixed order email showing code snippets
* Fixed gbase cron syntax
* Fixed google cron dao methods, and added tests.
* Updated recent objects in dash to account for obj status
* Added admin email notification in checkout process

#### 0.31

* Fixed order of addresses, delivery, folder, markup and theme populators.
* Added delete button to submission page
* Updated calendar system to use date ranges
* Added emails to orders when set to completed.
* Removed pointless basket widget
* Updated object exporter to include pricing and stock
* Added TX tranaction tests
* Removed obsolte siblings section
* Refactored gallery into proper plugin
* Updated folder widget to use direct XML for rendering.
* Fixed whitespace issue in folder widget exclusions
* Added robots.txt
* Added more order builder tests
* Added tinycme tests
* Increased security tests
* Removed deprecated services in context and updated references
* Updated menus to use options. 
* Added CSV export to sales report

#### 0.30
* Migrated all menus to new pluggable menu system
* Added paypal sage and submissions menu providers
* Added pluggable menu system for plugins
* Added equally wicked "create object" modal
* Added wicked "create folder" modal
* Added excel header to exporter controller and updated tests.
* Changed eyeball link to use URLGenerator. Fixed session id null bug.
* Updated sort system to use session id as seed for random ordering
* Updated OG tags to specific height / weight
* Fixed bootbox and so submissions deletion.

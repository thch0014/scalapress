scalapress
==========

scala based publishing platform





[![Build Status](https://travis-ci.org/sksamuel/scalapress.png)](https://travis-ci.org/sksamuel/scalapress)


### Release History

##### 0.33 
* Added deletion for object types.
* Fixed cloning for objects.


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

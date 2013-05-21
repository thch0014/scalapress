package com.liferay.scalapress.plugin.gallery.gallerific

import com.liferay.scalapress.ScalapressRequest
import javax.persistence.{ElementCollection, Entity, Table}
import com.liferay.scalapress.section.Section
import scala.beans.BeanProperty
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_galleriffic_section")
class GallerifficSection extends Section {

    @ElementCollection
    @BeanProperty var images: java.util.Set[String] = new util.HashSet[String]()

    override def desc: String = "A section showing a galleriffic gallery"
    override def backoffice: String = "/backoffice/plugin/galleriffic/section/" + id
    override def render(request: ScalapressRequest): Option[String] = {

        val rows = _images.map(i => _rows(i))
        val controls = <div id="galleriffic" class="content">
            <div id="galleriffic-loading" class="loader"></div>
            <div id="galleriffic-slideshow" class="slideshow"></div>
            <div id="galleriffic-caption" class="embox"></div>
        </div>
        val thumbs = <div id="galleriffic-thumbs" class="thumbs navigation">
            <ul class="thumbs noscript">
                {rows}
            </ul>
        </div>
        Some(controls + "\n\n" + thumbs + "\n\n" + _script)
    }

    def _images: Iterable[String] = images.size match {
        case 0 => images.asScala
        case _ => Option(super.obj).map(_.images.asScala.map(_.filename)).getOrElse(Nil)
    }

    def _script = {
        """<script>

            // We only want these styles applied when javascript is enabled
			$('div.navigation').css({'width' : '300px', 'float' : 'left'});
			$('div.content').css('display', 'block');


           $(document).ready(function() {
             var gallery = $('#galleriffic-thumbs').galleriffic({
                delay: 3000, // in milliseconds
                numThumbs: 20, // The number of thumbnails to show page
                preloadAhead: 40, // Set to -1 to preload all images
                enableTopPager: false,
                enableBottomPager: true,
                maxPagesToShow: 7, // The maximum number of pages to display in either the top or bottom pager
                imageContainerSel: '#galleriffic-slideshow', // The CSS selector for the element within which the main slideshow image should be rendered
                controlsContainerSel: '', // The CSS selector for the element within which the slideshow controls should be rendered
                captionContainerSel: '#galleriffic-caption', // The CSS selector for the element within which the captions should be rendered
                loadingContainerSel: '#galleriffic-loading', // The CSS selector for the element within which should be shown when an image is loading
                renderSSControls: true, // Specifies whether the slideshow's Play and Pause links should be rendered
                renderNavControls: true, // Specifies whether the slideshow's Next and Previous links should be rendered
                playLinkText: 'Play ',
                pauseLinkText: 'Pause ',
                prevLinkText: 'Previous ',
                nextLinkText: 'Next ',
                nextPageLinkText: 'Next &rsaquo;',
                prevPageLinkText: '&lsaquo; Prev ',
                enableHistory: false, // Specifies whether the url's hash and the browser's history cache should update when the current slideshow image changes
                enableKeyboardNavigation: true, // Specifies whether keyboard navigation is enabled
                autoStart: false, // Specifies whether the slideshow should be playing or paused when the page first loads
                syncTransitions: false, // Specifies whether the out and in transitions occur simultaneously or distinctly
                defaultTransitionDuration: 1000, // If using the default transitions, specifies the duration of the transitions
                onSlideChange: undefined, // accepts a delegate like such: function(prevIndex, nextIndex) { ... }
                onTransitionOut: undefined, // accepts a delegate like such: function(slide, caption, isSync, callback) { ... }
                onTransitionIn: undefined, // accepts a delegate like such: function(slide, caption, isSync) { ... }
                onPageTransitionOut: undefined, // accepts a delegate like such: function(callback) { ... }
                onPageTransitionIn: undefined, // accepts a delegate like such: function() { ... }
                onImageAdded: undefined, // accepts a delegate like such: function(imageData, $li) { ... }
                onImageRemoved: undefined // accepts a delegate like such: function(imageData, $li) { ... }
            });
        });
        </script>"""
    }

    def _rows(filename: String) = {
        val fullsize = "/images/" + filename
        val thumbnail = "/images/" + filename + "?w=100&h=100"
        <li>
            <a class="thumb" href={fullsize}>
                <img src={thumbnail}/>
            </a>
            <div class="caption">
                To do caption
            </div>
        </li>
    }
}
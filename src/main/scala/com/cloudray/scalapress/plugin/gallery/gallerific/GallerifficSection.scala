package com.cloudray.scalapress.plugin.gallery.gallerific

import javax.persistence.{ElementCollection, Entity, Table}
import com.cloudray.scalapress.section.Section
import scala.beans.BeanProperty
import java.util
import scala.collection.JavaConverters._
import com.cloudray.scalapress.framework.ScalapressRequest

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
    rows.size match {
      case 0 => None
      case _ =>
        val controls = <div id="gallery" class="galleriffic-content">
          <div id="loading" class="galleriffic-loader"></div>
          <div id="slideshow" class="galleriffic-slideshow"></div>
          <div id="caption" class="galleriffic-embox"></div>
        </div>
        val thumbs = <div id="thumbs" class="galleriffic-navigation">
          <ul class="galleriffic-thumbs noscript">
            {rows}
          </ul>
        </div>
        Some(controls + "\n\n" + thumbs + "\n\n" + _script)
    }
  }

  def _rows(filename: String) = {
    val fullsize = "/images/" + filename + "?width=800&height=600"
    val thumbnail = "/images/" + filename + "?width=100&height=100"
    <li>
      <a class="galleriffic-thumb" href={fullsize}>
        <img src={thumbnail}/>
      </a>
    </li>
  }

  def _images: Iterable[String] = images.size match {
    case 0 => Option(item).map(_.images.asScala).getOrElse(Nil)
    case _ => images.asScala
  }

  def _script = {
    """<script>
            // We only want these styles applied when javascript is enabled
			$('div.galleriffic-navigation').css({'width' : '610px'});
			$('div.galleriffic-content').css('display', 'block');

            // Initially set opacity on thumbs and add additional styling for hover effect on thumbs
            var onMouseOutOpacity = 0.67;
            $('#thumbs ul.galleriffic-thumbs li').css('opacity', onMouseOutOpacity).hover(function () {
                   $(this).not('.selected').fadeTo('fast', 1.0);
               }, function () {
                   $(this).not('.selected').fadeTo('fast', onMouseOutOpacity);
               });

           $(document).ready(function() {
             var gallery = $('#gallery').galleriffic('#thumbs', {
                delay: 10000, // in milliseconds
                numThumbs: 50, // The number of thumbnails to show page
                preloadAhead: 10, // Set to -1 to preload all images
                enableTopPager: true,
                enableBottomPager: true,
                maxPagesToShow: 7, // The maximum number of pages to display in either the top or bottom pager
                imageContainerSel: '#slideshow', // The CSS selector for the element within which the main slideshow image should be rendered
                controlsContainerSel: '', // The CSS selector for the element within which the slideshow controls should be rendered
                captionContainerSel: '#caption', // The CSS selector for the element within which the captions should be rendered
                loadingContainerSel: '#loading', // The CSS selector for the element within which should be shown when an image is loading
                renderSSControls: true, // Specifies whether the slideshow's Play and Pause links should be rendered
                renderNavControls: true, // Specifies whether the slideshow's Next and Previous links should be rendered
                playLinkText: 'Play ',
                pauseLinkText: 'Pause ',
                prevLinkText: 'Previous ',
                nextLinkText: 'Next ',
                nextPageLinkText: 'Next',
                prevPageLinkText: 'Prev ',
                enableHistory: false, // Specifies whether the url's hash and the browser's history cache should update when the current slideshow image changes
                enableKeyboardNavigation: true, // Specifies whether keyboard navigation is enabled
                autoStart: false, // Specifies whether the slideshow should be playing or paused when the page first loads
                syncTransitions: false, // Specifies whether the out and in transitions occur simultaneously or distinctly
                defaultTransitionDuration: 1000, // If using the default transitions, specifies the duration of the transitions
                onChange: function(prevIndex, nextIndex) { $('#thumbs ul.galleriffic-thumbs').children() .eq(prevIndex).fadeTo('fast', onMouseOutOpacity).end() .eq(nextIndex).fadeTo('fast', 1.0); },
                onTransitionOut: function(callback) { $('#caption').fadeOut('fast'); $('#slideshow').fadeOut('fast', callback); }                ,
                onTransitionIn: function() { $('#slideshow, #caption').fadeIn('fast'); },
                onPageTransitionOut: function(callback) { $('#thumbs ul.galleriffic-thumbs').fadeOut('fast', callback); },
                onPageTransitionIn: function() { $('#thumbs ul.galleriffic-thumbs').fadeIn('fast'); }
            });
        });
        </script>"""
  }

}
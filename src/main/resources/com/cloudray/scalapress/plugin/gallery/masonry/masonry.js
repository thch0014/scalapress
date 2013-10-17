jQuery(window).load(function () {
    jQuery(function () {
        jQuery('.masonry-container').masonry({
            itemSelector: '.masonry-image',
            columnWidth: '.masonry-image'
        });
    });
    jQuery(".colorbox").colorbox({ rel: 'colorboxgroup' });
});

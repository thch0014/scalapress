package com.cloudray.scalapress.plugin.disqus

import com.cloudray.scalapress.section.Section
import javax.persistence.{Table, Entity}
import com.cloudray.scalapress.ScalapressRequest
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_disqus_section")
class DisqusSection extends Section {

    @BeanProperty var shortname: String = _

    override def backoffice: String = "/backoffice/plugin/disqus/section/" + id
    def desc: String = "Disqus commenting system"

    def render(request: ScalapressRequest): Option[String] = {

        Option(shortname).map(arg => {
            s"""<div id="disqus_thread"></div>
                <script type="text/javascript">
                    /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
                    var disqus_shortname = '$shortname';
                    /* * * DON'T EDIT BELOW THIS LINE * * */
                    (function() {
                        var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
                        dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
                        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
                    })();
                </script>
                <noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
                <a href="http://disqus.com" class="dsq-brlink">comments powered by <span class="logo-disqus">Disqus</span></a>
        """
        })
    }
}

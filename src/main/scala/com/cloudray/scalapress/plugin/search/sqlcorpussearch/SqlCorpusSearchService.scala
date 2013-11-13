package com.cloudray.scalapress.plugin.search.sqlcorpussearch

import com.cloudray.scalapress.search.{CorpusResult, CorpusSearchService}
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.folder.section.FolderContentSection
import com.cloudray.scalapress.framework.{UrlGenerator, ScalapressContext}

/** @author Stephen Samuel
  *
  *         An implementation of CorpusSearchService that performs raw DB queries to search for data.
  *
  **/
@Component
class SqlCorpusSearchService extends CorpusSearchService {

    @Autowired var context: ScalapressContext = _

    def search(query: String): Seq[CorpusResult] = {

        val s = new Search(classOf[FolderContentSection])
          .setMaxResults(50)
          .addFilterEqual("visible", true)
          .addFetch("folder")
          .addFilterEqual("folder.hidden", false)
        val terms = query.split(" ")
        terms.foreach(term => s.addFilterILike("content", "%" + term.trim + "%"))

        val sections = context.folderDao.exposeSearch[FolderContentSection](s)
        val singlePerFolder = sections.groupBy(_.folder).map(_._2.head).toSeq

        singlePerFolder.map(section => {
            val snippet = _snippet(section.content, terms, 200)
            CorpusResult(section.folder.name, UrlGenerator.url(section.folder), snippet)

        }).take(20)
    }

    def _snippet(content: String, terms: Seq[String], max: Int) = {
        require(!content.isEmpty)

        val stripped = content.replaceAll("<.*?>", "").replaceAll("(?s)\\s{2,}", "").trim

        // find first term and use that as the snippet offset
        var start = stripped.indexOf(terms.head)
        var end = start + terms.length

        if (start == -1)
            stripped.take(max).reverse.dropWhile(_ != ' ').reverse.trim + "..."
        else {
            while (end - start < max && (start > 0 || end < stripped.length)) {
                if (start > 0)
                    start = start - 1
                if (end < stripped.length)
                    end = end + 1
            }

            var snippet = stripped.substring(start, end).trim
            if (start > 0)
                snippet = "..." + snippet.dropWhile(_ != ' ').trim
            if (end < stripped.length)
                snippet = snippet.reverse.dropWhile(_ != ' ').reverse.trim + "..."
            snippet
        }
    }
}

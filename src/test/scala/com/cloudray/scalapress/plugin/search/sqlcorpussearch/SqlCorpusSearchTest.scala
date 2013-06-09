package com.cloudray.scalapress.plugin.search.sqlcorpussearch

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.folder.section.FolderContentSection
import com.cloudray.scalapress.{TestDatabaseContext, ScalapressContext}

/** @author Stephen Samuel */
class SqlCorpusSearchTest extends FunSuite with MockitoSugar {

    val service = new SqlCorpusSearchService
    service.context = new ScalapressContext
    service.context.folderDao = TestDatabaseContext.folderDao

    val folder1 = new Folder
    folder1.name = "footie"
    TestDatabaseContext.folderDao.save(folder1)

    val folder2 = new Folder
    folder2.name = "the a team"
    TestDatabaseContext.folderDao.save(folder2)

    val folder3 = new Folder
    folder3.name = "music"
    TestDatabaseContext.folderDao.save(folder3)

    val folder4 = new Folder
    folder4.name = "coldplay events"
    folder4.hidden = true
    TestDatabaseContext.folderDao.save(folder4)

    val section1 = new FolderContentSection
    section1.folder = folder1
    section1.visible = true
    section1.content = "boro will win the title this year"
    TestDatabaseContext.sectionDao.save(section1)

    val section2 = new FolderContentSection
    section2.folder = folder1
    section2.visible = true
    section2.content = "sunderland will lose the title this year"
    TestDatabaseContext.sectionDao.save(section2)

    val section3 = new FolderContentSection
    section3.folder = folder2
    section3.visible = true
    section3.content = "face man will be driving his corvette"
    TestDatabaseContext.sectionDao.save(section3)

    val section4 = new FolderContentSection
    section4.folder = folder2
    section4.visible = true
    section4
      .content = "ba will be driving his van fool more about ba more about ba more about ba more about ba more about ba more about ba more about ba more about ba  more about ba more about ba more about ba more about ba more about ba more about ba"
    TestDatabaseContext.sectionDao.save(section4)

    val section5 = new FolderContentSection
    section5.folder = folder3
    section5.visible = false
    section5.content = "chris martin gives his children silly names"
    TestDatabaseContext.sectionDao.save(section5)

    val section6 = new FolderContentSection
    section6.folder = folder4
    section6.visible = true
    section6.content = "coldplay live in paris"
    TestDatabaseContext.sectionDao.save(section6)

    test("search brings back a single snippet per folder") {
        val results = service.search("title")
        assert(1 === results.size)
        assert(folder1.name === results(0).title)
    }

    test("search works across multiple folders") {
        val results = service.search("will")
        assert(2 === results.size)
    }

    test("search result uses correct URL") {
        val results = service.search("will")
        assert(2 === results.size)
        assert(results.exists(_.url == "/folder-1-footie"))
        assert(results.exists(_.url == "/folder-2-the-a-team"))
    }

    test("search works for multiple search terms") {
        val results = service.search("corvette driving")
        assert(1 === results.size)
        assert(folder2.name === results(0).title)
    }

    test("search brings back truncated snippet") {
        val results = service.search("fool")
        assert(1 === results.size)
        assert(
            "ba will be driving his van fool more about ba more about ba more about ba more about ba more about ba more about ba more about ba more about bamore about ba more about ba more about ba more about ba..." === results(
                0).snippet)
    }

    test("search only finds visible content") {
        val results = service.search("martin")
        assert(0 === results.size)
    }

    test("search only finds only content from visible folders") {
        val results = service.search("coldplay")
        assert(0 === results.size)
    }

    test("snippet does not go out of bounds on prefix") {
        val content = "two pairs of eyes watching me, watching me fade away, who are you, a pair of eyes, or are you gemini?"
        val snippet = service._snippet(content, Seq("pairs"), 40)
        assert("two pairs of eyes watching me,..." === snippet)
    }

    test("snippet does not go out of bounds on suffix") {
        val content = "two pairs of eyes watching me, watching me fade away, who are you, a pair of eyes, or are you gemini?"
        val snippet = service._snippet(content, Seq("gemini", "fade"), 40)
        assert("...a pair of eyes, or are you gemini?" === snippet)
    }

    test("snippet handles max length longer than entire content") {
        val content = "two pairs of eyes watching me, watching me fade away, who are you, a pair of eyes, or are you gemini?"
        val snippet = service._snippet(content, Seq("gemini", "fade"), 55555)
        assert("two pairs of eyes watching me, watching me fade away, who are you, a pair of eyes, or are you gemini?" === snippet)
    }

    test("snippet removes all html tags") {
        val content = "two pairs of eyes <b>watching me, <a href='somelink.html'>watching me</a> fade away, who are you, a pair of eyes, or are you gemini?"
        val snippet = service._snippet(content, Seq("gemini", "fade"), 55555)
        assert("two pairs of eyes watching me, watching me fade away, who are you, a pair of eyes, or are you gemini?" === snippet)
    }
}

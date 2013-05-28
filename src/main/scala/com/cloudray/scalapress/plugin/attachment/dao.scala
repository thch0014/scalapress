package com.cloudray.scalapress.plugin.attachment

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
trait AttachmentDao extends GenericDao[Attachment, java.lang.Long] {

    def findByObj(obj: Obj): Iterable[Attachment]
    def findByFolder(f: Folder): Iterable[Attachment]
}

@Component
@Transactional
class AttachmentDaoImpl extends GenericDaoImpl[Attachment, java.lang.Long] with AttachmentDao {
    def findByObj(obj: Obj): Iterable[Attachment] = Nil
    def findByFolder(f: Folder): Iterable[Attachment] = Nil
}

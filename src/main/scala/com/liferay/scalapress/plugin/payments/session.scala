package com.liferay.scalapress.plugin.payments

import javax.persistence._
import scala.beans.BeanProperty
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel
  *
  *         Contains a mapping from a unique id to a particular type of purchase.
  *
  *         This class is used so that when a payment processor returns the system is able to lookup the type of purchase that was
  *         ongoing, so that new types of purchases can be added in dynamically.
  **/
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class PurchaseSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    def callback(tx: Transaction, context: ScalapressContext)
}

@Service
class PurchaseSessionService {

    @Autowired var context: ScalapressContext = _

    def callback(tx: Transaction) {

    }

    def callback(session: PurchaseSession) {

    }
}
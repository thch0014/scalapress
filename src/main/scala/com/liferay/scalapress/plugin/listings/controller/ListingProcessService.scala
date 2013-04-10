package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.ecommerce.domain.{OrderLine, Order}
import com.liferay.scalapress.plugin.listings.{ListingEmailService, ListingNotificationService, ListingProcess2ObjectBuilder, ListingProcess}
import com.liferay.scalapress.obj.Obj
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressContext}
import org.springframework.stereotype.Component
import com.liferay.scalapress.plugin.payments.Transaction
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Component
class ListingProcessService extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var listingNotificationService: ListingNotificationService = _
    @Autowired var listingEmailService: ListingEmailService = _

    def process(tx: Transaction, process: ListingProcess, req: HttpServletRequest) {

        val account = _account(tx)

        val obj = _object(account, process)

        _emails(obj, process)

        val order = _order(account, process, req)
        tx.order = order.id.toString
        context.transactionDao.save(tx)

        _cleanup(process)
    }

    def _object(account: Obj, process: ListingProcess) = {
        logger.debug("Creating object [account={}]", account)

        val builder = new ListingProcess2ObjectBuilder(context)
        val obj = builder.build(process)
        obj.account = account
        context.objectDao.save(obj)
        obj
    }

    def _emails(obj: Obj, process: ListingProcess) {
        logger.debug("Sending email to customer")
        listingEmailService.send(obj, context)

        logger.debug("Sending email to admin")
        listingNotificationService.notify(obj, process)
    }

    def _cleanup(process: ListingProcess) {
        logger.info("Process completed - removing from database")
        context.listingProcessDao.remove(process)
    }

    def _account(tx: Transaction) = {
        logger.info("Building account from transactions")

        val account = new Obj()
        account.status = "Disabled"
        account.name = tx.payee
        account.email = tx.payeeEmail
        account.objectType = context.typeDao.getAccount.get
        context.objectDao.save(account)
        logger.info("Acount saved [{}]", account)

        account
    }

    def _order(account: Obj, process: ListingProcess, req: HttpServletRequest) = {
        logger.debug("Creating order for the listing")

        val order = Order(req.getRemoteAddr, account)
        context.orderDao.save(order)

        val orderLine = OrderLine(process.listingPackage.name, process.listingPackage.fee)
        orderLine.order = order
        order.lines.add(orderLine)
        context.orderDao.save(order)

        order
    }
}

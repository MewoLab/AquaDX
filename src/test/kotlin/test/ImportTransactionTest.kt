package test

import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.IExportClass
import icu.samnyan.aqua.net.games.ImportController
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.SimpleTransactionStatus

private data class TransactionTestExport(
    override var gameId: String = "TEST",
    override var userData: Chu3UserData = Chu3UserData(),
) : IExportClass<Chu3UserData>

private class TransactionTestImport(
    override val userDataRepo: GenericUserDataRepo<Chu3UserData>,
) : ImportController<TransactionTestExport, Chu3UserData>(
    game = "TEST",
    gameName = "test",
    exportClass = TransactionTestExport::class,
    exportFields = emptyMap(),
    exportRepos = emptyMap(),
    artemisRenames = emptyMap(),
) {
    override fun createEmpty() = TransactionTestExport()
}

private class TrackingTransactionManager : PlatformTransactionManager {
    var active = false
    var committed = false
    var rolledBack = false

    override fun getTransaction(definition: TransactionDefinition?): TransactionStatus {
        active = true
        return SimpleTransactionStatus()
    }

    override fun commit(status: TransactionStatus) {
        committed = true
        active = false
    }

    override fun rollback(status: TransactionStatus) {
        rolledBack = true
        active = false
    }
}

class ImportTransactionTest : StringSpec({
    "failed replacement rolls back deletion and insertion together" {
        @Suppress("UNCHECKED_CAST")
        val repo = mock(GenericUserDataRepo::class.java) as GenericUserDataRepo<Chu3UserData>
        val transactionManager = TrackingTransactionManager()
        val controller = TransactionTestImport(repo).apply {
            transManager = transactionManager
        }
        val existing = Chu3UserData()
        var deleteWasTransactional = false

        doAnswer {
            deleteWasTransactional = transactionManager.active
            null
        }.`when`(repo).delete(existing)

        shouldThrow<IllegalStateException> {
            controller.replaceInTransaction(existing, 123) {
                throw IllegalStateException("simulated insert failure")
            }
        }

        deleteWasTransactional shouldBe true
        transactionManager.rolledBack shouldBe true
        transactionManager.committed shouldBe false
    }
})

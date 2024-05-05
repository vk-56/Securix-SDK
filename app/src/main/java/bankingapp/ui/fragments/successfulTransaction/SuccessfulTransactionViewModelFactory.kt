package bankingapp.ui.fragments.successfulTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bankingapp.database.CustomerDao
import bankingapp.database.TransactionRecordDao
import java.lang.IllegalArgumentException

class SuccessfulTransactionViewModelFactory(

    private val customerDatasource: CustomerDao,
    private val transactionRecordDatasource: TransactionRecordDao

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuccessfulTransactionViewModel::class.java)) {
            return SuccessfulTransactionViewModel(
                customerDatasource,
                transactionRecordDatasource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
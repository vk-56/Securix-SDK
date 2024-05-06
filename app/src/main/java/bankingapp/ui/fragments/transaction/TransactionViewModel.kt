package bankingapp.ui.fragments.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bankingapp.database.Customer
import bankingapp.database.CustomerDao
import bankingapp.securityutils.DataObfuscation
import kotlinx.coroutines.launch

class TransactionViewModel(private var databaseSource: CustomerDao) : ViewModel() {

    private var customerListId = listOf<Long>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    private var requiredIdList = ArrayList<Long>()

    private var _updatedCustomerList = MutableLiveData<List<Customer>>()
    var updatedCustomerList: LiveData<List<Customer>> = _updatedCustomerList

    fun showCustomerList() {
        viewModelScope.launch {
            val customers = databaseSource.getCustomExcept(requiredIdList)
            _updatedCustomerList.value = obfuscateCustomers(customers)
        }
    }

    private fun obfuscateCustomers(customers: List<Customer>): List<Customer> {
        return customers.map { customer ->
            customer.copy(
                customerAccountNumber = DataObfuscation.obfuscateData(customer.customerAccountNumber),
                customerEmail = DataObfuscation.shuffleDatabaseRecords(listOf(customer.customerEmail)).first(),
                customerMobileNumber = DataObfuscation.maskOutData(customer.customerMobileNumber, 3, 7),
                swiftCode = DataObfuscation.randomCharacterObfuscate(customer.swiftCode)
            )
        }
    }

    fun updateCustomerList(customer: Customer) {

        for (id in customerListId) {
            if (id != customer.id) {
                requiredIdList.add(id)
            }
        }
    }
}
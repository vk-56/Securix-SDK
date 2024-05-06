package bankingapp.ui.fragments.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bankingapp.database.Customer
import bankingapp.database.CustomerDao
import bankingapp.securityutils.DataObfuscation
import kotlinx.coroutines.launch

class CustomerViewModel(private val datasource: CustomerDao): ViewModel() {

    lateinit var customerList: LiveData<List<Customer>>

    init {
        getCustomerList()
    }

    private fun getCustomerList() {
        viewModelScope.launch {
            customerList = datasource.getAllCustomer()
        }
    }

    fun obfuscateCustomer(customer: Customer): Customer {
        val obfuscatedAccountNumber = DataObfuscation.obfuscateData(customer.customerAccountNumber)
        val obfuscatedEmail = DataObfuscation.shuffleDatabaseRecords(listOf(customer.customerEmail)).first()
        val obfuscatedMobileNumber = DataObfuscation.maskOutData(customer.customerMobileNumber, 3, 7)
        val obfuscatedSwiftCode = DataObfuscation.randomCharacterObfuscate(customer.swiftCode)

        return customer.copy(
            customerAccountNumber = obfuscatedAccountNumber,
            customerEmail = obfuscatedEmail,
            customerMobileNumber = obfuscatedMobileNumber,
            swiftCode = obfuscatedSwiftCode,
        )
    }


    fun deobfuscateCustomer(customer: Customer): Customer {
        val deobfuscatedAccountNumber = DataObfuscation.deobfuscateData(customer.customerAccountNumber)
        val deobfuscatedEmail = DataObfuscation.shuffleDatabaseRecords(listOf(customer.customerEmail)).first()
        val deobfuscatedMobileNumber = DataObfuscation.maskOutData(customer.customerMobileNumber, 3, 7)
        return customer.copy(
            customerAccountNumber = deobfuscatedAccountNumber,
            customerEmail = deobfuscatedEmail,
            customerMobileNumber = deobfuscatedMobileNumber
        )
    }
}

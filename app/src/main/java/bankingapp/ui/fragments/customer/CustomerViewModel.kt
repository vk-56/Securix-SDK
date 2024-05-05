package bankingapp.ui.fragments.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bankingapp.database.Customer
import bankingapp.database.CustomerDao
import bankingapp.securityutils.DataObfuscation
import kotlinx.coroutines.launch

class CustomerViewModel(private val datasource: CustomerDao):ViewModel() {

    lateinit var customerList: LiveData<List<Customer>>

    init {
        getCustomerList()
    }

    private fun getCustomerList() {
        viewModelScope.launch {
            customerList=datasource.getAllCustomer()
        }
    }

    fun obfuscateCustomer(customer: Customer): Customer {
        val obfuscatedAccountNumber = DataObfuscation.obfuscateData(customer.customerAccountNumber)
        return customer.copy(customerAccountNumber = obfuscatedAccountNumber)
    }

    fun deobfuscateCustomer(customer: Customer): Customer {
        val deobfuscatedAccountNumber = DataObfuscation.deobfuscateData(customer.customerAccountNumber)
        return customer.copy(customerAccountNumber = deobfuscatedAccountNumber)
    }


}
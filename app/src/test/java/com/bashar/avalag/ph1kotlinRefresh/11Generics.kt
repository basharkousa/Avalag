package com.bashar.avalag.ph1kotlinRefresh

import com.bashar.avalag.ph1kotlinRefresh.Account
import org.junit.Assert
import org.junit.Test


/*
*
* Kotlin treats T as Int.

The letter does not have to be T, but it is a common convention.

Common names include:

T → Type
R → Return type
E → Element
K → Key
V → Value
*
* */


class Storage<T> {
    private var storedValue: T? = null
    fun save(value: T) {
        storedValue = value
    }

    fun get(): T? = storedValue

    fun clear() {
        storedValue = null
    }
}

/*
* val tokenStorage = Storage<String>()

tokenStorage.save("TOKEN_123")

val token: String? = tokenStorage.get()
* */

/*
* Account storage:

val accountStorage = Storage<PaymentAccount>()

accountStorage.save(
    PaymentAccount(
        id = 101,
        balance = 2000.0,
        status = PaymentAccountStatus.ACTIVE,
    ),
)

val account: PaymentAccount? = accountStorage.get()
* */


/*
 Without generics, you might use Any:

class UnsafeStorage {

    private var value: Any? = null

    fun save(value: Any) {
        this.value = value
    }

    fun get(): Any? {
        return value
    }
}*/

/*
5. Generic functions

A function can declare its own generic type.

fun <T> printValue(value: T) {
    println(value)

    Usage:

printValue("Hello")
printValue(100)
printValue(500.0)
printValue(PaymentAccountStatus.ACTIVE)
}*/

/*
Generic sealed results

You previously created results like this:

sealed interface PaymentResult {
    data class Success(
        val remainingBalance: Double,
    ) : PaymentResult

    data class Failure(
        val message: String,
    ) : PaymentResult
}
* */

sealed interface OperationResult<out T> {
    data class Success<T>(val data: T) : OperationResult<T>
    data class Failure<T>(val message: String) : OperationResult<Nothing>
}

fun loadAccount(): OperationResult<Account> {
    val account = Account(
        1, "Saving", 1000.0, "EU", true, ""
    )
    return OperationResult.Success(account)
}

fun login(): OperationResult<String> {
    return OperationResult.Success(
        data = "TOKEN_123",
    )
}

fun getBalance(): OperationResult<Double> {
    return OperationResult.Success(
        data = 1500.0,
    )
}


interface Repository<T> {
    fun getById(id: Int): T?
    fun getAll(): List<T>
}

class AccountRepository : Repository<Account> {
    val accountList = listOf(
        Account(
            1, "Saving", 1000.0, "EU", true, ""
        ),
        Account(
            2, "Current", 1000.0, "USD", true, ""
        ),
    )

    override fun getById(id: Int): Account? {
        return accountList.find { it.id == id }
    }

    override fun getAll(): List<Account> = accountList

}

interface Mapper<From, To> {
    fun map(value: From): To
}

data class AccountDTO(
    val id: Int,
    val balance: Double,
)

data class Accounttt(
    val id: Int,
    val balance: Double,
)

class AccountMapper : Mapper<AccountDTO, Accounttt> {
    override fun map(value: AccountDTO): Accounttt {
        return Accounttt(id = value.id, balance = value.balance)
    }
}


//Lesson 11 Exercise

data class Customer(
    val id: Int,
    val name: String,
)

data class Product(
    val id: Int,
    val title: String,
)

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Failure<T>(val message: String) : DataResult<Nothing>
}

class InMemoryRepository<T>(
    val initialItems: List<T>,
    private val idSelector: (T) -> Int
) {
    private val items = initialItems.toMutableList()

    fun getAll(): List<T> = items

    fun findById(id: Int): DataResult<T> {
        val item = items.find { idSelector(it) == id }
        item?.let {
            return DataResult.Success<T>(item)
        }
        return DataResult.Failure<T>("Item with ID $id was not found")
    }

    fun add(item: T) {
        items.add(item)
    }

}

class Generics {

    @Test
    fun `customer repository returns all customers`() {
        val repository = InMemoryRepository(
            initialItems = listOf(
                Customer(1, "Asef"),
                Customer(2, "Ahmad"),
            ),
            idSelector = { customer ->
                customer.id
            },
        )
        Assert.assertEquals(2,repository.getAll().size)
    }

    @Test
    fun `customer repository finds customer by id`() {
        val repository = InMemoryRepository(
            initialItems = listOf(
                Customer(1, "Asef"),
                Customer(2, "Ahmad"),
            ),
            idSelector = { customer ->
                customer.id
            },
        )
        Assert.assertEquals( Customer(1, "Asef"),repository.findById(1))
    }

    @Test
    fun `customer repository returns failure when customer is missing`() {
        val repository = InMemoryRepository(
            initialItems = listOf(
                Customer(1, "Asef"),
                Customer(2, "Ahmad"),
            ),
            idSelector = { customer ->
                customer.id
            },
        )
        Assert.assertEquals( Customer(3, "Asef"),repository.findById(1))
    }


    @Test
    fun `customer repository adds new customer`() {
        val repository = InMemoryRepository(
            initialItems = listOf(
                Customer(1, "Asef"),
                Customer(2, "Ahmad"),
            ),
            idSelector = { customer ->
                customer.id
            },
        )
        repository.add(Customer(3, "Ali"))
    }

    @Test
    fun `same generic repository works with products`() {

    }

}
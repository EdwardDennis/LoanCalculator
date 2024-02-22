# Loan Calculator

A simple console-based loan calculator in Scala. The application allows users to perform loan calculations and view their previous calculations.

## Important Classes

### LoanController

This is the main controller of the application. It handles the interaction with the user. Available commands are:

- `c`, `calc`, `calculate`: Perform a new loan calculation.
- `v`, `view`: View previous loan calculations.
- `x`, `exit`: Close the application.

When handling a new loan calculation, `LoanController` will prompt the user to enter the loan details: start date, end date, amount, currency, base interest rate, and margin.

### LoanService

`LoanService` interacts with the data store (`DataServiceImpl`) to add, edit, and retrieve `Loan` instances. There is a helper method `loanToString` to convert the `Loan` instances into a string format for display to the user.

### DataService

This implementation uses a TrieMap as an in-memory data storage for the records. The TrieMap data structure is a type of Map with constant time for lookup, insertion, and removal operations. It is based on a hash trie data structure which is a very efficient data structure for functional update. The TrieMap is thread-safe, which makes it a good option for dealing with concurrent updates.

The class uses an AtomicInteger as a counter for generating unique IDs for each record entry. This allows each record to have a unique identifier while ensuring thread safety.

Here's a bit more detail about key methods:

add(t: T): Adds an element of type T to the store (a TrieMap). It uses an AtomicInteger to create a unique ID for each new entry. The getAndIncrement() method atomically increments the current ID and returns the previous value, ensuring that each ID is unique.

edit(id: Int, t: T): Updates an existing element in the store based on the id. It first checks to see if the id exists in the store, and if it does, it replaces the current element with t. If the id does not exist, it returns false.

getAll: Returns a list of all records in the store. Each record is represented as a pair, with the unique ID as the first element and the record data as the second.

## Usage

### Running the application

1. Open your terminal/console.
2. Navigate to the project directory.
3. Enter the following command to start running the application:

```sh
sbt run
```

### Running the unit tests

1. Open your terminal/console.
2. Navigate to the project directory.
3. Enter the following command to start running the unit tests:

```sh
sbt run
```

## Future improvements
- Increase test coverage
- Data persistence
- Decouple user interaction from business login in LoanController
- Enhance validation
- Improving the UI
- Functional effects libraries
- Internationalisation

# My Money
A single page web application to report user's income and expenses based on bank card transactions exported from online banking systems(currently support ANZ csv format)\
The key issue it tries to address is telling user where were their money spent.
It automatically tags user's transactions into different categories based on a few dictionary files.\
It is currently hosted at <a href="https://sohpie0217nzmymoneyapi.azurewebsites.net/" target="_blank">MyMoney on Azure</a>

# Technology Stack
## React
A website template from MUI is obtained to provide styles and main web frames.
Front end interact with backend application via Rest APIs.
![example page](MyMoney.png "My Money example")

## Backend
A Java Spring Boot application is created to provide Rest API services. following Spring Boot component is used:
- Starter-Web 
- Starter-Data-JPA
- Starter-Security

## DataBase
Embedded H2 database (memory) is used to store imported transaction data. As JPA is used for persistence, switch to a formal database can be achieved easily.\
Persisting H2 data into disk seems not necessary, as the application will be run in container, writing to container internal storage is meaningless.\
As the application is only for demo purpose, not persisting user's bank transaction data reduce the chance of data leaking.

## Security
As the application is intended to be hosted in public cloud, to secure communication between React and Backend, Json Web Token is used for api calls.
User login Sequence:\
![sequence](login.png "User login Sequence Diagram")

Get user transactions from browser after login\
![sequence](getTransaction.png "Get Transactions Sequence Diagram")

Except requests used for login (to obtain token) and signup

## Data Import
The application is able to process csv file exported from ANZ online bank. It also allows user to pack multi files in to one zip file, which will be recursively processed by the java backend.\
Support of files from other banks could be added. but it seems not an ideal solution. Importing data from Bank's API with user's consent would be the ideal solution.       

## Expense Tagging
The application implemented a rule based tagging mechanism, which tags transactions based on a few existing dictionary files.
It may not able to cover new user's transaction data. \
Common approach could be adding a few new pages to maintain a category table, and allow user to select uncategorized transactions. But this is not an ideal solution, as it would be too much work thus impacting user experience.\
An ideal  solution would be introducing some machine learning mechanism and learn from existing data and do a pre-categorize, and let user review/correct the category. System will be getting smarter and smarter, and eventually able to categorize transactions without user interference.\

## Data Privacy
The biggest challenge for the application to be used by public would be data privacy, sharing ones bank transactions to an unknown website would be a big concern for most people.\
Maybe an App that runs on mobile device that does not upload any data to third party is a better option for such features. 



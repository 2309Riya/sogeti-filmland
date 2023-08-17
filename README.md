**# Sogeti-filmland**

This is a Filmland project with multiple features

**Customer can log in**

•	Login credentials are stored in the database

• The password is hashed into the database

•	JWT Authorization key and use this key with all requests to the server for authentication


**Getting categories**

The customer will have get categories that are both available and have been subscribed to by them.

**Customer can subscribe to a category**


•	Customers are eligible to subscribe to categories that are listed among the available options. 

•	Initially, customers can enjoy a free subscription for the first time, with a marked price of 0. Subsequent subscriptions will incur charges according to the prices specified for the available categories. 

•	It is not possible for customers to subscribe to categories they are already subscribed to.

•	Following the initial free subscription, customers are required to make payments for their subsequent subscriptions. 

•	A background scheduler is operational to monitor expiring subscriptions and alert customers to renew for continued access. 

•	Subscriptions will expire after 1 month, necessitating customers to renew their subscriptions for continued category access.

**Subscribers can share a category.**


•	Only categories that the subscriber is subscribed to can be shared. 

•	The subscriber is not allowed to share categories that fall under the free subscription.

•	Sharing the latest available content is not permissible for the subscriber. 

•	Categories that are already accessible to the recipient cannot be shared by the subscriber.

•	The content shared by the subscriber will depend on the remaining content, with pricing applied accordingly. For instance, if there are 4 units of remaining content, it will be shared equally. However, if there are 5 units, they will be shared as 3 and 2 units, and prices will be determined based on the content allocation.


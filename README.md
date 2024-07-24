# PriceCalculator
Simple Android app to calculate a price fast considering tax, tip, discount, and number of people splitting the bill.

The result of my introduction to Kotlin and Android Studio.
I started by following this tutorial of creating a tip calculator https://youtube.com/playlist?list=PL7NYbSE8uaBCMVBVg6cskGzdYguj3CUP-&feature=shared and then implemented my own features to make it a more general price calculator:
- Canadian tax rule: the user selects their province from a drop-down menu and the system dynamically updates the tax and total amount. This is done by parsing an XML document of the Canadian provinces' tax rules and adding up federal and provincial tax together.
- Custom tax option: a customizable tax percentage appears if the user selects N/A as a province.
- Discount option: the user enters the discount percentage and the app updates the total amount.
- Split option: upon entering the number of people splitting the bill, the app displays the total amount per person.

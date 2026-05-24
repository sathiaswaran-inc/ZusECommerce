# zusshopping
E-Commerce

The API and Documentions: https://ecommerce.routemisr.com/
The Design Inspired From: https://dribbble.com/shots/26660565-Luxury-E-commerce-Fashion-Shopping-App-UI 

Q1Answer:
I have design this project by using MVVM clean architecture and for the api response i used the generic Resources<T> wrapper with Success, Loading and Error. This allow the ViewModel to expose single observable stream that ui can react to consistently. 
Besides that, this ensure the ui never receive the success data while still in loading state. You will exprience this on shimmer effect upon loading the Menu, Category and also the Carts.

Q2Answer:
a. i will ask for the cdn optimisation because its takes somethime to load in the product and branch image.
b. i will request this api https://ecommerce.routemisr.com/api/v1/cart to allow me to add the cart more than 1 items.. which means we can simply request to add 2 paramaeters for the quantity and also for the operation remove / add

Q3Answer:
My carts survive normal configurations because its stored inside ViewModel manage by Koin. Since the ViewModel survives the activity recreation, the cart and data all are remain consistent after things rotation. I have used LaunchedEffect(Unit) {storeViewModel.getCartItems()} since we're using Android JEtpack Compose the composalble recreated afer screen rotation the effect run again even the ViewModel already ahving all the data.

Q4Answer,
a. Some animation on each screens and effects
b. Add Transaction History
c. Add customer reviews

Q5Answer,
a. Currently we have stored the api token in sharedprefrences -> need to use Encryped Shared Prefrences.
b. Build proper proguard so that what we can inclde/exclude
c. Permission request dialog so that the user awaare what permission can allow and so on.

# Suddah Utpadah Admin

## Overview
**Sudda Utpadah Admin** is an Android application built using the **MVVM architecture** to manage and monitor data related to users, services, orders, and categories. 
This admin panel allows for seamless data entry, tracking, and operations via Firebase Firestore and Firebase Authentication.

---

## Features
- **User Management**:
  - Add, update, and delete users.
  - View user details and order history.

- **Order Management**:
  - Manage ongoing and completed orders.
  - Maintain an order history.

- **Service Management**:
  - Add, update, and remove services.
  - Categorize services for easier access.

- **Authentication**:
  - Admin login and password recovery.
  - Email verification for security.


---

## Tech Stack
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Firebase Firestore
- **Authentication**: Firebase Authentication
- **UI Framework**: Jetpack Compose and XML
- **Utilities**: Shared Preferences for local storage.

---

## Project File Structure

```yaml
com.neatroots.suddahutpadahadmin
├── activities
│   ├── AuthActivity          // Handles user authentication flow (login, register).
│   ├── HomeMainActivity      // Main activity after successful login, serves as the base UI.
│   ├── SplashActivity        // Initial loading screen, checks authentication status.
│   ├── WelcomeActivity       // Displays onboarding or welcome UI for new users.
├── adapter
│   ├── CategoryAdapter       // RecyclerView adapter for displaying a list of categories.
│   ├── OrderedUserAdapter    // Adapter for listing users who placed orders.
│   ├── OrdersAdapter         // Adapter for managing and displaying a list of orders.
│   ├── OrdersListAdapter     // Adapter for viewing order details in a list.
│   ├── ServiceAdapter        // Adapter for displaying available services in a RecyclerView.
│   ├── UserAdapter           // Adapter for listing users with detailed information.
├── factory
│   ├── AuthViewModelFactory  // Factory class for providing the AuthViewModel instance.
│   ├── MainViewModelFactory  // Factory class for providing the MainViewModel instance.
├── fragmentauth
│   ├── ForgetPasswordFragment // UI for resetting a user's password via email.
│   ├── LoginFragment          // UI for logging in the admin user.
│   ├── RegisterFragment       // UI for admin registration (if applicable).
│   ├── VerificationFragment   // Handles email or OTP verification for authentication.
├── fragments
│   ├── AddServiceFragment     // UI for adding a new service to the database.
│   ├── CategoryFragment       // UI for managing and viewing service categories.
│   ├── EditServiceFragment    // UI for editing service details.
│   ├── HomeFragment           // Main dashboard screen with overview metrics.
│   ├── OrdersFragment         // UI for managing and tracking all orders.
│   ├── OrdersListFragment     // Displays details of individual orders in a list.
│   ├── UserDetailsFragment    // UI for viewing detailed information about a specific user.
│   ├── UserOrderListFragment  // Displays the list of orders placed by a specific user.
│   ├── UsersListFragment      // UI for displaying and managing a list of users.
├── model
│   ├── CartModel              // Data class for representing cart-related data.
│   ├── CategoryModel          // Data class for representing service category details.
│   ├── EmailModel             // Data class for representing email data.
│   ├── OrderModel             // Data class for representing order details.
│   ├── ProductModel           // Data class for representing product details (if applicable).
│   ├── UserModel              // Data class for representing user data.
├── repository
│   ├── AuthRepository         // Handles Firebase Authentication operations.
│   ├── MainRepository         // Handles database operations (Firestore, etc.).
├── utils
│   ├── Constants              // Stores constant values such as collection names or paths.
│   ├── SharedPref             // Handles shared preferences for local data storage.
│   ├── Utils                  // Utility functions used across the app (e.g., formatting).
├── viewmodel
│   ├── AuthViewModel          // ViewModel for managing authentication-related UI data.
│   ├── MainViewModel          // ViewModel for managing app-wide data operations.
```

---

### Key Folders:
- **`activities`**: Contains activity classes for authentication, onboarding, and main navigation.
- **`fragments`**: Fragments for each feature, such as managing users, orders, and services.
- **`adapter`**: RecyclerView adapters for displaying lists of users, orders, and services.
- **`viewmodel`**: ViewModel classes for managing UI-related data.
- **`repository`**: Handles Firebase interaction for data fetching and updates.
- **`model`**: Data classes for orders, users, and services.
- **`utils`**: Contains reusable utility classes, constants, and shared preferences.

---

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/SuddaHutPadahAdmin.git
   ```
2. Open the project in Android Studio.
3. Add your Firebase configuration files (google-services.json) to the app module.
4. Sync the project with Gradle files.
5. Run the app on an emulator or physical device.

---

## Screenshots & App Demo Video

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/0HtzRC8jP4A/0.jpg)](https://www.youtube.com/watch?v=0HtzRC8jP4A)

---

## License
This project is licensed under the MIT License.

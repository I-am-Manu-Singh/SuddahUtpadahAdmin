# Sudda Hut Padah Admin

## Overview
**Sudda Hut Padah Admin** is an Android application built using the **MVVM architecture** to manage and monitor data related to users, services, orders, and categories. 
This admin panel allows for seamless data entry, tracking, and operations via Firebase Firestore and Firebase Authentication.

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

## Tech Stack
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Firebase Firestore
- **Authentication**: Firebase Authentication
- **UI Framework**: Jetpack Compose and XML
- **Utilities**: Shared Preferences for local storage.

## Project Structure
## Project File Structure
```
com.neatroots.suddahutpadahadmin
├── activities
│   ├── AuthActivity
│   ├── HomeMainActivity
│   ├── SplashActivity
│   ├── WelcomeActivity
├── adapter
│   ├── CategoryAdapter
│   ├── OrderedUserAdapter
│   ├── OrdersAdapter
│   ├── OrdersListAdapter
│   ├── ServiceAdapter
│   ├── UserAdapter
├── factory
│   ├── AuthViewModelFactory
│   ├── MainViewModelFactory
├── fragmentauth
│   ├── ForgetPasswordFragment
│   ├── LoginFragment
│   ├── RegisterFragment
│   ├── VerificationFragment
├── fragments
│   ├── AddServiceFragment
│   ├── CategoryFragment
│   ├── EditServiceFragment
│   ├── HomeFragment
│   ├── OrdersFragment
│   ├── OrdersListFragment
│   ├── UserDetailsFragment
│   ├── UserOrderListFragment
│   ├── UsersListFragment
├── model
│   ├── CartModel
│   ├── CategoryModel
│   ├── EmailModel
│   ├── OrderModel
│   ├── ProductModel
│   ├── UserModel
├── repository
│   ├── AuthRepository
│   ├── MainRepository
├── utils
│   ├── Constants
│   ├── SharedPref
│   ├── Utils
├── viewmodel
│   ├── AuthViewModel
│   ├── MainViewModel
```

### Key Folders:
- **`activities`**: Contains activity classes for authentication, onboarding, and main navigation.
- **`fragments`**: Fragments for each feature, such as managing users, orders, and services.
- **`adapter`**: RecyclerView adapters for displaying lists of users, orders, and services.
- **`viewmodel`**: ViewModel classes for managing UI-related data.
- **`repository`**: Handles Firebase interaction for data fetching and updates.
- **`model`**: Data classes for orders, users, and services.
- **`utils`**: Contains reusable utility classes, constants, and shared preferences.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/SuddaHutPadahAdmin.git
   ```
2. Open the project in Android Studio.
3. Add your Firebase configuration files (google-services.json) to the app module.
4. Sync the project with Gradle files.
5. Run the app on an emulator or physical device.

## Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.

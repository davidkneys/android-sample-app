# Note Application

This application is a technological demo of modern Android application, which relies on selected Android Jetpack components and up to date best practices. The Application itself is a client for adding, removing, updating and loading of basic notes.

This Application is using the “one Activity many Fragments” approach, where Activity serves just as an entry point to the application. The only responsibility of the Activity is to rotate fragments based on user interaction. The Application also supports runtime Language change based on individual Application settings. UI is built with a reactive model in mind currently implemented with RxJava Observables.

## Features:
* support of API >=19
* MVVM architecture with ViewModels and RxJava
* dependency injection with Dagger2
* trivial implementation of Optimistic UI
* application level language preference
* network operations handled by Retrofit library

## Tests
The current code contains 3 basic Unit tests showing how to unit test ViewModels or custom Service classes. Code coverage is not sufficient, those tests serve mainly as an example.

## Future roadmap
* improve Optimistic UI, now it is buggy when the app is doing more than one network Request at a time
* use Android Jetpack Navigator for handling navigation of framework
* insert Animations according to Material design guidelines
* offline first approach with usage of device database
* better support of Online/Offline state handling, for example with BroadcastReceiver based on changing internet connection events
* finding and implementing Use case for IPC through AIDL
* example of instrumented Android test with Espresso framework
* have near to 100% test code coverage

## Installation
The application is built with Gradle build system with the stable version of Android Studio 3.1.3. To install one just needs to fork the repository, let the gradle do the rest and install the App to a device or Emulator with SDK >=19. Current implementation contains only Unit tests without Android dependencies.
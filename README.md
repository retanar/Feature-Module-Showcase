# Feature module template project

Template project for starting android development.

### Quick overview

| Feature      | Short description  |
|--------------|--------------------|
| Architecture | Feature modules    |
| DI           | Hilt               |
| UI           | Compose and MVI    |
| Navigation   | Compose navigation |

### Architecture

Feature modules, each divided into two parts: api and impl. Api modules only contain logic for
navigating to this module, and don't rely on any other module. Impl modules contain screens,
implementation for the navigation, and can rely on other api modules for navigating to them.

Modules' naming scheme is snake_case.

### UI

Single Activity containing bottom navigation bar and Compose screens.

UI architecture: MVVM with (possibly) MVI.
State is passed to UI, Events are passed to ViewModel. No Effects.
Single state stream can be separated into multiple streams if needed.

[BaseVM](app/src/main/java/com/featuremodule/template/BaseVM.kt) is a base class for ViewModels

### Navigation

Compose navigation. Routes are created with the help of api modules. Singleton Navigator is used to
centralize navigation and allow it to be passed to ViewModels. Stream of navigation commands is
observed in the module responsible for navigation (currently: app module).

### Gradle

Version catalogs are used for version management. Additionally convention plugins are added in
buildSrc for easier management of android files, such as
[convention-android-library](buildSrc/src/main/kotlin/convention-android-library.gradle.kts).
Convention plugins introduce some issues with using version catalogs 

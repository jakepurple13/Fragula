# Fragula 2

**Fragula** is a simple swipe-back extension for [navigation component library](https://developer.android.com/jetpack/androidx/releases/navigation) for Android.

![Android CI](https://github.com/massivemadness/Fragula/workflows/Android%20CI/badge.svg) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

![](.github/images/20200301_131107.gif)

---

# Table of Contents

1. [Gradle Dependency](#gradle-dependency)
2. [The Basics](#the-basics)
3. [Arguments](#arguments)

---

## Gradle Dependency

Add this to your module's `build.gradle` file:

```gradle
dependencies {
  ...
  implementation 'com.fragula2:fragula-core:2.0-alpha01'
}
```

The `fragula-core` module contains everything you need to get started with the library. It contains all core and normal-use functionality.

---

## The Basics

**First,** you need to replace `NavHostFragment` with `FragulaNavHostFragment` in your layout:

```xml
<!-- activity_main.xml -->
<androidx.fragment.app.FragmentContainerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/nav_host"
    android:name="com.fragula2.FragulaNavHostFragment"
    app:navGraph="@navigation/nav_graph"
    app:defaultNavHost="true" />
```

**Second,** you need to change all your `<fragment>` destinations in graph with `<swipeable>` as shown below:

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">

    <swipeable
        android:id="@+id/homeFragment"
        android:name="com.example.fragula.HomeFragment"
        android:label="HomeFragment"
        tools:layout="@layout/fragment_home" />

    ...
    
</navigation>
```

And that's it! If you open your app now you'll see that you can swipe fragments like in Telegram, 
Slack and many other apps.

---

## Arguments

In general, you should work with Fragula as if you would work with normal fragments. You should 
strongly prefer passing only the minimal amount of data between destinations. For example, you 
should pass a key to retrieve an object rather than passing the object itself, as the total space 
for all saved states is limited on Android.

***If you need to pass large amounts of data, consider using a ViewModel.***

**First**, add an argument to the destination:

```xml
<swipeable 
    android:id="@+id/detailFragment"
    android:name="com.example.fragula.DetailFragment">
     <argument
         android:name="itemId"
         app:argType="integer"
         android:defaultValue="0" />
 </swipeable>
```

**Second**, create a Bundle object and pass it to the destination using `navigate()`, as shown below: 

```kotlin
val bundle = bundleOf("itemId" to 123)
findNavController().navigate(R.id.detailFragment, bundle)
```

**Finally**, in your receiving destination’s code, use the `getArguments` method to retrieve the Bundle and use its contents:

```kotlin
val textView = view.findViewById<TextView>(R.id.textViewItemId)
textView.text = arguments?.getString("itemId")
```
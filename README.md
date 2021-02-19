# RightMoveApp
A demo app that requests data from a service and calculates average property prices.

- Supported Android API versions: 26 and above
- Programming language: Kotlin
- Supported device types: Phone only
- Supported orientation: Portrait and Landscape

## Architecture
Using MVVM design pattern, great for its modularity and code separation and recommended by Google and has direct support in the platform.
For asynchronous service calls I developed my own simple callback interface to make api calls through Retrofit on the model side. For the purpose of
this demo and it simplicity chose to write my own interface, however there is other libraries that can be imported to carry out interactions with the
network such as RXJava or Coroutine. My implementation allows me to manage my service requests and errors in my base Activity/ViewModel so I have full control.
Used LiveData for observing the viewmodels on the view side making it lifecycle aware of these components

## Dependency Injection
Dependency injection to reduce coupling and simplify testing.
For dependency injection I used Koin. A service locator framework that is much simpler and does not require
code generation and complex set up like dagger.

## Project Structure
I structure code by feature. This method makes the codebase modular. In this project currently just have the 'averageprices' feature package

## Testing
Wrote unit tests covering all the scenarios in my VieModel and Service classes. Also wrote UI tests for the AveragePriceActivity

## Build And Run
The easiest way to build/run application is to open project folder in Android Studio or Intelij with appropriate plug ins
Run the App module on a configured emulator

## Testing
To run tests go to appropriate test folders and run tests
I used the standard Mockito and Espresso testing framework. Also 'com.nhaarman.mockitokotlin2:mockito' a framework that provides useful
helper functions for working with Mockito in Kotlin


## Assumptions
- The endpoint always returns same structure Json with root property being a Json array.
- The price field might not always be present, and cant guarantee it will be a positive number or bigger than 0.
- Design just wants the question and answer in the view and no other components a user can interact with.
- A property price will never exceed the Java Integer max value

## Design Decisions
I chose to use one activity to show the score the view with the answer, which observes Livedata objects in the ViewModels to get data or error sources. The ViewModels implement a callback interface I designed
to make service calls in Service class (MVVM). Error sources and request progress indicators are managed in My BaseServiceViewModel and BaseServiceActivity class which  activities and viewmodels that manage 
service calls can conveniently would extend, so all these background processes can be managed centrally.

Used a thin MVVM architecture layer for the purpose of easily illustrating the separation of concerns for the demo app. As the app grows so would the architecture layers where we could introduce repositories, 
local data sources, network managers etc..

## Some Notes
Used Moshi instead of GSON for handling the Json response from service calls. It has a greater upcoming kotlin support,
allows to easily write custom Json adapters if needed, allows predictable and better Json exception, JsonReader.selectName()
avoids unnecessary UTF-8 decoding, its more light weight and amongst others.

In Koin (dependency injection framework) Module.kt, I am able to easily resolve dependencies with the get()
function if they are in context or can be pulled from companion function like for the retrofit object which is then injected to the AveragePriceService class.
Used Moshi's ConverterFactory and custom Moshi adapter to only collect fields that we need from the response payload making our response lean.

Chose to bind network data from the Livedata objects to view components by just setting the text. Data binding could also be used to achieve this.

The MutableLivedata in the Viewmodel are currently set by service callback by explicitly setting the value. This approach could be approved by using Transformations and mapping the data from the 
network source directly to the Livedata.

The Design requirement didn't include any component the user can use to interact with the activity. For that reason the user can't recover from an error stat, for instance when data 
fail to fetch there could be a refresh button to allow the user to request data again internet connection is recovered.

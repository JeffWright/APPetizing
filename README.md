# It's APPetizing!

APPetizing makes it easy to view recipes on the go!

## Features

* MVI/Redux-esque unidirectional data flow
* Shared Element Transitions, other animations
* Unit Tests
* UI Automation smoke test using Espresso
* Supports two-pane layout in landscape or on tablets
* Friendly handling of slow or error-prone network conditions
* Ability to simulate slow or error-prone network conditions, for testing

## Try it out

#### Setup
`echo path/to/your/SDK > local.properties`

or `cp local.properties.sample local.properties` and then edit `local.properties`

#### Run the app:

`./gradlew installRelease`

#### Run the tests:

`./gradlew check`

To simulate poor network conditions, change `simulate_poor_network=true` in gradle.properties.  (only works in debug builds, so: `./gradlew installDebug`)

## Future Tasks

Given more time, I would:

* Add a debug menu to turn on the flaky network toggle (instead of using gradle.properties)
* Add mocked network results so that the smoke test doesn't depend on real data
* Add a wakelock to the meal details screen (I hate it when my phone turns off when I'm cooking and I have to wash my hands to turn it back on)

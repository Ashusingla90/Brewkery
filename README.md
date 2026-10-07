# Brewkery

Brewkery is an Android coffee and bakery ordering application built with Kotlin and Jetpack Compose. It covers a clean ordering flow from menu discovery to order confirmation, and keeps the cart and active order saved on the device.

## Project Overview

Brewkery provides a straightforward in-app ordering journey:

1. **Menu browsing**: Explore drink and bakery items, search, and filter by category. Store info (delivery time, flat fee) is shown at the top.
2. **Product detail/customization**: Open an item, choose size, sugar and milk options, and add it to the cart.
3. **Cart review**: Adjust quantities, review subtotal, tax, delivery fee and total, and place the order. A bottom cart bar on the menu screen shows the item count and running total, with a quick "Proceed to Checkout" action.
4. **Order status**: View the active order/ticket details and return to the menu. An active-order banner on the menu lets you jump back to tracking.

These flows are reflected in the `presentation` package (`menu`, `detail`, `cart`, `order`, and `navigation`).

## Screenshots

| Menu | Detail | Cart | Order |
|---|---|---|---|
| ![Menu screen showing the item list and categories](screenshot/menu_screen.jpg) | ![Detail screen showing product customization options](screenshot/detail_screen.jpg) | ![Cart screen showing selected items and totals](screenshot/cart_screen.jpg) | ![Order status screen showing order ticket and dispatch state](screenshot/order_screen.jpg) |
| *Menu screen* | *Detail/customization screen* | *Cart screen* | *Order status screen* |

## Technology Stack

- **Language**: Kotlin
- **Platform**: Android
- **UI**: Jetpack Compose, Material 3
- **Architecture**: Clean architecture layers (`data` / `domain` / `presentation`) with MVVM and `StateFlow`
- **Dependency Injection**: Hilt (with KSP)
- **Networking**: Retrofit + Moshi
- **Persistence**: Room (local, per-device storage)
- **Navigation**: Navigation Compose
- **Image Loading**: Coil
- **Build System**: Gradle (Kotlin DSL) with a version catalog (`libs.versions.toml`)
- **Java Compatibility**: Java 11 (`sourceCompatibility`/`targetCompatibility`)
- **SDK Levels (app module)**: compile SDK 37, target SDK 37, min SDK 24

## Data and Persistence

- The **menu** (store info, categories, items) is fetched from a remote API using Retrofit and Moshi.
- The **cart** and the **active order** are stored locally with Room, so they survive app restarts.
- Room stores data in the app's private storage, so **every device keeps its own cart and order data**. No account or login is required.
- Each cart line and the active order are saved as JSON (via Moshi) inside Room entities, which keeps the database schema simple while the domain models evolve. Saved entries that can no longer be parsed after a model change are skipped instead of crashing the app.
- Repository interfaces (`CartRepository`, `OrderRepository`) live in the `domain` layer and expose `StateFlow`, so ViewModels do not depend on how the data is stored.

## Setup and Build

### Prerequisites

- Android Studio (latest stable recommended)
- Android SDK for API level 37
- JDK 11 or newer (use the JDK bundled with Android Studio if unsure)

### Open in Android Studio

1. Clone the repository:
   ```bash
   git clone https://github.com/Ashusingla90/Brewkery.git
   ```
2. Open the cloned folder in Android Studio.
3. Let Android Studio sync Gradle dependencies.
4. Select the **app** run configuration.
5. Run on an emulator or physical Android device.

### Command-line build/test

From the project root:

```bash
./gradlew assembleDebug
./gradlew test
```

## Project Structure

Main code is under `app/src/main/java/com/example/brewkery/`:

- `data/` - Remote API (Retrofit), local Room database, mappers, and repository implementations
- `di/` - Hilt modules for network, database and repository bindings
- `domain/` - Business models, repository interfaces, and use cases
- `presentation/` - Compose screens and view models, including:
  - `menu/`
  - `detail/`
  - `cart/`
  - `order/`
  - `navigation/`
  - `common/` (shared components and formatters)
- `ui/theme/` - App theme definitions (colors, typography, Material theming)

## Use of AI Tools

This project was built with AI assistance, mainly **Claude (by Anthropic)**, used as a development helper alongside Android Studio. It was used for:

- Debugging Gradle sync and build errors (KSP, Hilt and AGP version compatibility)
- Guidance and code suggestions for Hilt setup, the Room database layer and repository implementations
- UI refinements in Jetpack Compose (cart bar, cart badge, status bar and edge-to-edge handling)
- Explaining Android concepts such as adaptive launcher icons and manifest configuration
- Drafting and improving this README

All AI-generated suggestions were reviewed, tested and adapted by the project author before being included. The app design, architecture decisions and final code are the author's responsibility.

## Contributing

Contributions are welcome. Please open an issue first to discuss significant changes before submitting a pull request.

Repository author: **Ashusingla90**

## License

No license has been specified yet for this repository. Please contact the author (**Ashusingla90**) before redistributing or reusing the project.

# Surya-Shakti Solar Monitor

I have successfully built the complete **Surya-Shakti Solar Monitor** app according to your requirements. It's a modern, fully functional Android application using Kotlin, Jetpack Compose, MVVM, and Room Database.

## Application Architecture

The app is built on a clean MVVM (Model-View-ViewModel) architecture:

1.  **Data Layer (`data/`)**: Uses a Room Database (`SolarDatabase`) with a `SolarLog` entity and `SolarLogDao` to store daily generation and consumption metrics. A `SolarRepository` handles the data operations using Kotlin Flow for reactive updates.
2.  **Domain/Utils (`utils/`)**:
    *   `SimulationUtils.kt`: Contains algorithms to simulate solar generation based on weather, calculate savings, net usage, exports, and battery levels.
    *   `NotificationHelper.kt`: Manages smart notifications (requires Android 13+ permission), alerting users when generation is high enough for heavy appliances.
3.  **Presentation/UI (`ui/`)**:
    *   **Theme**: A custom, always-dark Material 3 theme (`SolarYellow` and `SolarBlack`) designed specifically for high-contrast outdoor visibility in sunlight.
    *   **ViewModel**: `SolarViewModel` holds the application state (using `StateFlow`) and logic, bridging the UI and the data repository.
    *   **Navigation**: `AppNavigation` manages routing across five main screens via a bottom navigation bar.

## Implemented Screens

I have implemented all the requested screens and components:

### 1. Dashboard (`DashboardScreen.kt`)
The main overview featuring a custom-built, animated **Circular Progress Indicator** that visually compares Solar vs. Grid usage. It displays:
*   Total generated, consumed, savings, and battery level in modern `SolarCard`s.
*   A "Green Score" showing the percentage of energy independence.
*   A quick summary of the latest log entry.

### 2. Generation Log (`GenerationLogScreen.kt`)
Allows users to log their daily solar power:
*   Interactive weather selector (Sunny, Cloudy, Rainy).
*   Auto-simulates generation based on weather choices, providing feedback on the generation quality.
*   Option for manual generation value entry.

### 3. Consumption Tracker (`ConsumptionScreen.kt`)
A tool to track electricity usage:
*   Input fields for previous and current meter readings.
*   Auto-calculates daily consumption.
*   Provides a visual breakdown of energy flow: Net Usage (from Grid) or Exported Energy (to Grid) if generation exceeds consumption.

### 4. Savings Calculator (`SavingsScreen.kt`)
Tracks the financial benefits of the solar system:
*   Configurable per-unit electricity rate (defaults to ₹8).
*   Displays daily, 30-day (monthly), and all-time savings.
*   Shows a recent savings history table.

### 5. Energy Reports (`ReportsScreen.kt`)
A detailed history and analysis screen:
*   Summary statistics at the top.
*   A custom Canvas-based bar chart comparing daily generation vs. consumption over the last 15 days.
*   A detailed, scrollable log table with all recorded data points.

## How to Run the App

The project is fully set up and ready to be compiled in Android Studio:

1.  Open the `SuryaShaktiSolarMonitor` folder in **Android Studio**.
2.  Allow Gradle to sync automatically.
3.  Click the **Run** button (▶️) to launch the app on an emulator or a physical device.

> [!NOTE]
> **Sample Data**: On the first launch, the app automatically seeds 15 days of realistic simulated solar data so you can immediately see the charts, dashboard, and reports in action without having to manually enter days of data.

> [!TIP]
> The app requests the Notification permission on startup on Android 13+ devices. Granting this allows the app to send simulated alerts when solar generation is high enough to run heavy appliances.

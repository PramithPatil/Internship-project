# ☀️ Surya-Shakti Solar Monitor

A smart solar energy monitoring dashboard for Android, built with **Kotlin** and **Jetpack Compose**. Designed for households with rooftop solar panels to track power generation, electricity consumption, savings, and energy independence.

## 📱 Features

### 🏠 Dashboard
- Total Solar Energy Generated (kWh)
- Total Energy Consumed (kWh)
- Net Savings in ₹
- Battery Level (%)
- Green Energy Independence Score (%)
- **Circular Progress Indicator** — Solar vs Grid usage visualization

### ☀️ Generation Log
- Select weather condition (Sunny / Cloudy / Rainy)
- Auto-simulate generation based on weather
- Manual generation value entry
- Instant database logging

### ⚡ Consumption Tracker
- Enter previous & current meter readings
- Auto-calculate daily consumption
- Net energy usage breakdown
- Export detection (when generation > consumption)

### 💰 Savings Calculator
- Configurable per-unit electricity rate (default: ₹8/kWh)
- Daily, monthly, and total savings tracking
- Savings history table
- Formula: `Savings = Solar Units × Per Unit Rate`

### 📊 Reports
- Last 30 days energy history
- Bar chart: Generation vs Consumption
- Detailed log table with weather, savings, and exports
- Summary statistics

### 🔔 Smart Notifications
- "High Sun" alerts when generation is ideal for heavy appliances
- Daily summary notifications

---

## 🏗️ Architecture

```
MVVM (Model-View-ViewModel)
├── data/
│   ├── database/        # Room Entity, DAO, Database
│   └── repository/      # SolarRepository
├── viewmodel/           # SolarViewModel (StateFlow)
├── ui/
│   ├── theme/           # Yellow/Black Material3 theme
│   ├── components/      # Reusable composables
│   ├── screens/         # 5 main screens
│   └── navigation/      # NavHost + routes
└── utils/               # Simulation & Notification helpers
```

## 🛠️ Tech Stack

| Component        | Technology                      |
|------------------|---------------------------------|
| Language         | Kotlin                          |
| UI Framework     | Jetpack Compose + Material 3    |
| Architecture     | MVVM                            |
| Database         | Room (SQLite)                   |
| State Management | ViewModel + StateFlow           |
| Navigation       | Jetpack Navigation Compose      |
| Annotations      | KSP (for Room)                  |
| Notifications    | Android NotificationManager     |

## 🎨 Design

- **Theme**: High-contrast **Yellow + Black** for outdoor sunlight readability
- **Style**: Material Design 3 with rounded cards, smooth animations
- **Typography**: Larger font sizes for outdoor visibility
- **Icons**: Material Icons Rounded

## 📋 Setup Instructions

### Prerequisites
- **Android Studio** Ladybug (2024.2.1) or newer
- **JDK 11** or higher
- **Android SDK** with API level 26+ (minimum) and 35 (target)

### Steps

1. **Clone or open the project** in Android Studio:
   ```
   Open Android Studio → File → Open → Select project folder
   ```

2. **Sync Gradle**: Android Studio will automatically prompt to sync. Click "Sync Now".

3. **Build the project**:
   ```
   Build → Make Project (Ctrl+F9)
   ```

4. **Run on emulator or device**:
   - Select a device/emulator with API 26+
   - Click ▶️ Run (Shift+F10)

5. **Grant notification permission** (Android 13+):
   The app will work without it, but smart notifications require the POST_NOTIFICATIONS permission.

### Troubleshooting

- **Gradle sync fails**: Ensure you have the correct Android SDK installed (API 35).
- **Build errors**: Try `File → Invalidate Caches → Restart`.
- **KSP issues**: Make sure KSP version matches your Kotlin version in `libs.versions.toml`.

## 📦 Dependencies

All managed via Gradle Version Catalog (`gradle/libs.versions.toml`):

- `androidx.room` — Local database
- `androidx.navigation` — Screen navigation
- `androidx.lifecycle` — ViewModel & StateFlow
- `androidx.compose.material3` — UI components
- `androidx.compose.material:material-icons-extended` — Icon set
- `com.google.devtools.ksp` — Annotation processing for Room

## 🔋 Sample Data

On first launch, the app seeds **15 days** of sample data with realistic solar generation values based on varied weather conditions. This lets you immediately explore all features without manual data entry.

## 📐 Key Formulas

```
Net Usage = Consumption - Solar Generation

Export to Grid = Solar Generation - Consumption  (when generation > consumption)

Savings = Solar Units × Per Unit Rate (₹8 default)

Independence Score = (Generation / Consumption) × 100%

Battery Level = (Generation / 10 kWh capacity) × 100%
```

## 📄 License

This project is for educational and demonstration purposes.

---

Built with ☀️ for promoting renewable energy awareness.

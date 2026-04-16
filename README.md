# 💸 Expense Tracker (JavaFX)

A simple and interactive desktop application built using **JavaFX** to track daily expenses, manage budgets, and visualize spending patterns.

---

## 🚀 Features

* ➕ Add and delete expenses
* 📅 Track expenses with category and date
* 📊 Visual representation using Pie Chart
* 💰 Set an expense limit
* ⚠️ Automatic warning when limit is exceeded
* 📉 Progress bar showing budget usage
* 🧠 Smart insights (top spending category & transaction count)
* 🎨 Clean and user-friendly interface

---

## 🛠️ Tech Stack

* **Java (JDK 17+)**
* **JavaFX**
* **VS Code / IntelliJ IDEA**

---

## 📂 Project Structure

```
ExpenseTracker/
│
├── Main.java
├── Controller.java
├── Expense.java
└── README.md
```

---

## ⚙️ Setup Instructions

### 1. Install Java

Ensure Java is installed:

```
java -version
```

---

### 2. Download JavaFX

Download from: https://openjfx.io

Extract it and note the path:

```
C:\javafx-sdk-XX\lib
```

---

### 3. Compile the Project

```
javac ^
--module-path "C:\path\to\javafx\lib" ^
--add-modules javafx.controls ^
*.java
```

---

### 4. Run the Application

```
java ^
-Xmx256m ^
--module-path "C:\path\to\javafx\lib" ^
--add-modules javafx.controls ^
Main
```

---

## 🎯 Usage

1. Enter **amount, category, and date**
2. Click **Add** to record expense
3. Set a **budget limit**
4. Monitor:

   * Total spending
   * Progress bar
   * Insights
   * Pie chart

---

## 💡 Future Improvements

* 💾 Save/load data from file
* 📊 Replace pie chart with advanced graphs
* 🔐 Add login system
* 📱 Convert to mobile app

---

## 👨‍💻 Author

**Aayush Singh**

---

## 📌 Notes

* Ensure correct JavaFX path while running
* Some warnings in CMD are normal for newer Java versions

---



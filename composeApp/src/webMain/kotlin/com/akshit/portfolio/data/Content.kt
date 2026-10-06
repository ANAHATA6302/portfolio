package com.akshit.portfolio.data

// Copy is verbatim from the design reference. No em or en dashes anywhere.

data class Seed(val name: String, val hue: Float)

val SEEDS = listOf(
    Seed("Android green", 145f),
    Seed("Electric violet", 295f),
    Seed("Tangerine", 45f),
    Seed("Ocean", 240f),
    Seed("Bubblegum", 350f),
)

val BOT = listOf(
    "Beep boop. I'm Bit.",
    "Stop poking, I'm compiling.",
    "Fun fact: I'm 100% Compose.",
    "Try the Work section. Totally unbiased.",
    "onPause() called. Just kidding.",
)

val COFFEE = listOf(
    "Coffee level: optimal.",
    "Release confidence +12%.",
    "Gradle is still faster than the kettle.",
    "Okay, that's a lot of coffee.",
    "Shipping to production. On caffeine.",
)

val BUBBLES = listOf(
    "Hi, I'm Bit. Poke me.",
    "Psst. Pull the status bar.",
    "I run on coffee and coroutines.",
)

/** Background and foreground refer to palette roles by name, see Widgets in HowIBuild. */
data class Widget(
    val id: String,
    val label: String,
    val title: String,
    val sub: String,
    val c: Int,
    val r: Int,
)

val WIDGETS = listOf(
    Widget("sec", "SECURITY_CHAMPION", "Security champion", "Threat modelling. API reviews. Dependency vulnerability scanning. Someone has to be the friendly paranoid one.", 2, 2),
    Widget("arch", "ARCHITECTURE", "MVVM and MVI", "On Clean Architecture. Layers that stay in their lane.", 2, 1),
    Widget("flow", "ASYNC", "Coroutines and Flow", "Structured and cancellable.", 1, 1),
    Widget("di", "DI", "Koin and Hilt", "Whichever fits.", 1, 1),
    Widget("kmp", "MULTIPLATFORM", "Kotlin Multiplatform", "Shared logic, native feel. Also: this website.", 2, 1),
    Widget("ktor", "NETWORKING", "Ktor", "Typed clients everywhere.", 1, 1),
    Widget("fb", "BACKEND", "Firebase", "Auth, Firestore, Storage, Functions.", 1, 1),
    Widget("test", "TESTING", "WireMock and friends", "JVM unit tests, wiring tests, emulator tests.", 2, 1),
    Widget("ci", "CI_CD", "GitHub Actions", "ktlint and detekt on every PR. Red builds don’t merge.", 2, 1),
)

data class Job(
    val cb: String,
    val whenText: String,
    val role: String,
    val org: String,
    val current: Boolean,
    val lines: List<String>,
)

val CAREER = listOf(
    Job(
        "onResume()", "Apr 2023 to now", "Android Engineer", "Jaguar Land Rover, Manchester", true,
        listOf(
            "Building the Range Rover App in Kotlin, Compose and Clean Architecture.",
            "Own remote connectivity: every command to the car and every byte of data back.",
            "Team security champion: threat models, API reviews, dependency scans.",
        ),
    ),
    Job(
        "onStart()", "Sep to Dec 2022", "Contract Android Developer", "IDS Logic", false,
        listOf(
            "Built a frictionless checkout with Google One Tap and Google Pay, cutting checkout latency by 50%.",
            "Integrated ExoPlayer for adaptive streaming and rebuilt the in-app purchase pipeline.",
            "Fixed tricky production bugs while keeping the codebase modular and testable.",
        ),
    ),
    Job(
        "onCreate()", "Oct 2021 to Aug 2022", "Android Engineer", "Freelance", false,
        listOf(
            "Built and published Android apps for clients.",
            "Shipped my own apps to Google Play along the way.",
        ),
    ),
)

data class CarAct(val id: String, val label: String, val on: String, val toast: String)

val ACTS = listOf(
    CarAct("unlock", "Unlock", "Unlocked", "Unlocked. Relax, it's not your car."),
    CarAct("beep", "Beep", "Beeped", "Beep beep. Somewhere a neighbour sighs."),
    CarAct("climate", "Climate", "21°C", "Pre-conditioning to 21°C. Toasty."),
    CarAct("locate", "Locate", "Found", "Found it. Exactly where you parked it."),
)

val BOOT = listOf(
    "akshitOS 16 booting",
    "> loading coroutines... ok",
    "> inflating whimsy... ok",
    "> BOOT_COMPLETE",
)

object Links {
    const val VANTAGE = "https://play.google.com/store/apps/details?id=com.akshit.vantagepoint"
    const val RANGE_ROVER = "https://play.google.com/store/apps/details?id=com.jaguarlandrover.rangerover.app"
    const val ROAMIO = "https://play.google.com/store/apps/details?id=com.akshit.roamio"
    const val POTTERPEDIA = "https://play.google.com/store/apps/details?id=com.akshit.potterpedia"
    const val DEVELOPER = "https://play.google.com/store/apps/developer?id=Akshit+Nahata"
    const val LINKEDIN = "https://www.linkedin.com/in/akshit-nahata"
    const val GITHUB = "https://github.com/ANAHATA6302"
    const val INSTAGRAM = "https://www.instagram.com/ak__it/"
    const val EMAIL = "account@akshitnahata.com"
}

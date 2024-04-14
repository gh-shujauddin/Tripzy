import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.qadri.tripzy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.qadri.tripzy"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            val localProperties = Properties()
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
            buildConfigField("String", "API_KEY", "${localProperties["API_KEY"]}")
            buildConfigField("String", "Here_API_KEY", "${localProperties["Here_API_KEY"]}")
            buildConfigField("String", "Places_API_KEY", "${localProperties["Places_API_KEY"]}")
            buildConfigField("String", "ServerClient", "${localProperties["ServerClient"]}")

        }
        release {
            val localProperties = Properties()
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
            buildConfigField("String", "API_KEY", "${localProperties["API_KEY"]}")
            buildConfigField("String", "Here_API_KEY", "${localProperties["Here_API_KEY"]}")
            buildConfigField("String", "Places_API_KEY", "${localProperties["Places_API_KEY"]}")
            buildConfigField("String", "ServerClient", "${localProperties["ServerClient"]}")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2024.04.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-util:1.3.0")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.5.3")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")


    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
//Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    ksp ("com.google.dagger:hilt-compiler:2.47")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    //Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    //Ktor
    implementation( "io.ktor:ktor-client-core:2.1.3")
    implementation( "io.ktor:ktor-client-android:2.1.3")
    implementation ("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation ("io.ktor:ktor-serialization-kotlinx-json:2.1.3")
    implementation ("io.ktor:ktor-client-logging:2.1.3")
    implementation("io.ktor:ktor-serialization-gson:2.3.2")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    //Firebase
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")

    // Maps
    implementation(project(":maps"))

    // Dialogs
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Swipe
    implementation("me.saket.swipe:swipe:1.2.0")

    //Lottie-compose
    implementation("com.airbnb.android:lottie-compose:5.2.0")

    // Reorderable Lists
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")

    implementation ("com.google.accompanist:accompanist-permissions:0.35.0-alpha")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}